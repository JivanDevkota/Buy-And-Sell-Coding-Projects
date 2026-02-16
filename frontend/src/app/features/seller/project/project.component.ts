import { Component } from '@angular/core';
import {ProjectDetailsResponse, ProjectStatus} from "../../../core/model/ProjectDetailsResponse";
import {ActivatedRoute, Router} from "@angular/router";
import {SellerService} from "../../../core/services/seller.service";

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent {

  projectId: number = 0;
  project: ProjectDetailsResponse | null = null;
  isLoading: boolean = true;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private sellerService: SellerService
  ) {}

  ngOnInit(): void {
    // Get project ID from route
    this.route.params.subscribe(params => {
      this.projectId = +params['id'];
      this.loadProjectDetails();
    });
  }

  /**
   * Loads project details including files
   */
  loadProjectDetails(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.sellerService.getProjectDetails(this.projectId).subscribe({
      next: (data: ProjectDetailsResponse) => {
        this.project = data;
        // Sort files by display order
        if (this.project.files) {
          this.project.files.sort((a, b) =>
            (a.displayOrder || 0) - (b.displayOrder || 0)
          );
        }
        this.isLoading = false;
        console.log('Project details loaded:', this.project);
      },
      error: (error: any) => {
        console.error('Error loading project details:', error);
        this.errorMessage = 'Failed to load project details. Please try again.';
        this.isLoading = false;
      }
    });
  }

  /**
   * Gets file icon based on file type
   */
  getFileIcon(fileType: string): string {
    const icons: {[key: string]: string} = {
      'SOURCE_CODE': 'bi-file-earmark-code',
      'EXECUTABLE': 'bi-file-earmark-binary',
      'DOCUMENTATION': 'bi-file-earmark-text',
      'DATABASE': 'bi-database',
      'CONFIGURATION': 'bi-gear',
      'MEDIA': 'bi-file-earmark-image',
      'OTHER': 'bi-file-earmark'
    };
    return icons[fileType] || 'bi-file-earmark';
  }

  /**
   * Gets file type badge color
   */
  getFileTypeBadgeClass(fileType: string): string {
    const classes: {[key: string]: string} = {
      'SOURCE_CODE': 'bg-primary',
      'EXECUTABLE': 'bg-danger',
      'DOCUMENTATION': 'bg-info',
      'DATABASE': 'bg-warning',
      'CONFIGURATION': 'bg-secondary',
      'MEDIA': 'bg-success',
      'OTHER': 'bg-dark'
    };
    return classes[fileType] || 'bg-secondary';
  }

  /**
   * Downloads a file
   */
  downloadFile(fileUrl: string, fileName: string): void {
    const link = document.createElement('a');
    link.href = 'http://localhost:8080' + fileUrl;
    link.download = fileName;
    link.target = '_blank';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  /**
   * Navigates to add file page
   */
  addFile(): void {
    this.router.navigate(['/seller/project', this.projectId, 'add-file']);
  }

  /**
   * Navigates back to projects list
   */
  goBack(): void {
    this.router.navigate(['/seller/projects']);
  }

  /**
   * Submit project for admin review
   */
  submitForReview(): void {
    if (!confirm('Are you sure you want to submit this project for admin review?')) {
      return;
    }

    this.sellerService.submitProjectForReview(this.projectId).subscribe({
      next: () => {
        alert('Project submitted for review successfully!');
        this.loadProjectDetails();
      },
      error: (error: any) => {
        alert('Failed to submit project: ' + error.message);
      }
    });
  }

  /**
   * Withdraw project from review
   */
  withdrawFromReview(): void {
    if (!confirm('Are you sure you want to withdraw this project from review?')) {
      return;
    }

    this.sellerService.withdrawProject(this.projectId).subscribe({
      next: () => {
        alert('Project withdrawn from review!');
        this.loadProjectDetails();
      },
      error: (error: any) => {
        alert('Failed to withdraw project: ' + error.message);
      }
    });
  }


  /**
   * Checks if file is an image
   */
  isImageFile(fileName: string): boolean {
    const imageExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.webp', '.svg'];
    return imageExtensions.some(ext => fileName.toLowerCase().endsWith(ext));
  }

  protected readonly ProjectStatus = ProjectStatus;
}
