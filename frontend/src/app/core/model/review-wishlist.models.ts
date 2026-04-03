export interface Review {
  reviewId: number;
  projectName: string;
  rating: number;
  comment: string;
  reviewerInitials: string;
  timeAgo: string;
  sellerResponse?: string;
  sellerResponseTime?: string;
}

export interface ReviewRequest {
  projectId: number;
  rating: number;
  comment: string;
}

export interface WishlistItem {
  id: number;
  projectName: string;
  projectId: number;
  projectImg: string;
  projectDescription: string;
  price: number;
  languageName: string[];
  addedAt: Date;
}
