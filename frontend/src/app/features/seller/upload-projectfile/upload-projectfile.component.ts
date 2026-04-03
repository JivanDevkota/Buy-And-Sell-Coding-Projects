import { Component } from '@angular/core';
import {FileType, ProjectFileDTO} from "../../../core/model";
import {ActivatedRoute, Router} from "@angular/router";
import { SellerService } from 'src/app/core/services/seller.service';
import {LocalstorageService} from "../../../core/services/localstorage.service";

@Component({
  selector: 'app-upload-projectfile',
  templateUrl: './upload-projectfile.component.html',
  styleUrls: ['./upload-projectfile.component.css']
})
export class UploadProjectfileComponent {
  projectId: number = 0;
  selectedFile: File | null = null;
  selectedFileName: string = '';
  isLoading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  // Make FileType enum available in template
  fileTypes = Object.values(FileType);

  // Form model
  projectFileForm: ProjectFileDTO = {
    projectId: 0,
    fileType: FileType.MEDIA,
    description: '',
    displayOrder: 0
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private sellerService: SellerService
  ) {}

  ngOnInit(): void {
    // Get project ID from route parameters
    this.route.params.subscribe(params => {
      this.projectId = +params['id'];
      this.projectFileForm.projectId = this.projectId;
    });
  }

  /**
   * Handles file selection
   */
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      // Validate file size (10MB max)
      const maxSize = 10 * 1024 * 1024; // 10MB in bytes
      if (file.size > maxSize) {
        this.errorMessage = 'File size must not exceed 10MB';
        this.selectedFile = null;
        this.selectedFileName = '';
        return;
      }

      this.selectedFile = file;
      this.selectedFileName = file.name;
      this.errorMessage = '';
    }
  }

  /**
   * Validates the form before submission
   */
  validateForm(): boolean {
    this.errorMessage = '';

    if (!this.selectedFile) {
      this.errorMessage = 'Please select a file';
      return false;
    }

    if (!this.projectFileForm.fileType) {
      this.errorMessage = 'Please select a file type';
      return false;
    }

    if (this.projectFileForm.description && this.projectFileForm.description.length > 500) {
      this.errorMessage = 'Description must not exceed 500 characters';
      return false;
    }

    return true;
  }

  /**
   * Submits the project file form
   */
  submitProjectFile(): void {
    if (!this.validateForm()) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const userId = LocalstorageService.getUserId();

    if (!userId) {
      this.errorMessage = 'User not authenticated';
      this.isLoading = false;
      return;
    }

    this.sellerService.addProjectFile(this.projectFileForm, this.selectedFile, userId).subscribe({
      next: (response: ProjectFileDTO) => {
        console.log('Project file added successfully:', response);
        this.successMessage = 'File added successfully!';
        this.isLoading = false;
        this.resetForm();

        // Optional: Navigate back to projects list after 2 seconds
        setTimeout(() => {
          this.router.navigate(['/seller/projects']);
        }, 2000);
      },
      error: (error: any) => {
        console.error('Error adding project file:', error);
        this.errorMessage = error.message || 'Failed to add file. Please try again.';
        this.isLoading = false;
      }
    });
  }

  /**
   * Resets the form to initial state
   */
  resetForm(): void {
    this.projectFileForm = {
      projectId: this.projectId,
      fileType: FileType.MEDIA,
      description: '',
      displayOrder: 0
    };
    this.selectedFile = null;
    this.selectedFileName = '';
  }

  /**
   * Navigates back to projects list
   */
  goBack(): void {
    this.router.navigate(['/seller/projects']);
  }
}
