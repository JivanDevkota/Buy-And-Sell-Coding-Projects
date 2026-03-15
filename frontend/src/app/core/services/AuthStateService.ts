import { BehaviorSubject, Observable } from "rxjs";
import { Injectable } from "@angular/core";

/**
 * Authentication State Service
 *
 * Manages and broadcasts authentication state changes across the application
 * Uses RxJS BehaviorSubject to emit events when:
 * - User logs in
 * - User logs out
 * - Token is refreshed
 *
 * Components subscribe to authState$ to stay in sync with auth changes
 * without requiring page reloads
 */
@Injectable({
  providedIn: 'root'
})
export class AuthStateService {
  private authStateSubject = new BehaviorSubject<void>(undefined);

  /**
   * Observable of authentication state changes
   * Components can subscribe to react to login/logout/refresh events
   */
  authState$: Observable<void> = this.authStateSubject.asObservable();

  /**
   * Notify subscribers of authentication state change
   * Called after:
   * - Successful login
   * - Successful registration
   * - Token refresh
   * - User logout
   */
  notify(): void {
    this.authStateSubject.next();
  }
}
