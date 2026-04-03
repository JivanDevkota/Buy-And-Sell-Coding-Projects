import {Component, OnInit} from '@angular/core';
import {LocalstorageService} from "../../../core/services/localstorage.service";
import {BuyerService} from "../../../core/services/buyer.service";
import {PurchaseHistoryResponse, BuyerStats, Review} from "../../../core/model";

@Component({
  selector: 'app-dashboard-data',
  templateUrl: './dashboard-data.component.html',
  styleUrls: ['./dashboard-data.component.css']
})
export class DashboardDataComponent implements OnInit {

  purchases:PurchaseHistoryResponse[] = [];
  loading=true
  error: string |null = null;

  stats: BuyerStats = {
    lifetimeSpend: 0,
    purchasedCount: 0,
    wishlistCount: 0
  };

  username = '';
  activeTab = 'overview';

  // Download state — track per file and per project
  downloadingZip: { [projectId: number]: boolean } = {};
  downloadSuccess: { [key: string]: boolean } = {};

  // Review modal state
  showReviewModal = false;
  reviewProjectId: number = 0;
  reviewProjectTitle: string = '';


  constructor(
    private buyerService: BuyerService,
  ) {}



  getStars(rating: number): number[] {
    return Array(rating).fill(0);
  }

  tabLabels: Record<string, string> = {
    overview:  'Overview',
    purchases: 'My Purchases',
    downloads: 'Downloads',
    wishlist:  'Wishlist',
    cart:      'Shopping Cart',
    reviews:   'My Reviews',
    orders:    'Order History',
  };

  // recentPurchases = [
  //   { title: 'Quiz Learning Platform',  tags: 'Python Django • Educational • Full source code included', price: '34.99', status: 'Downloaded', icon: 'bi-mortarboard-fill', color: '#ede9fe' },
  //   { title: 'Social Media App',        tags: 'React Native • Mobile App • Firebase backend',            price: '59.99', status: 'Available',  icon: 'bi-people-fill',      color: '#fef9c3' },
  //   { title: 'Hotel Booking API',       tags: 'Node.js • REST API • Complete documentation',             price: '45.99', status: 'Available',  icon: 'bi-building-fill',    color: '#fee2e2' },
  // ];

  cartItems = [
    { title: 'Quiz Learning Platform',  tags: 'Python Django • Educational • Full source code included', price: '34.99', icon: 'bi-mortarboard-fill', color: '#ede9fe' },
    { title: 'Social Media App',        tags: 'React Native • Mobile App • Firebase backend',            price: '59.99', icon: 'bi-people-fill',      color: '#fef9c3' },
    { title: 'Hotel Booking API',       tags: 'Node.js • REST API • Complete documentation',             price: '45.99', icon: 'bi-building-fill',    color: '#fee2e2' },
  ];

  ngOnInit(): void {
    this.username = LocalstorageService.getUserName() ?? 'Buyer';
    this.getDashStats();
    this.getPurchases();
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

  getPurchases(): void {
    const buyerId = LocalstorageService.getUserId();
    console.log(buyerId)
    if (!buyerId) return;

    this.buyerService.myPurchaseProjects(Number(buyerId)).subscribe({
      next: (data) => {
        this.purchases = data;
        console.log('✅ Purchases loaded:', data);
      },
      error: (err) => {
        console.error('❌ Failed to load purchases:', err);
      }
    });
  }

  setTab(tab: string): void {
    this.activeTab = tab;
  }

  /**
   * Open review modal for a project
   */
  openReviewModal(projectId: number, projectTitle: string): void {
    this.reviewProjectId = projectId;
    this.reviewProjectTitle = projectTitle;
    this.showReviewModal = true;
    console.log('Opening review modal for project:', projectTitle);
  }

  /**
   * Close review modal
   */
  closeReviewModal(): void {
    this.showReviewModal = false;
    this.reviewProjectId = 0;
    this.reviewProjectTitle = '';
  }

  /**
   * Handle successful review submission
   */
  onReviewSubmitted(review: any): void {
    console.log('✅ Review submitted successfully:', review);
    this.showReviewModal = false;
  }

  // Download ALL files as ZIP
  downloadAllAsZip(purchase: PurchaseHistoryResponse): void {
    if (this.downloadingZip[purchase.projectId]) return;
    this.downloadingZip[purchase.projectId] = true;

    this.buyerService.downloadProjectZip(purchase.projectId).subscribe({
      next: (blob: Blob) => {
        this.triggerDownload(blob, `${purchase.projectTitle}.zip`);
        this.downloadingZip[purchase.projectId] = false;
        this.downloadSuccess['zip_' + purchase.projectId] = true;
        setTimeout(() => delete this.downloadSuccess['zip_' + purchase.projectId], 3000);
      },
      error: () => {
        this.downloadingZip[purchase.projectId] = false;
        alert('Download failed. Please try again.');
      }
    });
  }

  // Helper: create blob URL and click it
  private triggerDownload(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }

}
