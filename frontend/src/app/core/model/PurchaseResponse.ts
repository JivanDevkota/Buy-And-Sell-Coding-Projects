export interface PurchaseResponse {
  id: number;
  buyerId: number;
  buyerUsername: string;
  projectId: number;
  projectTitle: string;
  projectImgUrl: string;
  paidAmount: number;
  paymentType: string;
  status: 'PENDING' | 'COMPLETED' | 'FAILED' | 'REFUNDED';
  transactionId: string;
  purchasedAt: string;
  isRefunded: boolean;
  refundedAt: string | null;
  refundReason: string | null;
}
