import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { PurchaseRequest, Purchase, PurchaseHistoryResponse, WishlistItem, BuyerStats } from "../model";
import { LocalstorageService } from "./localstorage.service";
import { environment } from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class BuyerService {

  private baseUrl = environment.apiUrl + '/buyer';

  constructor(private http: HttpClient) {
  }

  purchaseProject(buyerId: number, request: PurchaseRequest): Observable<Purchase> {
    return this.http.post<Purchase>(`${this.baseUrl}/${buyerId}/purchases`, request);
  }

  myPurchaseProjects(buyerId: number): Observable<PurchaseHistoryResponse[]> {
    return this.http.get<PurchaseHistoryResponse[]>(`${this.baseUrl}/${buyerId}/my-purchases`);
  }

  getBuyerDashStats(): Observable<BuyerStats> {
    const userIdRaw = LocalstorageService.getUserId();
    const userId = userIdRaw !== null ? Number(userIdRaw) : null;
    if (!userId) {
      return new Observable<BuyerStats>((subscriber) => {
        subscriber.next({ lifetimeSpend: 0, purchasedCount: 0, wishlistCount: 0 });
        subscriber.complete();
      });
    }
    return this.http.get<BuyerStats>(`${this.baseUrl}/${userId}/stats`);
  }

  addToWishlist(buyerId: number, projectId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/wishlist/add/${buyerId}/project/${projectId}`, {});
  }

  getAllMyWishlist(buyerId: number): Observable<WishlistItem[]> {
    return this.http.get<WishlistItem[]>(`${this.baseUrl}/wishlist/${buyerId}/all`);
  }

  deleteMyWishlist(projectId: number): Observable<any> {
    const buyerId = Number(LocalstorageService.getUserId());
    return this.http.delete(`${this.baseUrl}/${buyerId}/wishlist/${projectId}`);
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

  addReview(projectId: number, rating: number, comment: string): Observable<any> {
    const buyerId = Number(LocalstorageService.getUserId());
    if (!buyerId) {
      return new Observable(subscriber => {
        subscriber.error(new Error('User not authenticated'));
      });
    }
    return this.http.post(`${this.baseUrl}/reviews/project/${projectId}/user/${buyerId}`, {
      rating,
      comment
    });
  }

  getProjectReviews(): Observable<any[]> {
    const userId = Number(LocalstorageService.getUserId());
    return this.http.get<any[]>(`${this.baseUrl}/reviews/project/${userId}`);
  }
}
