import {Component, OnInit} from '@angular/core';
import {AdminService, PaginatedUsersResponse} from "../../../core/services/admin.service";

export interface UpdateStatusRequest {
  status: string;
  reason: string;
}

@Component({
  selector: 'app-dashboard-data',
  templateUrl: './dashboard-data.component.html',
  styleUrls: ['./dashboard-data.component.css']
})
export class DashboardDataComponent implements OnInit {

  userList: any[] = [];
  page = 0;
  size = 5;
  totalPages = 0;

  // Modal state
  showStatusModal = false;
  selectedUser: any = null;
  statusForm = {
    status: '',
    reason: ''
  };

  // Available status options based on current status
  availableStatuses: string[] = [];

  constructor(private adminService: AdminService) {
  }

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.adminService.getAllUsers(this.page, this.size)
      .subscribe((res: PaginatedUsersResponse) => {
        this.userList = res.users;
        console.log(this.userList);
        this.totalPages = res.totalPages;
      });
  }

  nextPage() {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.loadUsers();
    }
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.loadUsers();
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDING':
        return 'bg-warning';
      case 'ACTIVE':
        return 'bg-success';
      case 'SUSPENDED':
        return 'bg-danger';
      case 'INACTIVE':
        return 'bg-secondary';
      case 'BANNED':
        return 'bg-dark';
      default:
        return 'bg-secondary';
    }
  }

  // Open status update modal
  openStatusModal(user: any) {
    this.selectedUser = user;
    this.statusForm = {
      status: '',
      reason: ''
    };
    this.availableStatuses = this.getAvailableStatuses(user.status);
    this.showStatusModal = true;
  }

  // Close modal
  closeStatusModal() {
    this.showStatusModal = false;
    this.selectedUser = null;
    this.statusForm = {
      status: '',
      reason: ''
    };
  }

  // Get available status transitions based on current status
  getAvailableStatuses(currentStatus: string): string[] {
    const transitions: { [key: string]: string[] } = {
      'PENDING': ['ACTIVE'],
      'ACTIVE': ['SUSPENDED'],
      'SUSPENDED': ['ACTIVE'],
      'INACTIVE': [],
      'BANNED': []
    };
    return transitions[currentStatus] || [];
  }

  // Submit status update
  updateUserStatus() {
    if (!this.statusForm.status || !this.statusForm.reason.trim()) {
      alert('Please select a status and provide a reason');
      return;
    }

    const request: UpdateStatusRequest = {
      status: this.statusForm.status,
      reason: this.statusForm.reason
    };

    this.adminService.updateUserStatus(this.selectedUser.id, request)
      .subscribe({
        next: () => {
          alert('User status updated successfully!');
          console.log(this.selectedUser.userId)
          this.closeStatusModal();
          this.loadUsers(); // Reload the list
        },
        error: (err) => {
          console.error('Error updating status:', err);
          alert('Failed to update status: ' + (err.error?.message || err.message));
        }
      });
  }
}
