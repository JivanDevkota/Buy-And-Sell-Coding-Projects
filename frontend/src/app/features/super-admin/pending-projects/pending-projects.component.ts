import { Component, OnInit } from '@angular/core';
import { PendingProjects } from '../../../core/model/PendingProjects';
import { AdminService } from '../../../core/services/admin.service';

@Component({
  selector: 'app-pending-projects',
  templateUrl: './pending-projects.component.html',
  styleUrls: ['./pending-projects.component.css']
})
export class PendingProjectsComponent implements OnInit {

  projects:   PendingProjects[] = [];
  page       = 0;
  size       = 10;
  totalPages = 0;
  loading    = false;

  error:          string | null = null;
  successMessage: string | null = null;

  // Per-row loading map so each button shows its own state
  actionLoading: { [projectId: number]: boolean } = {};

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.loading = true;
    this.error   = null;

    this.adminService.getPendingProjects(this.page, this.size).subscribe({
      next: (res) => {
        this.projects    = res.content;
        this.page        = res.page;
        this.size        = res.size;
        this.totalPages  = res.totalPages;
        this.loading     = false;
      },
      error: () => {
        this.error   = 'Failed to load projects. Please try again.';
        this.loading = false;
      }
    });
  }

  approve(projectId: number): void {
    this.actionLoading[projectId] = true;
    this.adminService.approveProject(projectId).subscribe({
      next: () => {
        this.actionLoading[projectId] = false;
        this.showSuccess('Project approved successfully.');
        this.loadProjects();
      },
      error: () => {
        this.actionLoading[projectId] = false;
        this.showError('Failed to approve project.');
      }
    });
  }

  reject(projectId: number): void {
    this.actionLoading[projectId] = true;
    this.adminService.rejectProject(projectId).subscribe({
      next: () => {
        this.actionLoading[projectId] = false;
        this.showSuccess('Project rejected.');
        this.loadProjects();
      },
      error: () => {
        this.actionLoading[projectId] = false;
        this.showError('Failed to reject project.');
      }
    });
  }

  nextPage(): void {
    if (this.page + 1 < this.totalPages) { this.page++; this.loadProjects(); }
  }

  prevPage(): void {
    if (this.page > 0) { this.page--; this.loadProjects(); }
  }

  getInitials(name: string): string {
    if (!name) return '?';
    return name.split(' ').slice(0, 2).map(n => n[0]).join('').toUpperCase();
  }

  private showSuccess(msg: string): void {
    this.successMessage = msg;
    this.error          = null;
    setTimeout(() => { this.successMessage = null; }, 4000);
  }

  private showError(msg: string): void {
    this.error          = msg;
    this.successMessage = null;
    setTimeout(() => { this.error = null; }, 5000);
  }
}
