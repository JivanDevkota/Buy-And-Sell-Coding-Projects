import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {LocalstorageService} from "../services/localstorage.service";

export const buyerGuard: CanActivateFn = (route, state) => {

  const router = inject(Router)
  if (LocalstorageService.getToken() && LocalstorageService.isBuyerLoggedIn()) {
    return true;
  }
  return router.createUrlTree(['/auth/signin']);
};
