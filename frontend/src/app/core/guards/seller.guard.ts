import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {LocalstorageService} from "../services/localstorage.service";

export const sellerGuard: CanActivateFn = (route, state) => {


      const router = inject(Router);
      if (LocalstorageService.isSellerLoggedIn() && LocalstorageService.getToken()){
        return true;
      }
      return router.createUrlTree(['/auth/signin']);
};
