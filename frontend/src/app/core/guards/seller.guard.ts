import { CanActivateFn, Router } from '@angular/router';
import { inject } from "@angular/core";
import { LocalstorageService } from "../services/localstorage.service";

/**
 * Seller Guard
 * Protects routes that require SELLER role
 * Redirects to login if user is not authenticated as seller
 */
export const sellerGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  const hasValidToken = LocalstorageService.getToken();
  const isSeller = LocalstorageService.isSellerLoggedIn();

  if (hasValidToken && isSeller) {
    console.log("✅ Seller guard: Access granted to", state.url);
    return true;
  }

  console.warn("⚠️  Seller guard: Access denied - redirecting to login");
  return router.createUrlTree(['/auth/signin']);
};
