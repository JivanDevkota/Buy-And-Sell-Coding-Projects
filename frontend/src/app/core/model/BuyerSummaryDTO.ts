export interface BuyerSummaryDTO{
  id: number;
  username: string;
  email: string;
  fullName: string;
  profileImgUrl: string;
  createdAt: string;
  totalPurchases: number;
  totalSpent: number;
  reviewCount: number;
  status: string;
}
