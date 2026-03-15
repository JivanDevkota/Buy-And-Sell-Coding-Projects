import {Component, OnInit} from '@angular/core';
import {PendingProjects} from "../../../core/model/PendingProjects";
import {AdminService} from "../../../core/services/admin.service";

@Component({
  selector: 'app-pending-projects',
  templateUrl: './pending-projects.component.html',
  styleUrls: ['./pending-projects.component.css']
})
export class PendingProjectsComponent implements OnInit {

  projects: PendingProjects[] = [];
  page: number = 0;
  size: number = 10;
  totalPages: number = 0;

  loading: boolean = false;
  error: string | null = null;

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects() {
    this.loading = true;
    this.adminService.getPendingProjects(this.page, this.size).subscribe({
      next: res => {
        this.projects = res.content;
        this.page = res.page;
        this.size = res.size;
        this.totalPages = res.totalPages;
        this.loading = false;
      },
      error: err => {
        this.error = 'Failed to load projects';
        this.loading = false;
      }
    });
  }

  approve(projectId: number) {
    this.adminService.approveProject(projectId).subscribe({
      next: () => this.loadProjects(),
      error: err => alert('Error approving project')
    });
  }

  reject(projectId: number) {
    this.adminService.rejectProject(projectId).subscribe({
      next: () => this.loadProjects(),
      error: err => alert('Error rejecting project')
    });
  }

  nextPage() {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.loadProjects();
    }
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.loadProjects();
    }
  }
}
