import { Injectable } from "@angular/core";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { RegisterRequest } from "../model/RegisterRequest";
import { catchError, map, Observable, of, throwError } from "rxjs";
import { LoginRequest } from "../model/LoginRequest";
import { LocalstorageService } from "./localstorage.service";
import { AuthStateService } from "./AuthStateService";
import { environment } from "../../../environments/environment";

const BASE_URL = environment.apiUrl;

/**
 * Authentication Service
 * Handles user registration, login, logout, and token refresh
 * Manages JWT tokens (access and refresh) for API authentication
 */
@Injectable({
  providedIn: 'root',
})
export class AuthService {

  constructor(
    private http: HttpClient,
    private authState: AuthStateService
  ) {}

  /**
   * Register new user
   *
   * @param registerRequest Registration data (username, email, password)
   * @returns Observable with registration response containing tokens
   */
  registerUser(registerRequest: RegisterRequest): Observable<any> {
    return this.http.post<any>(`${BASE_URL}/register`, registerRequest).pipe(
      map(response => {
        this.handleAuthResponse(response);
        console.log("✅ User registered successfully:", response.username);
        // Notify app about new auth state so components (nav, guards) update immediately
        this.authState.notify();
        return response;
      }),
      catchError(error => {
        console.error("❌ Registration failed:", error.error?.message || error.statusText);
        return throwError(() => error);
      })
    );
  }

  /**
   * Login user with credentials
   * Stores access token, refresh token, and user details
   *
   * @param loginRequest Login data (username, password)
   * @returns Observable<boolean> - true if login successful
   */
  loginUser(loginRequest: LoginRequest): Observable<boolean> {
    return this.http.post<any>(`${BASE_URL}/login`, loginRequest).pipe(
      map(response => {
        this.handleAuthResponse(response);
        console.log("✅ User logged in successfully:", response.username);
        this.authState.notify();
        return true;
      }),
      catchError(error => {
        console.error("❌ Login failed:", error.error?.message || error.statusText);
        return of(false);
      })
    );
  }

  /**
   * Refresh access token using refresh token
   * Called when access token expires (after 15 minutes)
   * Gets new access token while maintaining session
   *
   * @returns Observable with new access token
   */
  refreshToken(): Observable<any> {
    const refreshToken = LocalstorageService.getRefreshToken();

    if (!refreshToken) {
      console.error("❌ No refresh token available");
      return throwError(() => new Error("No refresh token available"));
    }

    return this.http.post<any>(`${BASE_URL}/refresh`, { refreshToken }).pipe(
      map(response => {
        console.log("✅ Token refreshed successfully");
        LocalstorageService.saveToken(response.accessToken);
        LocalstorageService.saveRefreshToken(response.refreshToken);
        return response;
      }),
      catchError(error => {
        console.error("❌ Token refresh failed:", error.error?.message || error.statusText);
        this.logout();
        return throwError(() => error);
      })
    );
  }

  /**
   * Logout user
   * Clears all stored tokens and user data
   * Notifies components of auth state change
   */
  logout(): void {
    LocalstorageService.signOut();
    this.authState.notify();
    console.log("✅ User logged out");
  }

  /**
   * Check if user is authenticated
   *
   * @returns true if valid access token exists
   */
  isAuthenticated(): boolean {
    return !!LocalstorageService.getToken();
  }

  /**
   * Get current logged-in user
   *
   * @returns User object or null
   */
  getCurrentUser() {
    return LocalstorageService.getUser();
  }

  /**
   * Handle authentication response
   * Saves tokens and user data to localStorage
   *
   * @param response Authentication response from backend
   */
  private handleAuthResponse(response: any): void {
    if (!response) return;

    LocalstorageService.saveToken(response.accessToken);
    LocalstorageService.saveRefreshToken(response.refreshToken);
    LocalstorageService.saveUser({
      userId: response.userId,
      username: response.username,
      email: response.email,
      roles: response.roles
    });
  }

}
