import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BuyerService } from '../../core/services/buyer.service';

/**
 * Review Modal Component
 * Displays a modal for users to add reviews to purchased projects
 * Handles rating and comment submission with validation
 */
@Component({
  selector: 'app-review-modal',
  templateUrl: './review-modal.component.html',
  styleUrls: ['./review-modal.component.css']
})
export class ReviewModalComponent {
  @Input() projectId!: number;
  @Input() projectTitle: string = '';
  @Input() isVisible: boolean = false;

  @Output() close = new EventEmitter<void>();
  @Output() reviewSubmitted = new EventEmitter<any>();

  rating: number = 0;
  comment: string = '';
  hoveredRating: number = 0;
  isSubmitting: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private buyerService: BuyerService) {}

  /**
   * Set rating when user clicks on a star
   */
  setRating(value: number): void {
    this.rating = value;
    this.errorMessage = '';
  }

  /**
   * Show visual feedback on star hover
   */
  onStarHover(value: number): void {
    this.hoveredRating = value;
  }

  /**
   * Clear hover state when mouse leaves
   */
  onStarLeave(): void {
    this.hoveredRating = 0;
  }

  /**
   * Submit review with validation
   */
  submitReview(): void {
    // Validation
    if (this.rating === 0) {
      this.errorMessage = 'Please select a rating';
      return;
    }

    if (!this.comment.trim()) {
      this.errorMessage = 'Please write a comment';
      return;
    }

    if (this.comment.trim().length < 10) {
      this.errorMessage = 'Comment must be at least 10 characters long';
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.buyerService.addReview(this.projectId, this.rating, this.comment).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        this.successMessage = 'Review submitted successfully!';
        console.log('✅ Review added:', response);

        // Emit event and close after a short delay
        setTimeout(() => {
          this.reviewSubmitted.emit(response);
          this.resetForm();
          this.closeModal();
        }, 1500);
      },
      error: (error) => {
        this.isSubmitting = false;
        console.error('❌ Failed to submit review:', error);
        this.errorMessage = error.error?.message || 'Failed to submit review. Please try again.';
      }
    });
  }

  /**
   * Close the modal
   */
  closeModal(): void {
    this.resetForm();
    this.close.emit();
  }

  /**
   * Reset form fields
   */
  private resetForm(): void {
    this.rating = 0;
    this.comment = '';
    this.errorMessage = '';
    this.successMessage = '';
    this.hoveredRating = 0;
  }

  /**
   * Check if star should be filled (for display)
   */
  isStarFilled(starIndex: number): boolean {
    return starIndex <= (this.hoveredRating || this.rating);
  }
}

