import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../core/services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {
  loginForm !: FormGroup
  hidePassword = true;
  loading = false

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router) {
  }

  ngOnInit() {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    })
  }

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      alert('Please fill your all fields.')
      return;
    }
    this.loading = true;
    const loginRequest = {
      username :this.loginForm.value.username,
      password : this.loginForm.value.password
    }
    this.authService.loginUser(loginRequest).subscribe({
      next: (success) => {
        this.loading = false;
        if (success) {
          this.router.navigate(['/dashboard']);
        } else {
          alert('Login failed');
        }
      },
      error: (error) => {
        this.loading = false;
        alert("something went wrong!");
      }
    })
  }
}
