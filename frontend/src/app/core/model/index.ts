export * from './enums';
export * from './project.models';
export * from './language.models';
export * from './user.models';
export * from './purchase.models';
export * from './review-wishlist.models';
export * from './category.model';

export interface BuyerStats {
  lifetimeSpend: number;
  purchasedCount: number;
  wishlistCount: number;
}

export interface DashboardStats {
  today: { count: number; revenue: number };
  thisWeek: { count: number; revenue: number };
  thisMonth: { count: number; revenue: number };
}

export interface SellerDashboard {
  sellerId?: number;
  username?: string;
  totalProjects?: number;
  activeProjects: number;
  pendingProjects?: number;
  totalSales: number;
  salesChange: number;
  totalEarnings: number;
  earningsChange: number;
  averageRating: number;
  ratingChange: number;
  recentSales?: any[];
}

export interface DashboardUserStats {
  totalUsers: number;
  totalSellers: number;
  totalBuyers: number;
  activeUsers: number;
  suspendedUsers: number;
}

export interface SellerSummaryDTO {
  id: number;
  username: string;
  email: string;
  fullName?: string;
  profileImgUrl?: string;
  isActive: boolean;
  totalProjects: number;
  totalSales: number;
  totalEarnings: number;
  joinedAt: string;
}

export interface BuyerSummaryDTO {
  id: number;
  username: string;
  email: string;
  fullName?: string;
  profileImgUrl?: string;
  isActive: boolean;
  totalPurchases: number;
  totalSpend: number;
  joinedAt: string;
}

export interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export interface PendingProjects {
  id: number;
  title: string;
  description: string;
  price: number;
  sellerId: number;
  sellerUsername: string;
  categoryName: string;
  languages: string[];
  tags: string[];
  status: string;
  submittedAt: string;
}

export interface PendingSellerApprovals{
  id: number;
  username: string;
  email: string;
  requestedDate: string;
  projectCount: number;
}


export interface PendingApprovalsSummary {
  "PENDING SELLERS": number;
  "PENDING PROJECTS": number;
}
