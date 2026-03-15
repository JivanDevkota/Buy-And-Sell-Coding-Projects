import {Component, OnInit} from '@angular/core';
import {LocalstorageService} from "../../../core/services/localstorage.service";
import {PurchaseHistoryResponse} from "../../../core/model/PurchaseHistoryResponse";
import {BuyerService} from "../../../core/services/buyer.service";
import {BuyerStats} from "../../../core/model/buyer-stats";

@Component({
  selector: 'app-dashboard-data',
  templateUrl: './dashboard-data.component.html',
  styleUrls: ['./dashboard-data.component.css']
})
export class DashboardDataComponent implements OnInit {

  stats!: BuyerStats

  username = '';
  activeTab = 'overview';

  // Download state — track per file and per project
  downloadingZip: { [projectId: number]: boolean } = {};
  downloadSuccess: { [key: string]: boolean } = {};

  constructor(
    private buyerService: BuyerService,
  ) {}


  tabLabels: Record<string, string> = {
    overview:  'Overview',
    purchases: 'My Purchases',
    downloads: 'Downloads',
    wishlist:  'Wishlist',
    cart:      'Shopping Cart',
    reviews:   'My Reviews',
    orders:    'Order History',
  };

  recentPurchases = [
    { title: 'Quiz Learning Platform',  tags: 'Python Django • Educational • Full source code included', price: '34.99', status: 'Downloaded', icon: 'bi-mortarboard-fill', color: '#ede9fe' },
    { title: 'Social Media App',        tags: 'React Native • Mobile App • Firebase backend',            price: '59.99', status: 'Available',  icon: 'bi-people-fill',      color: '#fef9c3' },
    { title: 'Hotel Booking API',       tags: 'Node.js • REST API • Complete documentation',             price: '45.99', status: 'Available',  icon: 'bi-building-fill',    color: '#fee2e2' },
  ];

  cartItems = [
    { title: 'Quiz Learning Platform',  tags: 'Python Django • Educational • Full source code included', price: '34.99', icon: 'bi-mortarboard-fill', color: '#ede9fe' },
    { title: 'Social Media App',        tags: 'React Native • Mobile App • Firebase backend',            price: '59.99', icon: 'bi-people-fill',      color: '#fef9c3' },
    { title: 'Hotel Booking API',       tags: 'Node.js • REST API • Complete documentation',             price: '45.99', icon: 'bi-building-fill',    color: '#fee2e2' },
  ];

  ngOnInit(): void {
    this.username = LocalstorageService.getUserName() ?? 'Buyer';
    this.getDashStats();
  }

  getDashStats(){
    this.buyerService.getBuyerDashStats().subscribe({
      next: (data) => {
        this.stats = data;
        console.log(data)
      },
      error: (err) => {
        console.error('Failed to load stats', err);
      }
    })
  }

  setTab(tab: string): void {
    this.activeTab = tab;
  }


}
