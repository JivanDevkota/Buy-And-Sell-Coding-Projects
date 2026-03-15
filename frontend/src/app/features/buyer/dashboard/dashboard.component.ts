import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {AuthStateService} from "../../../core/services/AuthStateService";
import {LocalstorageService} from "../../../core/services/localstorage.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  username = '';
  usernameInitials = '';

  constructor(
    private router: Router,
    private authState: AuthStateService
  ) {}

  ngOnInit(): void {
    const name = LocalstorageService.getUserName() ?? 'Buyer';
    this.username = name;
    // Build initials: "Alex Johnson" → "AJ", "alex" → "A"
    this.usernameInitials = name
      .split(' ')
      .map(w => w.charAt(0).toUpperCase())
      .slice(0, 2)
      .join('');
  }

  logout(): void {
    LocalstorageService.signOut();
    this.authState.notify();
    this.router.navigate(['/home']);
  }
}
