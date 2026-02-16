import {Component, OnInit} from '@angular/core';
import {PublicService} from '../../../core/services/Public.service';
import {CategoryDTO} from '../../../core/model/CategoryDTO';
import {SellerService} from '../../../core/services/seller.service';
import {ProjectDTO} from '../../../core/model/ProjectDTO';
import {LanguageResponse} from "../../../core/model/languageResponse";
import {LocalstorageService} from "../../../core/services/localstorage.service";

;

@Component({
  selector: 'app-upload-project',
  templateUrl: './upload-project.component.html',
  styleUrls: ['./upload-project.component.css']
})
export class UploadProjectComponent implements OnInit {

  categories: CategoryDTO[] = [];
  languages: LanguageResponse[] = [];
  selectedFile: File | null = null;
  selectedFileName: string = '';
  isLoading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  // Form model matching backend expectations
  projectForm: ProjectDTO = {
    title: '',
    description: '',
    tags: [],
    price: 1,
    languageIds: [],  // Changed from languages to languageIds
    categoryId: 0,
    userId: 0
  };

  // Temporary tag input
  tagInput: string = '';

  constructor(
    private publicService: PublicService,
    private sellerService: SellerService
  ) {
  }

  ngOnInit(): void {
    this.loadData();
  }

  /**
   * Loads all required data for the form
   */
  private loadData(): void {
    this.getAllLanguage();
    this.getAllCategory();
    // In production, get userId from authentication service
    // this.projectForm.userId = this.authService.getCurrentUserId();
    this.projectForm.userId = LocalstorageService.getUserId(); // Hardcoded for testing - REMOVE IN PRODUCTION
  }

  /**
   * Handles file selection
   */
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      // Validate file type
      const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
      if (!allowedTypes.includes(file.type)) {
        this.errorMessage = 'Please select a valid image file (JPEG, PNG, GIF, WEBP)';
        this.selectedFile = null;
        this.selectedFileName = '';
        return;
      }

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
   * Fetches all categories
   */
  getAllCategory(): void {
    this.publicService.getAllCategory().subscribe({
      next: (data: CategoryDTO[]) => {
        this.categories = data;
        console.log('Categories loaded:', this.categories);
      },
      error: (error: any) => {
        console.error('Error loading categories:', error);
        this.errorMessage = 'Failed to load categories';
      }
    });
  }

  /**
   * Fetches all languages
   */
  getAllLanguage(): void {
    this.publicService.getAllLanguages().subscribe({
      next: (data: LanguageResponse[]) => {
        this.languages = data;
        console.log('Languages loaded:', this.languages);
      },
      error: (error: any) => {
        console.error('Error loading languages:', error);
        this.errorMessage = 'Failed to load languages';
      }
    });
  }

  /**
   * Adds a tag to the project
   */
  addTag(): void {
    const tag = this.tagInput.trim();
    if (tag && !this.projectForm.tags.includes(tag)) {
      this.projectForm.tags.push(tag);
      this.tagInput = '';
    }
  }

  /**
   * Removes a tag from the project
   */
  removeTag(index: number): void {
    this.projectForm.tags.splice(index, 1);
  }

  /**
   * Toggles language selection
   */
  toggleLanguage(languageId: number): void {
    const index = this.projectForm.languageIds.indexOf(languageId);
    if (index > -1) {
      this.projectForm.languageIds.splice(index, 1);
    } else {
      this.projectForm.languageIds.push(languageId);
    }
  }

  /**
   * Checks if a language is selected
   */
  isLanguageSelected(languageId: number): boolean {
    return this.projectForm.languageIds.includes(languageId);
  }

  /**
   * Validates the form before submission
   */
  validateForm(): boolean {
    this.errorMessage = '';

    if (!this.projectForm.title || this.projectForm.title.trim().length < 3) {
      this.errorMessage = 'Title must be at least 3 characters long';
      return false;
    }

    if (!this.projectForm.description || this.projectForm.description.trim().length < 10) {
      this.errorMessage = 'Description must be at least 10 characters long';
      return false;
    }

    if (this.projectForm.tags.length === 0) {
      this.errorMessage = 'Please add at least one tag';
      return false;
    }

    if (!this.projectForm.price || this.projectForm.price <= 0) {
      this.errorMessage = 'Price must be greater than 0';
      return false;
    }

    if (this.projectForm.languageIds.length === 0) {
      this.errorMessage = 'Please select at least one language';
      return false;
    }

    if (!this.projectForm.categoryId || this.projectForm.categoryId === 0) {
      this.errorMessage = 'Please select a category';
      return false;
    }

    if (!this.selectedFile) {
      this.errorMessage = 'Please select a project image';
      return false;
    }

    return true;
  }

  /**
   * Submits the project form
   */
  submitProject(): void {
    if (!this.validateForm()) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.sellerService.sellerCreateProduct(this.projectForm, this.selectedFile).subscribe({
      next: (response: ProjectDTO) => {
        console.log('Project created successfully:', response);
        this.successMessage = 'Project created successfully!';
        this.isLoading = false;
        this.resetForm();
      },
      error: (error: any) => {
        console.error('Error creating project:', error);
        this.errorMessage = error.message || 'Failed to create project. Please try again.';
        this.isLoading = false;
      }
    });
  }

  /**
   * Resets the form to initial state
   */
  resetForm(): void {
    this.projectForm = {
      title: '',
      description: '',
      tags: [],
      price: 1,
      languageIds: [],
      categoryId: 0,
      userId: this.projectForm.userId // Preserve userId
    };
    this.selectedFile = null;
    this.selectedFileName = '';
    this.tagInput = '';
  }
}
