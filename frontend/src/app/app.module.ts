import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {SharedModule} from "./shared/module/shared.module";
import { HomeComponent } from './features/home/home.component';
import {AuthInterceptor} from "./core/interceptor/authInterceptor.service";
import {FooterComponent} from "./shared/footer/footer.component";
import { ProjectDetailsComponent } from './features/project-details/project-details.component';



@NgModule({
    declarations: [
        AppComponent,
        HomeComponent,
        FooterComponent,
        ProjectDetailsComponent,
    ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    // SellerModule,
    // BuyerModule,
    HttpClientModule,
    // AuthModule,
    SharedModule,
    // SuperAdminModule,
  ],
  providers: [
    {provide:HTTP_INTERCEPTORS,useClass:AuthInterceptor,multi:true},
  ],
  exports: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
