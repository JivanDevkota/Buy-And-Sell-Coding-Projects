import {Component} from '@angular/core';
import {LanguageResponse} from "../../core/model/languageResponse";
import {PublicService} from "../../core/services/Public.service";
import {PublicProjectResponse} from "../../core/model/PublicProjectResponse";
import {Router} from "@angular/router";
import {LocalstorageService} from "../../core/services/localstorage.service";
import {BuyerService} from "../../core/services/buyer.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  languages: LanguageResponse[] = []
  projects: PublicProjectResponse[] = [];
  isBuyerLoggedIn = false


  purchaseLoading: { [projectId: number]: boolean } = {};
  purchaseSuccess: string | null = null;
  purchaseError: string | null = null;

  constructor(private publicService: PublicService, private router: Router, private buyerService: BuyerService) {
  }

  ngOnInit() {
    this.isBuyerLoggedIn = LocalstorageService.isBuyerLoggedIn() as boolean;
    this.getAllProgrammingLanguages()
    this.getAllProjects();
  }

  getAllProgrammingLanguages(): void {
    this.publicService.getAllLanguages().subscribe(({
      next: (data) => {
        this.languages = data;
        console.log(this.languages);
      },
      error: (error) => {
        console.log("error", error);
      },
    }))
  }

  getAllProjects(): void {
    this.publicService.getAllProjects().subscribe({
      next: (data) => {
        console.log('PROJECT API RESPONSE:', data);
        this.projects = data;
      },
      error: (error) => {
        console.log("error", error);
      }
    });
  }

  purchaseProject(projectId: number) {
    if (!this.isBuyerLoggedIn) {
      this.router.navigate(['/auth/signin']);
      return;
    }
    this.purchaseSuccess = null;
    this.purchaseError = null;

    this.purchaseLoading[projectId] = true;

    this.buyerService.purchaseProject(
      Number(LocalstorageService.getUserId()),
      {
        projectId: projectId,
        paymentType: 'ESEWA'
      }
    ).subscribe({
      next: (response) => {
        this.purchaseLoading[projectId] = false;
        this.purchaseSuccess = `"${response.projectTitle}" purchased successfully! Transaction ID: ${response.transactionId}`;
        setTimeout(() => {
          this.purchaseSuccess = null, 4000;
        })
      },
      error: (error) => {
        this.purchaseLoading[projectId] = false;
        const message = error?.error?.message || 'Purchase failed. Please try again.';
        this.purchaseError = message;

        // Auto-clear error message after 4 seconds
        setTimeout(() => this.purchaseError = null, 4000);
      }
    })

  }

}
