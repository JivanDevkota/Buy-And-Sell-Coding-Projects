import { Component, OnInit } from '@angular/core';
import { AdminService } from "../../../core/services/admin.service";
import {
  PendingApprovalsSummary,
  PendingSellerApprovals
} from "../../../core/model";
import { PendingProjects } from "../../../core/model/PendingProjects";

@Component({
  selector: 'app-pending-approvals',
  templateUrl: './pending-approvals.component.html',
  styleUrls: ['./pending-approvals.component.css']
})
export class PendingApprovalsComponent implements OnInit {

  // ── Data ──────────────────────────────────────────────
  pendingSellerApprovals: PendingSellerApprovals[] = [];
  pendingSummary: PendingApprovalsSummary | null = null;

  projects: PendingProjects[] = [];
  page      = 0;
  size      = 10;
  totalPages = 0;

  // Hardcoded until backend provides it
  approvedToday = 0;

  // ── Loading / error state ─────────────────────────────
  isLoadingSummary = false;
  isLoadingSellers = false;
  loading          = false;

  error: string | null = null;
  errorMessage         = '';

  // Per-row action loading so only the clicked row shows a spinner
  actionLoading: { [key: string]: boolean } = {};
  projectActionLoading: { [projectId: number]: boolean } = {};

  // ── Toast feedback (replaces alert()) ─────────────────
  toastMessage: string | null = null;
  toastType: 'success' | 'error' = 'success';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadSummary();
    this.loadAllPendingSellerApprovals();
    this.loadProjects();
  }

  // ── Summary ───────────────────────────────────────────
  loadSummary(): void {
    this.isLoadingSummary = true;
    this.adminService.getPendingApprovals().subscribe({
      next: (data) => {
        this.pendingSummary   = data;
        this.isLoadingSummary = false;
      },
      error: (err) => {
        console.error('Error loading summary:', err);
        this.errorMessage     = 'Failed to load summary data.';
        this.isLoadingSummary = false;
      }
    });
  }

  // ── Pending Sellers ───────────────────────────────────
  loadAllPendingSellerApprovals(): void {
    this.isLoadingSellers = true;
    this.adminService.getPendingSellerApprovals().subscribe({
      next: (data) => {
        this.pendingSellerApprovals = data;
        this.isLoadingSellers       = false;
      },
      error: (err) => {
        console.error('Error loading seller approvals:', err);
        this.errorMessage     = 'Failed to load pending sellers.';
        this.isLoadingSellers = false;
      }
    });
  }

  // approveSeller(seller: PendingSellerApprovals): void {
  //   this.actionLoading[seller.username] = true;
  //   this.adminService.approveSeller(seller.id).subscribe({
  //     next: () => {
  //       this.actionLoading[seller.username] = false;
  //       this.approvedToday++;
  //       this.showToast(`Seller "${seller.username}" approved successfully`, 'success');
  //       // Remove from local list instantly so UI feels fast
  //       this.pendingSellerApprovals = this.pendingSellerApprovals
  //         .filter(s => s.id !== seller.id);
  //       this.loadSummary();
  //     },
  //     error: (err) => {
  //       this.actionLoading[seller.username] = false;
  //       this.showToast(`Failed to approve seller "${seller.username}"`, 'error');
  //       console.error(err);
  //     }
  //   });
  // }
  //
  // rejectSeller(seller: PendingSellerApprovals): void {
  //   this.actionLoading[seller.username] = true;
  //   this.adminService.rejectSeller(seller.id).subscribe({
  //     next: () => {
  //       this.actionLoading[seller.username] = false;
  //       this.showToast(`Seller "${seller.username}" rejected`, 'success');
  //       this.pendingSellerApprovals = this.pendingSellerApprovals
  //         .filter(s => s.id !== seller.id);
  //       this.loadSummary();
  //     },
  //     error: (err) => {
  //       this.actionLoading[seller.username] = false;
  //       this.showToast(`Failed to reject seller "${seller.username}"`, 'error');
  //       console.error(err);
  //     }
  //   });
  // }

  // ── Pending Projects ──────────────────────────────────
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
      error: (err) => {
        this.error   = 'Failed to load pending projects. Please try again.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  approve(projectId: number): void {
    this.projectActionLoading[projectId] = true;
    this.adminService.approveProject(projectId).subscribe({
      next: () => {
        this.projectActionLoading[projectId] = false;
        this.approvedToday++;
        this.showToast('Project approved successfully', 'success');
        // Remove instantly from list, then reload for pagination accuracy
        this.projects = this.projects.filter(p => p.projectId !== projectId);
        this.loadProjects();
        this.loadSummary();
      },
      error: (err) => {
        this.projectActionLoading[projectId] = false;
        this.showToast('Failed to approve project', 'error');
        console.error(err);
      }
    });
  }

  reject(projectId: number): void {
    this.projectActionLoading[projectId] = true;
    this.adminService.rejectProject(projectId).subscribe({
      next: () => {
        this.projectActionLoading[projectId] = false;
        this.showToast('Project rejected', 'success');
        this.projects = this.projects.filter(p => p.projectId !== projectId);
        this.loadProjects();
        this.loadSummary();
      },
      error: (err) => {
        this.projectActionLoading[projectId] = false;
        this.showToast('Failed to reject project', 'error');
        console.error(err);
      }
    });
  }

  nextPage(): void {
    if (this.page + 1 < this.totalPages) { this.page++; this.loadProjects(); }
  }

  prevPage(): void {
    if (this.page > 0) { this.page--; this.loadProjects(); }
  }

  // ── Computed getters ──────────────────────────────────
  get pendingSellers(): number {
    return this.pendingSummary?.['PENDING SELLERS'] ?? 0;
  }

  get pendingProjects(): number {
    return this.pendingSummary?.['PENDING PROJECTS'] ?? 0;
  }

  get total(): number {
    return this.pendingSellers + this.pendingProjects;
  }

  // ── Toast helper ──────────────────────────────────────
  showToast(message: string, type: 'success' | 'error'): void {
    this.toastMessage = message;
    this.toastType    = type;
    setTimeout(() => { this.toastMessage = null; }, 4500);
  }
}
