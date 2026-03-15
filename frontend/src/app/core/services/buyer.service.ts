import {Injectable} from "@angular/core";
import {PurchaseRequest} from "../model/PurchaseRequest";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {PurchaseResponse} from "../model/PurchaseResponse";
import {PurchaseHistoryResponse} from "../model/PurchaseHistoryResponse";
import {LocalstorageService} from "./localstorage.service";
import {BuyerStats} from "../model/buyer-stats";
import {WishlistResponse} from "../model/WishlistResponse";


@Injectable(
  {providedIn: 'root'}
)
export class BuyerService{

  private baseUrl='http://localhost:8080/api/buyer';

  constructor(private http:HttpClient) {
  }

  purchaseProject(buyerId:number,request:PurchaseRequest): Observable<PurchaseResponse> {
    return this.http.post<PurchaseResponse>(`${this.baseUrl}/${buyerId}/purchases`,request);
  }

  myPurchaseProjects(buyerId:number): Observable<PurchaseHistoryResponse>{
    return this.http
      .get<PurchaseHistoryResponse>(`${this.baseUrl}/${buyerId}/my-purchases`)
  }

  //dash stats
  getBuyerDashStats() :Observable<BuyerStats>{
    const userId = Number(LocalstorageService.getUserId());
    return this.http.get<BuyerStats>(`${this.baseUrl}/${userId}/stats`);
  }

  addToWishlist(buyerId:number,projectId:number):Observable<any> {
    return this.http.post(`${this.baseUrl}/reviews/project/${projectId}/user/${buyerId}`, {});
  }

  getAllMyWishlist(projectId:number): Observable<WishlistResponse[]> {
    return this.http.get<WishlistResponse[]>(`${this.baseUrl}/reviews/project/{projectId}`);

  }

  downloadProjectZip(projectId: number): Observable<Blob> {
    const userId = Number(LocalstorageService.getUserId());
    return this.http.get(`${this.baseUrl}/download/project/${projectId}?userId=${userId}`, {
      responseType: 'blob'
    });
  }

  downloadSingleFile(fileId: number): Observable<Blob> {
    const userId = Number(LocalstorageService.getUserId());
    return this.http.get(`${this.baseUrl}/download/file/${fileId}?userId=${userId}`, {
      responseType: 'blob'
    });
  }
}
