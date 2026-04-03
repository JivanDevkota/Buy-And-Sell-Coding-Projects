import {Component, OnInit} from '@angular/core';
import {ProjectResponse} from "../../../core/model/ProjectResponse";
import {SellerService} from "../../../core/services/seller.service";
import {LocalstorageService} from "../../../core/services/localstorage.service";
import {ProjectStats} from "../../../core/model/ProjectStats";

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit {
  projects: ProjectResponse[] = []
  projectStats!: ProjectStats

  constructor(private sellerService: SellerService,) {
  }

  ngOnInit(): void {
    this.getAllMyProjects();
    this.loadAllProjectStats();
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

  loadAllProjectStats() {
    this.sellerService.getProjectStats().subscribe({
      next: (data: any) => {
        this.projectStats = data;
        console.log(data);
      },
      error: (error: any) => {
        console.log(error);
      }
    })
  }
}
