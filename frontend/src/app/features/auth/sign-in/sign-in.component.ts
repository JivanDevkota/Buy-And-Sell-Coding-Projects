 import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../core/services/auth.service";
import {Router} from "@angular/router";
 import {LocalstorageService} from "../../../core/services/localstorage.service";

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {
  loginForm!: FormGroup;
  hidePassword = true;
  loading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const loginRequest = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password
    };

    this.authService.loginUser(loginRequest).subscribe({
      next: (success) => {
        this.loading = false;
        if (success) {
          // Redirect based on role
          if (LocalstorageService.isSellerLoggedIn()) {
            this.router.navigate(['seller/data']);
          } else if (LocalstorageService.isBuyerLoggedIn()) {
            this.router.navigate(['home']);   // buyer stays on home (now with full nav)
          } else if (LocalstorageService.isSuperAdminLoggedIn()) {
            this.router.navigate(['admin/data']);
          } else {
            this.router.navigate(['home']);
          }
        } else {
          this.errorMessage = 'Invalid username or password. Please try again.';
        }
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Something went wrong. Please try again later.';
      }
    });
  }
}
