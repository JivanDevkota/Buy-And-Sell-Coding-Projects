import {Component} from '@angular/core';
import {SellerService} from "../../../core/services/seller.service";
import {DashboardStats} from "../../../core/model";

@Component({
  selector: 'app-sales',
  templateUrl: './sales.component.html',
  styleUrls: ['./sales.component.css']
})
export class SalesComponent {
  stats: DashboardStats | null = null;
  loading = true
  error: string | null = null;

  cards: any[] = []

  constructor(private sellerService: SellerService) {

  }

  ngOnInit() {
    this.fetchStats();
  }

  fetchStats() {
    this.loading = true;
    this.error = null;

    this.sellerService.getSalesStats().subscribe({
      next: (data) => {
        this.stats = data;
        console.log(this.stats);
        this.loading = false
      },
      error: (err) => {
        this.error = 'Failed to fetch stats';
        this.loading = false
      }
    })
  }
}
