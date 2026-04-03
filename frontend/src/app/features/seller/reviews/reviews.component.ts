import {Component, OnInit} from '@angular/core';
import {ReviewStats} from "../../../core/model/ReviewStats";
import {SellerService} from "../../../core/services/seller.service";


interface Review {
  id: number;
  name: string;
  initials: string;
  avatarColor: string;
  product: string;
  timeAgo: string;
  rating: number;
  comment: string;
  response: string;
  showResponseBox: boolean;
}

@Component({
  selector: 'app-reviews',
  templateUrl: './reviews.component.html',
  styleUrls: ['./reviews.component.css']
})
export class ReviewsComponent implements OnInit {

  stats!: ReviewStats;

  constructor(private sellerService: SellerService) {
  }

  ngOnInit(): void {
    this.loadReviewStats();
  }

  loadReviewStats() {
    this.sellerService.getReviewStats().subscribe({
      next: (data) => this.stats = data,
      error: (err) => console.error(err)
    });
  }

  reviews: Review[] = [
    {
      id: 1,
      name: 'Alex Johnson',
      initials: 'AJ',
      avatarColor: '#3b82f6',
      product: 'Purchased E-Commerce React App',
      timeAgo: '2 days ago',
      rating: 5,
      comment: 'Amazing project! The code is well-organized and documentation is thorough. Saved me weeks of development time. Highly recommended!',
      response: '',
      showResponseBox: true,
    },
    {
      id: 2,
      name: 'Sarah Miller',
      initials: 'SM',
      avatarColor: '#8b5cf6',
      product: 'Purchased Flight Booking System',
      timeAgo: '5 days ago',
      rating: 4,
      comment: 'Great project overall. The Spring Boot backend is solid. Would love to see more comments in the code, but otherwise excellent work!',
      response: '',
      showResponseBox: false,
    },
    {
      id: 3,
      name: 'David Chen',
      initials: 'DC',
      avatarColor: '#16a34a',
      product: 'Purchased Quiz Platform',
      timeAgo: '1 week ago',
      rating: 5,
      comment: 'Exactly what I needed. Clean UI, great structure. Would buy again!',
      response: '',
      showResponseBox: false,
    },
  ];

  getStars(rating: number): number[] {
    return Array.from({length: 5}, (_, i) => i + 1);
  }

  toggleResponseBox(review: Review): void {
    review.showResponseBox = !review.showResponseBox;
  }

  postResponse(review: Review): void {
    if (review.response.trim()) {
      alert(`Response posted for ${review.name}:\n"${review.response}"`);
      review.showResponseBox = false;
    }
  }

}
