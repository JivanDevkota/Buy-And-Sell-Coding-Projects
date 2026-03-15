import { CanActivateFn, Router } from '@angular/router';
import { inject } from "@angular/core";
import { LocalstorageService } from "../services/localstorage.service";

/**
 * No Auth Guard
 * Protects public routes (login, register, etc.)
 * Redirects authenticated users away from auth pages to their dashboard
 */
export const noauthGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  // Allow access if not authenticated
  if (!LocalstorageService.getToken()) {
    console.log("✅ NoAuth guard: User not logged in - allow access to", state.url);
    return true;
  }

  // Redirect authenticated users to their respective area
  console.warn("⚠️  NoAuth guard: User already logged in - redirecting to dashboard");

  if (LocalstorageService.isSellerLoggedIn()) {
    console.log("→ Redirecting seller to dashboard");
    return router.createUrlTree(['/seller/data']);
  }

  if (LocalstorageService.isBuyerLoggedIn()) {
    console.log("→ Redirecting buyer to home");
    return router.createUrlTree(['/home']);
  }

  if (LocalstorageService.isSuperAdminLoggedIn()) {
    console.log("→ Redirecting admin to dashboard");
    return router.createUrlTree(['/admin/data']);
  }

  // Default: allow if no role matched (shouldn't happen)
  return true;
};
