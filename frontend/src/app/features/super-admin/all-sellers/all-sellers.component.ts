import {Component, OnInit} from '@angular/core';
import {AdminService} from '../../../core/services/admin.service';
import {SellerSummaryDTO, PagedResponse} from '../../../core/model/Seller';

@Component({
  selector: 'app-all-sellers',
  templateUrl: './all-sellers.component.html',
  styleUrls: ['./all-sellers.component.css']
})
export class AllSellersComponent implements OnInit {
  sellers: SellerSummaryDTO[] = [];
  totalElements = 0;
  totalPages = 0;
  currentPage = 0;
  pageSize = 10;
  loading = false;
  error = false;

  stats = {
    totalSellers: 0,
    verified: 0,
    pending: 0,
    suspended: 0,
    revenue: 0,
    totalProjects: 0
  };

  selectedStatus: string = 'ALL';

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.loadSellers();
  }

  loadSellers(page: number = 0) {
    this.loading = true;
    this.error = false;
    this.currentPage = page;

    this.adminService
      .getSellers(page, this.pageSize, this.selectedStatus)
      .subscribe({
        next: (response: PagedResponse<SellerSummaryDTO>) => {
          this.sellers = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.calculateStats();
          this.loading = false;
        },
        error: () => {
          this.error = true;
          this.loading = false;
        }
      });
  }

  onStatusChange(event: any) {
    this.selectedStatus = event.target.value;
    this.loadSellers(0);
  }

  calculateStats() {
    this.stats.totalSellers = this.totalElements;
    this.stats.verified = this.sellers.filter(s => s.status === 'ACTIVE').length;
    this.stats.pending = this.sellers.filter(s => s.status === 'PENDING').length;
    this.stats.suspended = this.sellers.filter(s => s.status === 'SUSPENDED').length;
    this.stats.revenue = this.sellers.reduce((sum, s) => sum + (s.totalRevenue || 0), 0);
    this.stats.totalProjects = this.sellers.reduce((sum, s) => sum + (s.totalProjects || 0), 0);
  }

  onPageChange(page: number) {
    this.loadSellers(page);
  }

  getAverageRating(): number {
    if (this.sellers.length === 0) return 0;
    const ratedSellers = this.sellers.filter(s => s.averageRating > 0);
    if (ratedSellers.length === 0) return 0;
    const total = ratedSellers.reduce((sum, s) => sum + s.averageRating, 0);
    return Math.round((total / ratedSellers.length) * 10) / 10;
  }

  getTopSeller(): SellerSummaryDTO | null {
    if (this.sellers.length === 0) return null;
    return this.sellers.reduce((top, s) =>
      (!top || (s.totalRevenue || 0) > (top.totalRevenue || 0)) ? s : top, null as any);
  }
}

