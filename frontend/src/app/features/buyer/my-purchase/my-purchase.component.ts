import { Component } from '@angular/core';
import {BuyerService} from "../../../core/services/buyer.service";
import {LocalstorageService} from "../../../core/services/localstorage.service";
import {PurchaseHistoryResponse} from "../../../core/model/PurchaseHistoryResponse";
import {PurchasedFile} from "../../../core/model/PurchasedFile";

@Component({
  selector: 'app-my-purchase',
  templateUrl: './my-purchase.component.html',
  styleUrls: ['./my-purchase.component.css']
})
export class MyPurchaseComponent {
  purchases:PurchaseHistoryResponse[] = [];
  loading=true
  error: string |null = null;

  // Modal state
  showModal = false;
  selectedPurchase: PurchaseHistoryResponse | null = null;

  // Download state — track per file and per project
  downloadingZip: { [projectId: number]: boolean } = {};
  downloadingFile: { [fileId: number]: boolean } = {};
  downloadSuccess: { [key: string]: boolean } = {};

  constructor(
    private buyerService: BuyerService,
  ) {}

  ngOnInit(): void {
    this.myPurchases();
  }

  myPurchases(): void {
    this.buyerService.myPurchaseProjects(Number(LocalstorageService.getUserId()))
      .subscribe({
        next: (data: any) => {
          this.purchases = data;
          this.loading = false;
        },
        error: () => {
          this.error = 'Failed to load purchases. Please try again.';
          this.loading = false;
        }
      });
  }

  // Open modal
  openDownloadModal(purchase: PurchaseHistoryResponse): void {
    this.selectedPurchase = purchase;
    this.showModal = true;
    this.downloadSuccess = {};
  }

  closeModal(): void {
    this.showModal = false;
    this.selectedPurchase = null;
  }

  // Download ALL files as ZIP
  downloadAllAsZip(purchase: PurchaseHistoryResponse): void {
    if (this.downloadingZip[purchase.projectId]) return;
    this.downloadingZip[purchase.projectId] = true;

    this.buyerService.downloadProjectZip(purchase.projectId).subscribe({
      next: (blob: Blob) => {
        this.triggerDownload(blob, `${purchase.projectTitle}.zip`);
        this.downloadingZip[purchase.projectId] = false;
        this.downloadSuccess['zip_' + purchase.projectId] = true;
        setTimeout(() => delete this.downloadSuccess['zip_' + purchase.projectId], 3000);
      },
      error: () => {
        this.downloadingZip[purchase.projectId] = false;
        alert('Download failed. Please try again.');
      }
    });
  }

  // Download single file
  downloadFile(file: PurchasedFile): void {
    if (this.downloadingFile[file.fileId]) return;
    this.downloadingFile[file.fileId] = true;

    this.buyerService.downloadSingleFile(file.fileId).subscribe({
      next: (blob: Blob) => {
        this.triggerDownload(blob, file.fileName);
        this.downloadingFile[file.fileId] = false;
        this.downloadSuccess['file_' + file.fileId] = true;
        setTimeout(() => delete this.downloadSuccess['file_' + file.fileId], 3000);
      },
      error: () => {
        this.downloadingFile[file.fileId] = false;
        alert('File download failed. Please try again.');
      }
    });
  }

  // Helper: create blob URL and click it
  private triggerDownload(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }

  getFileIcon(fileName: string): string {
    const ext = fileName.split('.').pop()?.toLowerCase();
    const icons: { [key: string]: string } = {
      zip: 'fas fa-file-archive', rar: 'fas fa-file-archive',
      pdf: 'fas fa-file-pdf', java: 'fas fa-file-code',
      jar: 'fas fa-cube', war: 'fas fa-cube',
      py: 'fas fa-file-code', js: 'fas fa-file-code',
      ts: 'fas fa-file-code', sql: 'fas fa-database',
      mp4: 'fas fa-file-video', docx: 'fas fa-file-word',
    };
    return icons[ext || ''] || 'fas fa-file';
  }

  getFileIconColor(fileName: string): string {
    const ext = fileName.split('.').pop()?.toLowerCase();
    const colors: { [key: string]: string } = {
      zip: '#F59E0B', rar: '#F59E0B', pdf: '#EF4444',
      java: '#3B82F6', jar: '#8B5CF6', war: '#8B5CF6',
      py: '#10B981', js: '#F59E0B', ts: '#3B82F6',
      sql: '#06B6D4', mp4: '#EC4899',
    };
    return colors[ext || ''] || '#6B7280';
  }

  getStatusClass(status: string): string {
    const map: { [key: string]: string } = {
      COMPLETED: 'status-completed',
      PENDING: 'status-pending',
      REFUNDED: 'status-refunded',
      FAILED: 'status-failed'
    };
    return map[status] || 'status-pending';
  }
}
