import {PurchasedFile} from "./PurchasedFile";

export interface PurchaseHistoryResponse {
  id: number;
  projectTitle: string;
  projectDescription: string;
  projectImgUrl: string;
  sellerUsername: string;
  paidAmount: number;
  status: string;
  purchasedAt: string;
  isRefunded: boolean;
  canDownload: boolean;
  files: PurchasedFile[];
  projectId:number
}
