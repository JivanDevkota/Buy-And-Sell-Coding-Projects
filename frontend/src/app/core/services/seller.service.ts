import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {ProjectDTO} from '../model/ProjectDTO';
import {ProjectFileDTO} from '../model/ProjectFileDTO';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {ProjectResponse} from "../model/ProjectResponse";

const baseUrl = 'http://localhost:8080/api';

@Injectable({
  providedIn: 'root'
})
export class SellerService {

  constructor(private http: HttpClient) {
  }

  /**
   * Creates a new project
   * @param projectDto the project data
   * @param file the project image file
   * @returns Observable of created project
   */
  sellerCreateProduct(projectDto: ProjectDTO, file: File | null): Observable<ProjectDTO> {
    if (!file) {
      return throwError(() => new Error('Project image file is required'));
    }

    const formData = new FormData();

    // Append project data as JSON blob
    formData.append(
      'project',
      new Blob([JSON.stringify(projectDto)], {type: 'application/json'})
    );

    // Append file
    formData.append('file', file);

    return this.http.post<ProjectDTO>(`${baseUrl}/seller/create/project`, formData)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Adds a file to an existing project
   * @param projectFileDto the file metadata
   * @param file the file to upload
   * @param userId the user ID
   * @returns Observable of created project file
   */
  addProjectFile(projectFileDto: ProjectFileDTO, file: File | null, userId: number): Observable<ProjectFileDTO> {
    if (!file) {
      return throwError(() => new Error('File is required'));
    }

    const formData = new FormData();

    // Append project file data as JSON blob
    formData.append(
      'projectFile',
      new Blob([JSON.stringify(projectFileDto)], {type: 'application/json'})
    );

    // Append file
    formData.append('file', file);

    return this.http.post<ProjectFileDTO>(
      `${baseUrl}/seller/project/add-file?userId=${userId}`,
      formData
    ).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Get all projects for a seller
   * @param sellerId
   */
  getMyAllProjects(sellerId: number | null): Observable<ProjectResponse[]> {
    return this.http.get<ProjectResponse[]>(`${baseUrl}/seller/${sellerId}/projects`);
  }

  /**
   * Get project details with files
   * @param projectId
   */
  getProjectDetails(projectId: number): Observable<any> {
    return this.http.get<any>(`${baseUrl}/seller/project/${projectId}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Submit project for admin review
   */
  submitProjectForReview(projectId: number): Observable<any> {
    return this.http.put<any>(`${baseUrl}/seller/project/${projectId}/submit-for-review`, {})
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Withdraw project from review
   */
  withdrawProject(projectId: number): Observable<any> {
    return this.http.put<any>(`${baseUrl}/seller/project/${projectId}/withdraw`, {})
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Handles HTTP errors
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An error occurred';

    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
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
