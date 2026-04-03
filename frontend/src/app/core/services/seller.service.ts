import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ProjectDTO, ProjectListItem, ProjectFileDTO, DashboardStats, SellerDashboard } from '../model';
import { environment } from '../../../environments/environment';
import {ProjectStats} from "../model/ProjectStats";
import {ReviewStats} from "../model/ReviewStats";

const baseUrl = environment.apiUrl;

@Injectable({
  providedIn: 'root'
})
export class SellerService {

  constructor(private http: HttpClient) {
  }

  sellerCreateProduct(projectDto: ProjectDTO, file: File | null): Observable<ProjectDTO> {
    if (!file) {
      return throwError(() => new Error('Project image file is required'));
    }

    const formData = new FormData();
    formData.append(
      'project',
      new Blob([JSON.stringify(projectDto)], { type: 'application/json' })
    );
    formData.append('file', file);

    return this.http.post<ProjectDTO>(`${baseUrl}/seller/create/project`, formData)
      .pipe(catchError(this.handleError));
  }

  addProjectFile(projectFileDto: ProjectFileDTO, file: File | null, userId: number): Observable<ProjectFileDTO> {
    if (!file) {
      return throwError(() => new Error('File is required'));
    }

    const formData = new FormData();
    formData.append(
      'projectFile',
      new Blob([JSON.stringify(projectFileDto)], { type: 'application/json' })
    );
    formData.append('file', file);

    return this.http.post<ProjectFileDTO>(
      `${baseUrl}/seller/project/add-file?userId=${userId}`,
      formData
    ).pipe(catchError(this.handleError));
  }

  getMyAllProjects(sellerId: number | null): Observable<ProjectListItem[]> {
    return this.http.get<ProjectListItem[]>(`${baseUrl}/seller/${sellerId}/projects`);
  }

  getProjectDetails(projectId: number): Observable<any> {
    return this.http.get<any>(`${baseUrl}/seller/project/${projectId}`)
      .pipe(catchError(this.handleError));
  }

  submitProjectForReview(projectId: number): Observable<any> {
    return this.http.put<any>(`${baseUrl}/seller/project/${projectId}/submit-for-review`, {})
      .pipe(catchError(this.handleError));
  }

  withdrawProject(projectId: number): Observable<any> {
    return this.http.put<any>(`${baseUrl}/seller/project/${projectId}/withdraw`, {})
      .pipe(catchError(this.handleError));
  }

  getSalesStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${baseUrl}/seller/stats`);
  }

  getSellerDashboard(): Observable<SellerDashboard> {
    return this.http.get<SellerDashboard>(`${baseUrl}/seller/dashboard`);
  }

  getProjectStats():Observable<ProjectStats> {
    return this.http.get<ProjectStats>(`${baseUrl}/seller/stats/projects`);
  }


  getReviewStats(): Observable<ReviewStats> {
    return this.http.get<ReviewStats>(`${baseUrl}/seller/stats/reviews`);
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      if (error.error?.message) {
        errorMessage = error.error.message;
      } else if (error.error?.error) {
        errorMessage = error.error.error;
      } else {
        errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
      }
    }
    console.error('SellerService Error:', errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
