import { Component } from '@angular/core';
import {LocalstorageService} from "../../../core/services/localstorage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

      constructor(private router: Router) {
      }

      logout(){
        LocalstorageService.signOut();
        this.router.navigate(['/auth/signin']);
      }
}
