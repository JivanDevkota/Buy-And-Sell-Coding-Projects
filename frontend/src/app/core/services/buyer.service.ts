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

  myPurchaseProjects(buyerId:number): Observable<PurchaseHistoryResponse[]>{
    return this.http
      .get<PurchaseHistoryResponse[]>(`${this.baseUrl}/${buyerId}/my-purchases`)
  }

  //dash stats
  getBuyerDashStats() :Observable<BuyerStats>{
    const userIdRaw = LocalstorageService.getUserId();
    const userId = userIdRaw !== null ? Number(userIdRaw) : null;
    if (!userId) {
      // Return default empty stats observable to prevent frontend errors when user not set
      return new Observable<BuyerStats>((subscriber) => {
        subscriber.next({ lifetimeSpend: 0, purchasedCount: 0, wishlistCount: 0 });
        subscriber.complete();
      });
    }

    return this.http.get<BuyerStats>(`${this.baseUrl}/${userId}/stats`);
  }

  addToWishlist(buyerId:number,projectId:number):Observable<any> {
    return this.http.post(`${this.baseUrl}/wishlist/add/${buyerId}/project/${projectId}`, {});
  }

  getAllMyWishlist(buyerId:number): Observable<WishlistResponse[]> {
    return this.http.get<WishlistResponse[]>(`${this.baseUrl}/wishlist/${buyerId}/all`);

  }

  deleteMyWishlist(projectId:number): Observable<any> {
    const buyerId=Number(LocalstorageService.getUserId());
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

  // Review operations
  /**
   * Add a review for a purchased project
   * @param projectId the project to review
   * @param rating rating score 1-5
   * @param comment review comment
   * @returns Observable of review response
   */
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

  /**
   * Get all reviews for a project
   * @param projectId the project ID
   * @returns Observable of reviews list
   */
  getProjectReviews(): Observable<any[]> {
    const userId=Number(LocalstorageService.getUserId());
    return this.http.get<any[]>(`${this.baseUrl}/reviews/project/${userId}`);
  }

}
