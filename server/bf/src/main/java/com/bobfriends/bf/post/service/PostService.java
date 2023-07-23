package com.bobfriends.bf.post.service;

import com.bobfriends.bf.auth.jwt.JwtTokenizer;
import com.bobfriends.bf.exception.BusinessLogicException;
import com.bobfriends.bf.exception.ExceptionCode;
import com.bobfriends.bf.location.repository.LocationRepository;
import com.bobfriends.bf.location.service.LocationService;
import com.bobfriends.bf.mate.service.MateService;
import com.bobfriends.bf.member.service.MemberService;
import com.bobfriends.bf.post.dto.PostDto;
import com.bobfriends.bf.post.entity.Post;
import com.bobfriends.bf.post.entity.PostTag;
import com.bobfriends.bf.post.repository.PostRepository;
import com.bobfriends.bf.tag.repository.FoodRepository;
import com.bobfriends.bf.tag.repository.GenderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final FoodRepository foodRepository;

    private final GenderRepository genderRepository;

    private final PostTagService postTagService;

    private final MemberService memberService;

    private final MateService mateService;

    private final LocationService locationService;

    private final LocationRepository locationRepository;

    private final JwtTokenizer jwtTokenizer;

    /**
     * 게시글 등록
     * - [밥먹기] 성별 태그 / 음식 태그 선택 (default : 남녀노소(3), 기타(5))
     * - [장보기] 음식 태그 사용 X
     */
    public Post createPost(Post requestBody){

        memberService.findVerifiedMember(requestBody.getMember().getMemberId());

        return postRepository.save(requestBody);
    }


    /** 게시글 수정 **/
    public Post updatePost(long postId, PostDto.Patch patch){

        Post findPost = findVerifiedPost(postId);

        if(findPost.getMember().getMemberId() == patch.getMemberId()){

            Optional.ofNullable(patch.getCategory()).ifPresent(category -> findPost.setCategory(category));
            Optional.ofNullable(patch.getTitle()).ifPresent(title -> findPost.setTitle(title));
            Optional.ofNullable(patch.getContent()).ifPresent(content -> findPost.setContent(content));
            Optional.ofNullable(patch.getStatus()).ifPresent(status -> findPost.setStatus(status));

            if(patch.getGenderTag() != null){
                PostTag postTag2 = postTagService.updatePostGenderTag(findPost, patch.getGenderTag());
                findPost.setPostTag(postTag2);
            }

            if(patch.getFoodTag() != null){
                PostTag postTag1 = postTagService.updatePostFoodTag(findPost, patch.getFoodTag());
                findPost.setPostTag(postTag1);
            }

            if(patch.getMate() != null){
                mateService.updateMate(findPost, patch.getMate());
            }

            return postRepository.save(findPost);

        }else {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_POST);
        }
    }


    /** 질문 상세 조회 **/
    public Post findPost(long postId){

        Post post = findVerifiedPost(postId);
        post.addViewCount(post.getViewCount());

        return post;
    }
    /** 로그인 전 전체 질문 검색 **/
    public Page<Post> searchPostsNotLogin(Pageable pageable, String keyword, String category, Long genderTag, Long foodTag){
        return postRepository.findBySearchOption(pageable, keyword, category, genderTag, foodTag);
    }

    /** 로그인 후 (위치 적용) 전체 질문 검색 **/
    public Page<Post> searchPosts(Pageable pageable, String keyword, String category, Long genderTag, Long foodTag, String token){

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        long memberId = jwtTokenizer.getMemberIdFromToken(token, base64EncodedSecretKey);

        // 현재 사용자의 위치를 가져옴
        Point point = locationService.locationRegisteredMember(memberId).getPoint();

        List<Long> memberIdList = locationRepository.findByNativeQuery(point);

        List<Post> postsByMemberId = memberIdList.stream()
                .flatMap(id -> postRepository.findAllByMemberId(id).stream())
                .collect(Collectors.toList());

        /** 여기까지가 위치로 뽑은 근방에 있는 member의 postList**/

        List <Post> filteredPosts = postRepository.findBySearchOptionNoPage(keyword, category, genderTag, foodTag);

        List<Post> commonPosts = postsByMemberId.stream()
                .filter(filteredPosts::contains)
                .collect(Collectors.toList());

        Collections.sort(commonPosts, (post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()));


        if (memberIdList.size() < 3)
            return postRepository.findBySearchOption(pageable, keyword, category, genderTag, foodTag);
        else
            return new PageImpl<>(commonPosts, pageable, commonPosts.size());

    }

    /** 질문 삭제 **/
    public void deletePost(long postId, String token){

        Post findPost = findVerifiedPost(postId);

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        long loginId = jwtTokenizer.getMemberIdFromToken(token, base64EncodedSecretKey);

        long writerMemberId = findPost.getMember().getMemberId();

        // 로그인 한 회원이 작성자이면
        if(loginId == writerMemberId){
            postRepository.delete(findPost);
        }else {
            throw new RuntimeException("등록한 작성자가 아닙니다");
        }
    }


    /** 등록된 태그가 존재하는지 확인 **/
    private void verifyTag(Post requestBody){

        // 음식 태그가 존재하지 않으면
        if(foodRepository.findById(requestBody.getPostTag().getFoodTag().getFoodTagId()) == null){
            throw new BusinessLogicException(ExceptionCode.FOOD_TAG_NOT_FOUND);
            // 성별 태그가 존재하지 않으면
        }else if(genderRepository.findById(requestBody.getPostTag().getGenderTag().getGenderTagId()) == null){
            throw new BusinessLogicException(ExceptionCode.GENDER_TAG_NOT_FOUND);
        }
    }


    /** 질문이 등록된 질문인지 확인 **/
    public Post findVerifiedPost(long postId){

        Optional<Post> findPost = postRepository.findById(postId);

        Post post = findPost.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));

        return post;
    }

}
