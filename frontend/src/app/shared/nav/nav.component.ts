import { Component } from '@angular/core';
import {LocalstorageService} from "../../core/services/localstorage.service";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent {
  isSellerLoggedIn=LocalstorageService.isSellerLoggedIn();
  isBuyerLoggedIn=LocalstorageService.isBuyerLoggedIn();
  isSuperAdminLoggedIn=LocalstorageService.isSuperAdminLoggedIn();
  username=LocalstorageService.getUserName();
}
