import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {UserDetailsResponse} from "../model/UserDetailsResponse";
import {Observable} from "rxjs";
import {CategoryDTO} from "../model/CategoryDTO";
import {PaginatedPendingProjectsResponse, PendingProjects} from "../model/PendingProjects";
import {DashboardUserStats} from "../model/DashboardUserStats";
import {PagedResponse, SellerSummaryDTO} from "../model/Seller";
import {BuyerSummaryDTO} from "../model/BuyerSummaryDTO";
import { environment } from "../../../environments/environment";
import {LanguageDTO, PendingApprovalsSummary, PendingSellerApprovals} from "../model";


export interface PaginatedUsersResponse {
  users: UserDetailsResponse[];
  currentPage: number;
  totalPages: number;
  totalItems: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

export interface PaginatedCategoriesResponse {
  categories: CategoryDTO[];
  currentPage: number;
  totalPages: number;
  totalItems: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

export interface UpdateStatusRequest {
  status: string;
  reason: string;
}

@Injectable(
  {providedIn: 'root'}
)

export class AdminService {
  private readonly adminUrl = environment.apiUrl;

  constructor(private http: HttpClient) {

  }

  getAllUsers(page: number = 0, size: number = 5): Observable<PaginatedUsersResponse> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<PaginatedUsersResponse>(`${this.adminUrl}/admin/recent/users`, {
      params
    });
  }

  /**
   * Update user status (Admin only)
   */
  updateUserStatus(userId: number, request: UpdateStatusRequest): Observable<void> {
    return this.http.patch<void>(`${this.adminUrl}/admin/${userId}/status`, request);
  }

  /**
   * Get user status history (optional - for future use)
   */
  getUserStatusHistory(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.adminUrl}/admin/${userId}/status-history`);
  }

  createCategory(categoryDto: CategoryDTO): Observable<CategoryDTO> {
    return this.http.post<CategoryDTO>(`${this.adminUrl}/admin/add/category`, categoryDto);
  }

  getAllCategory(): Observable<CategoryDTO[]> {
    return this.http.get<CategoryDTO[]>(`${this.adminUrl}/admin/recent/categories`);
  }

  getAllCategories(page: number = 0, size: number = 5): Observable<PaginatedCategoriesResponse> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);
    return this.http.get<PaginatedCategoriesResponse>(`${this.adminUrl}/admin/recent/categories`, {params})
  }

  getPendingProjects(page: number, size: number):
    Observable<PaginatedPendingProjectsResponse> {

    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PaginatedPendingProjectsResponse>(
      `${this.adminUrl}/admin/pending-projects`,
      {params}
    );
  }

  approveProject(projectId: number) {
    return this.http.put(`${this.adminUrl}/admin/project/${projectId}/approve`, {});
  }

  rejectProject(projectId: number) {
    return this.http.put(`${this.adminUrl}/admin/project/${projectId}/suspend`, {});
  }

  getUsersStats(): Observable<DashboardUserStats> {
    return this.http.get<DashboardUserStats>(`${this.adminUrl}/admin/users/stats`);
  }

  getSellers(
    page: number,
    size: number,
    status?: string
  ): Observable<PagedResponse<SellerSummaryDTO>> {

    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (status && status !== 'ALL') {
      params = params.set('status', status);
    }

    return this.http.get<PagedResponse<SellerSummaryDTO>>(
      `${this.adminUrl}/admin/sellers`,
      { params }
    );
  }

  getBuyer():Observable<BuyerSummaryDTO[]>{
    return this.http.get<BuyerSummaryDTO[]>(`${this.adminUrl}/admin/top-buyers`);
  }

  getPendingApprovals():Observable<PendingApprovalsSummary>{
    return this.http.get<PendingApprovalsSummary>(`${this.adminUrl}/admin/pending-approvals`);
  }

  getPendingSellerApprovals():Observable<PendingSellerApprovals[]>{
    return this.http.get<PendingSellerApprovals[]>(`${this.adminUrl}/admin/pending-sellers-approvals`);
  }


  getAllLanguages(): Observable<LanguageDTO[]> {
    return this.http.get<LanguageDTO[]>(`${this.adminUrl}/admin/languages`);
  }


  createLanguage(formData: FormData): Observable<LanguageDTO> {
    return this.http.post<LanguageDTO>(`${this.adminUrl}/admin/create/language`, formData);
    // Do NOT set Content-Type manually — HttpClient sets multipart boundary automatically
  }

}

