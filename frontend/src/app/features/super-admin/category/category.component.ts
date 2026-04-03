import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../../core/services/admin.service';
import { CategoryDTO } from '../../../core/model/CategoryDTO';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {

  categoryForm!: FormGroup;
  loading = false;

  // Inline feedback messages (replaces alert())
  successMessage: string | null = null;
  errorMessage:   string | null = null;

  categories: CategoryDTO[] = [];
  page      = 0;
  size      = 8;
  totalPages = 0;

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.categoryForm = this.fb.group({
      name:        ['', [Validators.required, Validators.minLength(2)]],
      description: ['',  Validators.required],
    });

    this.loadAllCategory();
  }

  onSubmit(): void {
    if (this.categoryForm.invalid) {
      this.categoryForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.successMessage = null;
    this.errorMessage   = null;

    this.adminService.createCategory(this.categoryForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = `Category "${this.categoryForm.value.name}" created successfully!`;
        this.categoryForm.reset();
        this.page = 0;
        this.loadAllCategory();
        setTimeout(() => { this.successMessage = null; }, 4000);
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error?.error?.message || 'Failed to create category. Please try again.';
        setTimeout(() => { this.errorMessage = null; }, 5000);
        console.error(error);
      }
    });
  }

  loadAllCategory(): void {
    this.adminService.getAllCategories(this.page, this.size).subscribe({
      next: (data: any) => {
        this.categories = data.categories || [];
        this.totalPages = data.totalPages;
        this.page       = data.currentPage ?? this.page;
      },
      error: (error) => { console.error(error); }
    });
  }

  nextPage(): void {
    if (this.page + 1 < this.totalPages) { this.page++; this.loadAllCategory(); }
  }

  prevPage(): void {
    if (this.page > 0) { this.page--; this.loadAllCategory(); }
  }
}
