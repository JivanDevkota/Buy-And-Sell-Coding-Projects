import {Component} from '@angular/core';
import {PublicService} from "../../core/services/Public.service";
import {PublicProjectDetailsResponse} from "../../core/model/PublicProjectDetailsReponse";
import {ActivatedRoute, Router} from "@angular/router";
import {BuyerService} from "../../core/services/buyer.service";
import {LocalstorageService} from "../../core/services/localstorage.service";

@Component({
  selector: 'app-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.css']
})
export class ProjectDetailsComponent {
  projectDetails!: PublicProjectDetailsResponse;
  loading = true;

  constructor(
    private publicService: PublicService,
    private router: Router,
    private route: ActivatedRoute,
    private buyerService:BuyerService

  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      const projectId = +params['id'];
      if (projectId) {
        this.getProjectsDetailsById(projectId);
      } else {
        console.error('No project ID found in route');
        this.loading = false;
      }
    });
  }

  getProjectsDetailsById(projectId: number): void {
    this.publicService.getProjectDetailsById(projectId).subscribe({
      next: (data: PublicProjectDetailsResponse) => {
        this.projectDetails = data;
        this.loading = false;
      },
      error: (error: any) => {
        console.log("Error fetching project details:", error);
        this.loading = false;
      }
    });
  }

  // Method to get colorful language badge classes
  getLanguageBadgeClass(index: number): string {
    const colors = [
      'bg-primary text-white',
      'bg-info text-dark',
      'bg-success text-white',
      'bg-warning text-dark',
      'bg-danger text-white',
      'bg-purple text-white',
      'bg-teal text-white',
      'bg-orange text-white'
    ];
    return colors[index % colors.length];
  }

  // Method to get colorful tag badge classes
  getTagBadgeClass(index: number): string {
    const colors = [
      'bg-secondary text-white',
      'bg-dark text-white',
      'bg-light text-dark border',
      'bg-blue text-white',
      'bg-indigo text-white',
      'bg-pink text-white',
      'bg-cyan text-dark',
      'bg-gray text-white'
    ];
    return colors[index % colors.length];
  }

  addProjectToWishlist(projectId:number): void {
    const userId=Number(LocalstorageService.getUserId())
    if (!LocalstorageService.isBuyerLoggedIn()) {
      this.router.navigate(['/auth/signin']);
      return;
    }
    this.buyerService.addToWishlist(userId,projectId).subscribe({
      next: (response) => {
        console.log('Project added to wishlist successfully:', response);
        alert('Project added to wishlist successfully!');
      },
      error: (error) => {
        console.error('Error adding project to wishlist:', error);
        alert('Failed to add project to wishlist. Please try again.');
      }
    });
  }
}
