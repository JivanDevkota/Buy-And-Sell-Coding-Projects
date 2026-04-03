import { Component, OnInit, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { LanguageDTO, PublicProjectListItem } from "../../core/model";
import { PublicService } from "../../core/services/Public.service";
import { Router } from "@angular/router";
import { LocalstorageService } from "../../core/services/localstorage.service";
import { BuyerService } from "../../core/services/buyer.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, AfterViewInit {

  languages: LanguageDTO[] = [];
  projects: PublicProjectListItem[] = [];
  isBuyerLoggedIn = false;

  purchaseLoading: { [projectId: number]: boolean } = {};
  purchaseSuccess: string | null = null;
  purchaseError: string | null = null;

  canScrollLeft  = false;
  canScrollRight = true;

  @ViewChild('carousel') carouselRef!: ElementRef<HTMLElement>;

  constructor(
    private publicService: PublicService,
    private router: Router,
    private buyerService: BuyerService
  ) {}

  ngOnInit(): void {
    this.isBuyerLoggedIn = LocalstorageService.isBuyerLoggedIn() as boolean;
    this.getAllProgrammingLanguages();
    this.getAllProjects();
  }

  ngAfterViewInit(): void {
    // Update button states once projects are rendered
    // Small delay lets Angular finish rendering *ngFor items
    setTimeout(() => this.updateScrollState(), 300);
  }

  getAllProgrammingLanguages(): void {
    this.publicService.getAllLanguages().subscribe({
      next:  (data)  => { this.languages = data; },
      error: (error) => { console.error('Languages error:', error); }
    });
  }

  getAllProjects(): void {
    this.publicService.getAllProjects().subscribe({
      next:  (data)  => {
        this.projects = data;
        // Give *ngFor time to render cards, then re-check scroll state
        setTimeout(() => this.updateScrollState(), 200);
      },
      error: (error) => { console.error('Projects error:', error); }
    });
  }

  // ── Carousel ─────────────────────────────────────────────────────
  private get carousel(): HTMLElement {
    return this.carouselRef?.nativeElement;
  }

  /**
   * Scroll by exactly one card width (260px card + 16px gap = 276px).
   * Using a named constant makes it easy to adjust if card width changes.
   */
  scrollCarousel(direction: 'left' | 'right'): void {
    const CARD_STEP = 276; // card width (260) + gap (16)
    this.carousel.scrollBy({
      left: direction === 'right' ? CARD_STEP : -CARD_STEP,
      behavior: 'smooth'
    });
    setTimeout(() => this.updateScrollState(), 400);
  }

  updateScrollState(): void {
    if (!this.carousel) return;
    const el = this.carousel;
    this.canScrollLeft  = el.scrollLeft > 2;
    this.canScrollRight = el.scrollLeft + el.clientWidth < el.scrollWidth - 2;
  }

  // ── Purchase ─────────────────────────────────────────────────────
  purchaseProject(projectId: number): void {
    if (!this.isBuyerLoggedIn) {
      this.router.navigate(['/auth/signin']);
      return;
    }

    this.purchaseSuccess = null;
    this.purchaseError   = null;
    this.purchaseLoading[projectId] = true;

    this.buyerService.purchaseProject(
      Number(LocalstorageService.getUserId()),
      { projectId, paymentType: 'ESEWA' as any }
    ).subscribe({
      next: (response) => {
        this.purchaseLoading[projectId] = false;
        this.purchaseSuccess = `"${response.projectTitle}" purchased! Transaction: ${response.transactionId}`;
        setTimeout(() => { this.purchaseSuccess = null; }, 4000);
      },
      error: (error) => {
        this.purchaseLoading[projectId] = false;
        this.purchaseError = error?.error?.message || 'Purchase failed. Please try again.';
        setTimeout(() => { this.purchaseError = null; }, 4000);
      }
    });
  }
}
