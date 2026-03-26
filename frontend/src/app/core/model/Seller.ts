
export type SellerStatus = 'ACTIVE' | 'PENDING' | 'SUSPENDED' | 'INACTIVE';


export interface SellerSummaryDTO {
  id: number;
  username: string;
  fullName: string;
  profileImgUrl: string | null;
  status: SellerStatus;
  totalProjects: number;
  totalSales: number;
  totalRevenue: number;
  averageRating: number;
  createdAt: string; // ISO date string
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;   // current page
  size: number;
}



export interface StatusConfig {
  label: string;
  badgeClass: string;
}

export const STATUS_CONFIG: Record<SellerStatus, StatusConfig> = {
  ACTIVE:    { label: 'Verified',             badgeClass: 'badge-active'    },
  PENDING:   { label: 'Pending Verification', badgeClass: 'badge-pending'   },
  SUSPENDED: { label: 'Suspended',            badgeClass: 'badge-suspended' },
  INACTIVE:  { label: 'Inactive',             badgeClass: 'badge-inactive'  },
};
