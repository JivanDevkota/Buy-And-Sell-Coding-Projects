import { CanActivateFn, Router } from '@angular/router';
import { inject } from "@angular/core";
import { LocalstorageService } from "../services/localstorage.service";

/**
 * Buyer Guard
 * Protects routes that require BUYER role
 * Redirects to login if user is not authenticated as buyer
 */
export const buyerGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  const hasValidToken = LocalstorageService.getToken();
  const isBuyer = LocalstorageService.isBuyerLoggedIn();

  if (hasValidToken && isBuyer) {
    console.log("✅ Buyer guard: Access granted to", state.url);
    return true;
  }

  console.warn("⚠️  Buyer guard: Access denied - redirecting to login");
  return router.createUrlTree(['/auth/signin']);
};
