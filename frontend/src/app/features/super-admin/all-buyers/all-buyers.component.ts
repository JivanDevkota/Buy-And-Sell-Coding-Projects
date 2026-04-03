import { Component, OnInit } from '@angular/core';
import { AdminService } from "../../../core/services/admin.service";
import { BuyerSummaryDTO } from "../../../core/model/BuyerSummaryDTO";

@Component({
  selector: 'app-all-buyers',
  templateUrl: './all-buyers.component.html',
  styleUrls: ['./all-buyers.component.css']
})
export class AllBuyersComponent implements OnInit {

  buyers: BuyerSummaryDTO[] = [];
  loading = true;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadBuyers();
  }

  loadBuyers(): void {
    this.loading = true;
    this.adminService.getBuyer().subscribe({
      next: (data) => {
        console.log(data)
        // Sort by totalSpent descending so top buyers appear first
        this.buyers = data.sort((a, b) => b.totalSpent - a.totalSpent);
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load buyers:', err);
        this.loading = false;
      }
    });
  }

  /** Stat card helpers — computed from the buyers array */

  getTotalSpent(): number {
    return this.buyers.reduce((sum, b) => sum + (b.totalSpent || 0), 0);
  }

  getAvgSpent(): number {
    if (!this.buyers.length) return 0;
    return this.getTotalSpent() / this.buyers.length;
  }

  getTotalPurchases(): number {
    return this.buyers.reduce((sum, b) => sum + (b.totalPurchases || 0), 0);
  }

  getAvgPurchases(): number {
    if (!this.buyers.length) return 0;
    return this.getTotalPurchases() / this.buyers.length;
  }

  getTotalReviews(): number {
    return this.buyers.reduce((sum, b) => sum + (b.reviewCount || 0), 0);
  }

  /** Returns buyer with highest totalSpent */
  getTopBuyer(): BuyerSummaryDTO | null {
    if (!this.buyers.length) return null;
    return this.buyers.reduce((top, b) => b.totalSpent > top.totalSpent ? b : top, this.buyers[0]);
  }

  /** Extracts up to 2 initials from a display name */
  getInitials(name: string): string {
    if (!name) return '?';
    return name
      .split(' ')
      .slice(0, 2)
      .map(n => n[0])
      .join('')
      .toUpperCase();
  }
}
