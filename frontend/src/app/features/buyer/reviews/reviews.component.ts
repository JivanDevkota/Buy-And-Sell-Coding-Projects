import {Component, OnInit} from '@angular/core';
import {Review} from "../../../core/model/ReviewResponse";
import {BuyerService} from "../../../core/services/buyer.service";

@Component({
  selector: 'app-reviews',
  templateUrl: './reviews.component.html',
  styleUrls: ['./reviews.component.css']
})
export class ReviewsComponent implements OnInit {


  reviews: Review[] = [];
  selectedProject: string = 'All Projects';


  constructor(
    private buyerService: BuyerService,
  ) {}


  loadReviews() {
    this.buyerService.getProjectReviews().subscribe(data => {
      this.reviews = data;
    });
  }

  ngOnInit() {
    this.loadReviews();
  }
}
