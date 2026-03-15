import { Injectable } from "@angular/core";

/**
 * User interface representing authenticated user data
 */
export interface User {
  userId: number;
  username: string;
  email: string;
  roles: string[];
}

/**
 * Local Storage Service
 * Manages JWT tokens and user data in browser localStorage
 * Provides role-based access control checks
 */
@Injectable({
  providedIn: 'root',
})
export class LocalstorageService {
  private static readonly TOKEN_KEY = 'accessToken';
  private static readonly REFRESH_TOKEN_KEY = 'refreshToken';
  private static readonly USER_KEY = 'currentUser';

  /**
   * Save access token to localStorage
   *
   * @param token JWT access token
   */
  static saveToken(token: string): void {
    if (token) {
      localStorage.setItem(this.TOKEN_KEY, token);
    }
  }

  /**
   * Get access token from localStorage
   *
   * @returns JWT access token or null
   */
  static getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Save refresh token to localStorage
   *
   * @param token JWT refresh token
   */
  static saveRefreshToken(token: string): void {
    if (token) {
      localStorage.setItem(this.REFRESH_TOKEN_KEY, token);
    }
  }

  /**
   * Get refresh token from localStorage
   *
   * @returns JWT refresh token or null
   */
  static getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  /**
   * Save user details to localStorage
   * Stores user info retrieved from authentication response
   *
   * @param user User object with userId, username, email, roles
   */
  static saveUser(user: User): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  /**
   * Get user details from localStorage
   * Safely parses JSON and handles errors
   *
   * @returns User object or null
   */
  static getUser(): User | null {
    const user = localStorage.getItem(this.USER_KEY);
    if (!user) return null;

    try {
      return JSON.parse(user);
    } catch (error) {
      console.error("❌ Failed to parse user data:", error);
      localStorage.removeItem(this.USER_KEY);
      return null;
    }
  }

  /**
   * Get current user ID
   *
   * @returns User ID or null
   */
  static getUserId(): number | null {
    return this.getUser()?.userId ?? null;
  }

  /**
   * Get current username
   *
   * @returns Username or null
   */
  static getUserName(): string | null {
    return this.getUser()?.username ?? null;
  }

  /**
   * Get current user roles
   *
   * @returns Array of role strings (e.g., ['ROLE_BUYER', 'ROLE_SELLER'])
   */
  static getRoles(): string[] {
    return this.getUser()?.roles ?? [];
  }

  /**
   * Check if user has specific role
   *
   * @param role Role to check (e.g., 'ROLE_BUYER')
   * @returns true if user has the role
   */
  static hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }

  /**
   * Check if access token exists
   *
   * @returns true if valid access token exists
   */
  static isTokenAvailable(): boolean {
    return !!this.getToken();
  }

  /**
   * Check if refresh token exists
   *
   * @returns true if valid refresh token exists
   */
  static isRefreshTokenAvailable(): boolean {
    return !!this.getRefreshToken();
  }

  /**
   * Check if user is logged in as SELLER
   *
   * @returns true if token exists and user has ROLE_SELLER
   */
  static isSellerLoggedIn(): boolean {
    return this.isTokenAvailable() && this.hasRole("ROLE_SELLER");
  }

  /**
   * Check if user is logged in as BUYER
   *
   * @returns true if token exists and user has ROLE_BUYER
   */
  static isBuyerLoggedIn(): boolean {
    return this.isTokenAvailable() && this.hasRole("ROLE_BUYER");
  }

  /**
   * Check if user is logged in as ADMIN
   *
   * @returns true if token exists and user has ROLE_ADMIN
   */
  static isSuperAdminLoggedIn(): boolean {
    return this.isTokenAvailable() && this.hasRole("ROLE_ADMIN");
  }

  /**
   * Check if user is authenticated
   *
   * @returns true if user has valid token and user data
   */
  static isAuthenticated(): boolean {
    return this.isTokenAvailable() && this.getUser() !== null;
  }

  /**
   * Sign out user
   * Clears all stored authentication data
   */
  static signOut(): void {
    localStorage.removeItem(this.USER_KEY);
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    console.log("✅ Cleared all authentication data");
  }

}
