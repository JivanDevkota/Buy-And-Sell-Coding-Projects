export interface DashboardUserStats {
  roleCounts: {[key: string]: number};
  totalUsers: number;
  activeUsers: number;
  pendingUsers: number;
}
