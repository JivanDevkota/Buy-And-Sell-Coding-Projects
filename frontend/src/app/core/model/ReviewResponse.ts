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
