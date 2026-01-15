import { NgModule } from '@angular/core';
// import { CommonModule } from '@angular/common';

import { AuthRoutingModule } from './auth-routing.module';
import { SignInComponent } from './sign-in/sign-in.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import {SharedModule} from "../../shared/module/shared.module";
import {NgIf} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    SignInComponent,
    SignUpComponent,
  ],
  imports: [
    AuthRoutingModule,
    SharedModule,
    NgIf,
    FormsModule,
    ReactiveFormsModule,

  ]
})
export class AuthModule { }
