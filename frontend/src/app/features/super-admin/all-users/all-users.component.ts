import {Component} from '@angular/core';
import {AdminService, PaginatedUsersResponse} from "../../../core/services/admin.service";
import {DashboardUserStats} from "../../../core/model/DashboardUserStats";

export interface UpdateStatusRequest {
  status: string;
  reason: string;
}

@Component({
  selector: 'app-all-users',
  templateUrl: './all-users.component.html',
  styleUrls: ['./all-users.component.css']
})
export class AllUsersComponent {

  stats!: DashboardUserStats;
  roleKeys: string[] = [];

  userList: any[] = [];
  page = 0;
  size = 10;
  totalPages = 0;

  // Modal state
  showStatusModal = false;
  selectedUser: any = null;
  statusForm = { status: '', reason: '' };
  availableStatuses: string[] = [];

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.loadUsers();
    this.loadUserStats();
  }

  loadUserStats() {
    this.adminService.getUsersStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.roleKeys = Object.keys(this.stats.roleCounts);
        console.log(this.roleKeys);
        console.log(JSON.stringify(this.stats));
      },
      error: (err) => console.error(err)
    });
  }

  loadUsers() {
    this.adminService.getAllUsers(this.page, this.size)
      .subscribe((res: PaginatedUsersResponse) => {
        this.userList = res.users;
        this.totalPages = res.totalPages;
      });
  }

  nextPage() {
    if (this.page + 1 < this.totalPages) { this.page++; this.loadUsers(); }
  }

  prevPage() {
    if (this.page > 0) { this.page--; this.loadUsers(); }
  }

  /**
   * Returns a custom CSS class — NOT Bootstrap classes.
   * These map to .status-active / .status-banned etc. in the CSS.
   */
  getStatusClass(status: string | undefined): string {
    switch (status) {
      case 'ACTIVE':    return 'status-active';
      case 'PENDING':   return 'status-pending';
      case 'SUSPENDED': return 'status-suspended';
      case 'INACTIVE':  return 'status-inactive';
      case 'BANNED':    return 'status-banned';
      default:          return 'status-inactive';
    }
  }

  openStatusModal(user: any) {
    this.selectedUser = user;
    this.statusForm = { status: '', reason: '' };
    this.availableStatuses = this.getAvailableStatuses(user.status);
    this.showStatusModal = true;
  }

  closeStatusModal() {
    this.showStatusModal = false;
    this.selectedUser = null;
    this.statusForm = { status: '', reason: '' };
  }

  getAvailableStatuses(currentStatus: string): string[] {
    const transitions: { [key: string]: string[] } = {
      'PENDING':   ['ACTIVE'],
      'ACTIVE':    ['SUSPENDED'],
      'SUSPENDED': ['ACTIVE'],
      'INACTIVE':  [],
      'BANNED':    []
    };
    return transitions[currentStatus] || [];
  }

  updateUserStatus() {
    if (!this.statusForm.status || !this.statusForm.reason.trim()) return;

    const request: UpdateStatusRequest = {
      status: this.statusForm.status,
      reason: this.statusForm.reason
    };

    this.adminService.updateUserStatus(this.selectedUser.id, request).subscribe({
      next: () => {
        this.closeStatusModal();
        this.loadUsers();
      },
      error: (err) => console.error('Error updating status:', err)
    });
  }
}
