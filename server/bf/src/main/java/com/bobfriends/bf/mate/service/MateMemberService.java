package com.bobfriends.bf.mate.service;

import com.bobfriends.bf.exception.BusinessLogicException;
import com.bobfriends.bf.exception.ExceptionCode;
import com.bobfriends.bf.mate.dto.MateMemberDto;
import com.bobfriends.bf.mate.entity.Mate;
import com.bobfriends.bf.mate.entity.MateMember;
import com.bobfriends.bf.mate.repository.MateMemberRepository;
import com.bobfriends.bf.member.entity.Member;
import com.bobfriends.bf.member.service.MemberService;
import com.bobfriends.bf.post.entity.Post;
import com.bobfriends.bf.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bobfriends.bf.post.entity.Post.recruitStatus.COMPLETE;
@Service
@RequiredArgsConstructor
public class MateMemberService {
    private final MemberService memberService;
    private final MateMemberRepository mateMemberRepository;
    private final PostService postService;
    private final MateService mateService;

    /** Mate create **/
    public MateMember createMateMember(MateMember mateMember, MateMemberDto.PostMateMember post) {
        // 존재하는 회원인지 검증
        Member findMember = memberService.findVerifiedMember(post.getMemberId());

        // postId로 등록한 mateId를 찾음
        // mateId에 mateMember를 등록
        Post post1 = postService.findVerifiedPost(mateMember.getMate().getPost().getPostId());
        mateMember.setMate(post1.getMate());
        Mate mate = mateService.findVerifiedMate(mateMember.getMate().getMateId());

        // 이미 등록된 회원은 다시 등록할 수 없음
        if (post1.getMate().getMateMembers().stream().anyMatch(member -> member.getMember().getMemberId().equals(post.getMemberId()))) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CREATE_SAME_MATE_MEMBER);
            // 작성자는 등록할 수 없음
        } else if (findMember.getMemberId() == post1.getMember().getMemberId()) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CREATE_MATE_MEMBER);
            // 성별 확인
        } else if (findMember.getGender().equals(Member.genderStatus.FEMALE) && findGenderTagByMate(mate) == 2) {
            throw new BusinessLogicException(ExceptionCode.GENDER_DIFFERENT);
        } else if (findMember.getGender().equals(Member.genderStatus.MALE) && findGenderTagByMate(mate) == 1) {
            throw new BusinessLogicException(ExceptionCode.GENDER_DIFFERENT);
        }

        // 구해진 mate 인원
        int findNum = mate.getMateMembers().size() + 1;

        if (mate.getMateNum() == findNum) post1.setStatus(COMPLETE);
        if (mate.getMateNum() < findNum) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CREATE_MATE_MEMBER);
        }
        return mateMemberRepository.save(mateMember);
    }

    /** mate 팀원 전체 조회 **/
    public List<MateMember> getMateMembers(Long postId) {

        Post post1 = postService.findVerifiedPost(postId);

        if(post1.getMate()==null) throw new BusinessLogicException(ExceptionCode.MATE_NOT_FOUND);
        Mate mate = mateService.findVerifiedMate(post1.getMate().getMateId());

        // postId가 일치하는 mateMembers를 List로 찾음

        List<MateMember> mateMembers = mate.getMateMembers().stream()
                .filter(mateMember -> mateMember.getMate().getPost().getPostId().equals(post1.getPostId()))
                .collect(Collectors.toList());

        return mateMembers;

    }

    /** mate의 GenderTag find 메서드 **/
    public Long findGenderTagByMate(Mate mate){

        Long genderTagId = mate.getPost()
                .getPostTag()
                .getGenderTag()
                .getGenderTagId();

        return genderTagId;
    }
}