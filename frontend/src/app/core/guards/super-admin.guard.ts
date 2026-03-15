import { CanActivateFn, Router } from '@angular/router';
import { inject } from "@angular/core";
import { LocalstorageService } from "../services/localstorage.service";

/**
 * Super Admin Guard
 * Protects routes that require ADMIN role
 * Redirects to login if user is not authenticated as admin
 */
export const superAdminGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  const hasValidToken = LocalstorageService.getToken();
  const isAdmin = LocalstorageService.isSuperAdminLoggedIn();

  if (hasValidToken && isAdmin) {
    console.log("✅ Admin guard: Access granted to", state.url);
    return true;
  }

  console.warn("⚠️  Admin guard: Access denied - redirecting to login");
  return router.createUrlTree(['/auth/signin']);
};
