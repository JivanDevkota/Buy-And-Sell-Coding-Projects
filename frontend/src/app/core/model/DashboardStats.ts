export interface SalesStat{
  count: number;
  revenue: number;
}

export interface DashboardStats {
  today: SalesStat;
  thisWeek: SalesStat;
  thisMonth: SalesStat;
}
