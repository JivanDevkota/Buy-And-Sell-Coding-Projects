import { Component } from '@angular/core';
import {LocalstorageService} from "../../core/services/localstorage.service";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {AuthStateService} from "../../core/services/AuthStateService";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent {
  // isSellerLoggedIn=LocalstorageService.isSellerLoggedIn();
  // isBuyerLoggedIn=LocalstorageService.isBuyerLoggedIn();
  // isSuperAdminLoggedIn=LocalstorageService.isSuperAdminLoggedIn();
  // username=LocalstorageService.getUserName();

  isSellerLoggedIn = false;
  isBuyerLoggedIn = false;
  isSuperAdminLoggedIn = false;
  username: string | null = null;
  cartCount = 0;

  private sub!: Subscription;

  constructor(private router: Router, private authState: AuthStateService) {}

  ngOnInit(): void {
    // Subscribe to reactive auth state so nav updates instantly after login/logout
    this.sub = this.authState.authState$.subscribe(() => {
      this.isSellerLoggedIn = LocalstorageService.isSellerLoggedIn() as boolean;
      this.isBuyerLoggedIn = LocalstorageService.isBuyerLoggedIn() as boolean;
      this.isSuperAdminLoggedIn = LocalstorageService.isSuperAdminLoggedIn() as boolean;
      this.username = LocalstorageService.getUserName();
    });
  }

  logout(): void {
    LocalstorageService.signOut();
    this.authState.notify(); // triggers nav to re-evaluate
    this.router.navigate(['/home']);
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }
}
