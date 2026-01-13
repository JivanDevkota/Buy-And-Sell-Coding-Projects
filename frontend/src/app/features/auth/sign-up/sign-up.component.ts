import {Component} from '@angular/core';
import {RegisterRequest} from "../../../core/model/RegisterRequest";
import {AuthService} from "../../../core/services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {

  form : RegisterRequest = {
    username: "",
    email: "",
    password: ""
  }

  loading = false
  error = ""

  constructor(private authService: AuthService, private router: Router) {
  }

  onSubmit(){
    if (!this.form.username || !this.form.email || !this.form.password) {
      this.error = "All fields must be required!";
    }
    this.loading = true;
    this.error=""

    this.authService.registerUser(this.form).subscribe({
      next: (data: any) => {
        this.router.navigate(['/buyer']);
      },
      error: (error: any) => {
        this.error = error;
      }
    })
  }
}
