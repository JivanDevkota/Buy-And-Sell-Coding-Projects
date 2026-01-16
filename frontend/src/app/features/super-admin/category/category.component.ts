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

  categories: CategoryDTO[] = [];

  page = 0;
  size = 5;
  totalPages = 0;

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.categoryForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
    });

    this.loadAllCategory();
  }

  onSubmit() {
    if (this.categoryForm.invalid) {
      return;
    }

    this.loading = true;

    const category = this.categoryForm.value;

    this.adminService.createCategory(category).subscribe({
      next: () => {
        this.loading = false;
        this.categoryForm.reset();
        this.page = 0;               // go back to first page
        this.loadAllCategory();      // refresh list
      },
      error: (error) => {
        this.loading = false;
        console.error(error);
      }
    });
  }

  loadAllCategory() {
    this.adminService.getAllCategories(this.page, this.size).subscribe({
      next: (data: any) => {
        console.log(data);
        this.categories = data.categories || [];   // Spring page uses "content"
        this.totalPages = data.totalPages;
        this.page = data.currentPage;
      },
      error: (error) => {
        console.error(error);
      }
    });
  }

  nextPage() {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.loadAllCategory();
    }
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.loadAllCategory();
    }
  }
}
