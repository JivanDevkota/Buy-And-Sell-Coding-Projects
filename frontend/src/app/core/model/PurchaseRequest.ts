export interface PurchaseRequest {
  projectId: number;
  paymentType: 'STRIPE' | 'PAYPAL' | 'ESEWA'
}
