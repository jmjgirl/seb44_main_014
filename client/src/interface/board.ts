export type Nullable<T> = T | null;

// Page Board - borad list info
export interface IBoardList {
  postId: number;
  memberId: number;
  name: string;
  avgStarRate: number;
  viewCount: number;
  commentCount: number;
  status: string;
  category: string;
  title: string;
  createdAt: string;
  postTag: {
    postTagId: number;
    genderTagId: number;
    foodTagId: number;
  };
}

export interface IFilterInfo {
  page: number;
  category: string;
  genderTag: Nullable<number>;
  foodTag: Nullable<number>;
}

export interface IPageInfo {
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

// Page BoardDetail - mate info
export interface IMateMember {
  mateMemberId: number;
  name: string;
}

// Page BoardDetail - comment info
export interface IComments {
  commentId: number;
  content: string;
  memberId: number;
  avgStarRate: number;
  name: string;
  createdAt: string;
  image: string;
  gender: string;
  eatStatus: boolean;
}

// Page BoardDetail - general info
export interface IBoardDetailData {
  title: string;
  content: string;
  image?: string;
  createdAt: string;
  viewCount: number;
  commentCount: number;
  status: string;
  category: string;
  member: {
    memberId: number;
    image: string;
    name: string;
    gender: string;
    avgStarRate: number;
    eatStatus: boolean;
  };
  postTag: {
    postTagId: number;
    foodTagId: number;
    genderTagId: number;
  };
  mate: {
    findNum: number;
    mateNum: number;
  };
  mateMembers: IMateMember[];
  comments: IComments[];
}

// Page BoardDetail - Participants info
export interface IParticipants {
  memberId: number;
  name: string;
  image: string;
  gender: string;
  eatStatus: boolean;
  avgStarRate: number;
}

// Page PostBoard - post info
export interface IPostInfo {
  memberId: Nullable<number>;
  category: string;
  title: string;
  content: string;
  genderTag: {
    genderTagId?: number;
  } | null;
  foodTag: {
    foodTagId?: number;
  } | null;
  mate: {
    mateNum: Nullable<number>;
  };
}

// Page Edit Board - edit info
export interface IEditInfo extends IPostInfo {
  status?: string;
}
