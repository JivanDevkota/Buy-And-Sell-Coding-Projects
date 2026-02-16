import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SuperAdminRoutingModule } from './super-admin-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DashboardDataComponent } from './dashboard-data/dashboard-data.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { CategoryComponent } from './category/category.component';
import { AllUsersComponent } from './all-users/all-users.component';
import { AllSellersComponent } from './all-sellers/all-sellers.component';
import { AllBuyersComponent } from './all-buyers/all-buyers.component';


@NgModule({
  declarations: [
    DashboardComponent,
    DashboardDataComponent,
    CategoryComponent,
    AllUsersComponent,
    AllSellersComponent,
    AllBuyersComponent
  ],
    imports: [
        CommonModule,
        SuperAdminRoutingModule,
        FormsModule,
        ReactiveFormsModule
    ]
})
export class SuperAdminModule { }
