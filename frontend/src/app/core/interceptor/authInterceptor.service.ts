import {HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse} from "@angular/common/http";
import {inject, Injectable} from "@angular/core";
import {LocalstorageService} from "../services/localstorage.service";
import {catchError, switchMap, throwError, BehaviorSubject, filter, take} from "rxjs";
import {AuthService} from "../services/auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  private authService = inject(AuthService); // ✅ Inject in constructor alternative

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    // Skip token for public endpoints
    if (this.isPublicEndpoint(req.url)) {
      return next.handle(req);
    }

    const token = LocalstorageService.getToken();

    if (token) {
      req = this.addToken(req, token);
    }

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 && !this.isRefreshing) {
          return this.handle401Error(req, next);
        }

        return throwError(() => error);
      })
    );
  }

  private handle401Error(req: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      const refreshToken = LocalstorageService.getRefreshToken();

      if (!refreshToken) {
        this.isRefreshing = false;
        LocalstorageService.signOut();
        return throwError(() => new Error('No refresh token available'));
      }

      return this.authService.refreshToken().pipe(
        switchMap((res: any) => {
          this.isRefreshing = false;
          this.refreshTokenSubject.next(res.accessToken);

          LocalstorageService.saveToken(res.accessToken);
          LocalstorageService.saveRefreshToken(res.refreshToken);

          return next.handle(this.addToken(req, res.accessToken));
        }),
        catchError(err => {
          this.isRefreshing = false;
          LocalstorageService.signOut();
          // Optionally redirect to login
          // window.location.href = '/login';
          return throwError(() => err);
        })
      );
    } else {
      // Wait for the token refresh to complete
      return this.refreshTokenSubject.pipe(
        filter(token => token != null),
        take(1),
        switchMap(token => next.handle(this.addToken(req, token)))
      );
    }
  }

  private addToken(req: HttpRequest<any>, token: string) {
    return req.clone({
      setHeaders: {Authorization: `Bearer ${token}`}
    });
  }

  private isPublicEndpoint(url: string): boolean {
    const publicEndpoints = ['/api/register', '/api/login', '/api/refresh', '/api/languages', '/api/public'];
    return publicEndpoints.some(endpoint => url.includes(endpoint));
  }
}
