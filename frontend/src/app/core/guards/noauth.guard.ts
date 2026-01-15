import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {LocalstorageService} from "../services/localstorage.service";

export const noauthGuard: CanActivateFn = (route, state) => {

  const router=inject(Router)
  if (LocalstorageService.getToken()){
    if (LocalstorageService.isSellerLoggedIn()){
      return router.createUrlTree(['/seller/data'])
    }
    if (LocalstorageService.isBuyerLoggedIn()){
      return router.createUrlTree(['/buyer/data'])
    }
    if (LocalstorageService.isSuperAdminLoggedIn()){
      return router.createUrlTree(['/admin/data']);
    }
  }
  return true;
};
