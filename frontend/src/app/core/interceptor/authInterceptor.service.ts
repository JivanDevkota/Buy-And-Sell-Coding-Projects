import { HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { LocalstorageService } from "../services/localstorage.service";
import { catchError, switchMap, throwError, BehaviorSubject, filter, take } from "rxjs";
import { AuthService } from "../services/auth.service";

/**
 * HTTP Interceptor for JWT Authentication
 *
 * Responsibilities:
 * 1. Add JWT access token to all outgoing requests
 * 2. Handle 401 responses by refreshing the access token
 * 3. Retry failed requests with new token
 * 4. Skip token for public endpoints
 * 5. Queue concurrent requests during token refresh
 */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  private authService = inject(AuthService);

  /**
   * Intercept HTTP requests and responses
   *
   * @param req HttpRequest to intercept
   * @param next HttpHandler for the next interceptor
   * @returns Observable of HttpEvent
   */
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    // Skip token addition for public endpoints
    if (this.isPublicEndpoint(req.url)) {
      console.debug("⏭️  Skipping auth for public endpoint:", req.url);
      return next.handle(req);
    }

    const token = LocalstorageService.getToken();

    // Add token if available
    if (token) {
      req = this.addToken(req, token);
    }

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        // Handle 401 Unauthorized (token expired or invalid)
        if (error.status === 401 && !this.isRefreshing) {
          console.warn("⚠️  401 Unauthorized - Attempting token refresh");
          return this.handle401Error(req, next);
        }

        // For other errors, continue with error response
        return throwError(() => error);
      })
    );
  }

  /**
   * Handle 401 Unauthorized response
   * Refreshes the access token and retries the failed request
   *
   * @param req Original failed request
   * @param next HttpHandler
   * @returns Observable with retried request or error
   */
  private handle401Error(req: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      const refreshToken = LocalstorageService.getRefreshToken();

      if (!refreshToken) {
        console.error("❌ No refresh token available - logging out");
        this.isRefreshing = false;
        LocalstorageService.signOut();
        return throwError(() => new Error('No refresh token available'));
      }

      return this.authService.refreshToken().pipe(
        switchMap((res: any) => {
          console.log("✅ Token refreshed successfully");
          this.isRefreshing = false;
          this.refreshTokenSubject.next(res.accessToken);

          // Update tokens in storage
          LocalstorageService.saveToken(res.accessToken);
          LocalstorageService.saveRefreshToken(res.refreshToken);

          // Retry original request with new token
          return next.handle(this.addToken(req, res.accessToken));
        }),
        catchError((err) => {
          console.error("❌ Token refresh failed - logging out");
          this.isRefreshing = false;
          LocalstorageService.signOut();
          return throwError(() => err);
        })
      );
    } else {
      // If refresh is already in progress, wait for it to complete
      // Then retry the request with the new token
      console.debug("⏳ Token refresh in progress - queueing request");
      return this.refreshTokenSubject.pipe(
        filter(token => token != null),
        take(1),
        switchMap((token) => {
          console.log("✅ Retrying request with new token");
          return next.handle(this.addToken(req, token));
        })
      );
    }
  }

  /**
   * Add JWT token to request Authorization header
   *
   * @param req HttpRequest
   * @param token JWT access token
   * @returns HttpRequest with Authorization header set
   */
  private addToken(req: HttpRequest<any>, token: string): HttpRequest<any> {
    return req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  /**
   * Check if URL is a public endpoint that doesn't require authentication
   *
   * @param url Request URL
   * @returns true if URL is public endpoint
   */
  private isPublicEndpoint(url: string): boolean {
    const publicEndpoints = [
      '/api/register',
      '/api/login',
      '/api/refresh',
      '/api/languages',
      '/api/projects',
      '/api/categories',
      '/api/public',
      '/img/',
      '/uploads/'
    ];
    return publicEndpoints.some(endpoint => url.includes(endpoint));
  }
}
