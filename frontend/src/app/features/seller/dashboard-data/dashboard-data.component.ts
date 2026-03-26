import {Component, OnInit} from '@angular/core';
import {SellerService} from "../../../core/services/seller.service";
import {SellerDashboard} from "../../../core/model/SellerDashboard";

@Component({
  selector: 'app-dashboard-data',
  templateUrl: './dashboard-data.component.html',
  styleUrls: ['./dashboard-data.component.css']
})
export class DashboardDataComponent implements OnInit {
  stats!: SellerDashboard;
  loading = true;
  error = false;

  constructor(private sellerService: SellerService) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.loading = true;
    this.error = false;

    this.sellerService.getSellerDashboard().subscribe({
      next: (data) => {
        this.stats = data;
        console.log(this.stats);
        this.loading = false;
      },
      error: (err) => {
        console.error("Dashboard API Error:", err);

        this.error = true;
        this.loading = false;
      }
    });
  }
}
