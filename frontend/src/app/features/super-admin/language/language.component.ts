import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../../core/services/admin.service';
import {LanguageDTO} from "../../../core/model";


@Component({
  selector: 'app-language',
  templateUrl: './language.component.html',
  styleUrls: ['./language.component.css']
})
export class LanguageComponent implements OnInit {

  languageForm!: FormGroup;
  loading    = false;
  listLoading = false;

  successMessage: string | null = null;
  errorMessage:   string | null = null;

  languages: LanguageDTO[] = [];

  /* File upload state */
  selectedFile: File | null = null;
  previewUrl:   string | null = null;
  fileError:    string | null = null;
  isDragging    = false;

  /* Skeleton placeholders while loading */
  skeletons = Array(8);

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.languageForm = this.fb.group({
      name:        ['', [Validators.required, Validators.minLength(2)]],
      description: ['',  Validators.required],
    });

    this.loadAllLanguages();
  }

  /* ── Submit ── */
  onSubmit(): void {
    if (this.languageForm.invalid) {
      this.languageForm.markAllAsTouched();
      return;
    }
    if (!this.selectedFile) {
      this.fileError = 'Please select an icon image.';
      return;
    }

    this.loading        = true;
    this.successMessage = null;
    this.errorMessage   = null;

    const formData = new FormData();
    // Backend expects @RequestPart("language") LanguageDTO  and @RequestPart("file")
    formData.append('language', new Blob(
      [JSON.stringify(this.languageForm.value)],
      { type: 'application/json' }
    ));
    formData.append('file', this.selectedFile);

    this.adminService.createLanguage(formData).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = `Language "${this.languageForm.value.name}" added successfully!`;
        this.resetForm();
        this.loadAllLanguages();
        setTimeout(() => { this.successMessage = null; }, 4000);
      },
      error: (err) => {
        this.loading      = false;
        this.errorMessage = err?.error?.message || 'Failed to add language. Please try again.';
        setTimeout(() => { this.errorMessage = null; }, 5000);
        console.error(err);
      }
    });
  }

  /* ── Load list ── */
  loadAllLanguages(): void {
    this.listLoading = true;
    this.adminService.getAllLanguages().subscribe({
      next: (data: LanguageDTO[]) => {
        this.languages   = data;
        this.listLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.listLoading = false;
      }
    });
  }

  /* ── File handling ── */
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.handleFile(input.files[0]);
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.isDragging = true;
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragging = false;
    const file = event.dataTransfer?.files[0];
    if (file) this.handleFile(file);
  }

  handleFile(file: File): void {
    this.fileError = null;

    if (!file.type.startsWith('image/')) {
      this.fileError = 'Only image files are accepted.';
      return;
    }
    if (file.size > 2 * 1024 * 1024) {
      this.fileError = 'File must be smaller than 2 MB.';
      return;
    }

    this.selectedFile = file;

    const reader = new FileReader();
    reader.onload = (e) => { this.previewUrl = e.target?.result as string; };
    reader.readAsDataURL(file);
  }

  removeFile(event: MouseEvent): void {
    event.stopPropagation();
    this.selectedFile = null;
    this.previewUrl   = null;
    this.fileError    = null;
  }

  /* ── Reset ── */
  resetForm(): void {
    this.languageForm.reset();
    this.selectedFile = null;
    this.previewUrl   = null;
    this.fileError    = null;
  }

  /* ── Broken image fallback ── */
  onImgError(event: Event): void {
    (event.target as HTMLImageElement).src =
      'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="36" height="36" viewBox="0 0 24 24"%3E%3Cpath fill="%2394a3b8" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 15v-4H7l5-8v4h4l-5 8z"/%3E%3C/svg%3E';
  }
}
