import { PurchaseStatus, PaymentType } from './enums';

export interface Purchase {
  id: number;
  buyerId: number;
  buyerUsername: string;
  projectId: number;
  projectTitle: string;
  projectImgUrl: string;
  paidAmount: number;
  paymentType: PaymentType;
  status: PurchaseStatus;
  transactionId: string;
  purchasedAt: string;
  isRefunded: boolean;
  refundedAt: string | null;
  refundReason: string | null;
}

export interface PurchaseRequest {
  projectId: number;
  paymentType: PaymentType;
}

export interface PurchaseHistoryResponse extends Purchase {
  projectDescription?: string;
  canDownload?: boolean;
  files?: PurchasedFile[];
}

export interface PurchasedFile {
  id: number;
  fileName: string;
  fileUrl: string;
  fileType: string;
  downloadedAt?: string;
}

export interface RefundRequest {
  reason: string;
}
