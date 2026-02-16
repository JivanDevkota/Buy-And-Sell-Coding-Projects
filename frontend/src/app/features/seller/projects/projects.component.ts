import {Component, OnInit} from '@angular/core';
import {ProjectResponse, ProjectStatus} from "../../../core/model/ProjectResponse";
import {SellerService} from "../../../core/services/seller.service";
import {LocalstorageService} from "../../../core/services/localstorage.service";

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit {
  projects: ProjectResponse[] = []
  projectStatus=ProjectStatus

  constructor(private sellerService: SellerService,) {
  }

  ngOnInit(): void {
    this.getAllMyProjects();
  }

  getAllMyProjects() {
    const sellerId = LocalstorageService.getUserId();
    this.sellerService.getMyAllProjects(sellerId).subscribe({
      next: (data: any) => {
        console.log(data);
        this.projects = data;
      },
      error: (error: any) => {
        console.log(error);
      }
    })
  }

  protected readonly ProjectStatus = ProjectStatus;
}
