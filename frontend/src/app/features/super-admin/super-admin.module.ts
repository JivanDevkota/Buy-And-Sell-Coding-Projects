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
import { PendingProjectsComponent } from './pending-projects/pending-projects.component';
import { PendingApprovalsComponent } from './pending-approvals/pending-approvals.component';
import { AdministratorsComponent } from './administrators/administrators.component';
import {SharedModule} from "../../shared/module/shared.module";
import { LanguageComponent } from './language/language.component';
import { AllProjectsComponent } from './all-projects/all-projects.component';


@NgModule({
  declarations: [
    DashboardComponent,
    DashboardDataComponent,
    CategoryComponent,
    AllUsersComponent,
    AllSellersComponent,
    AllBuyersComponent,
    PendingProjectsComponent,
    PendingApprovalsComponent,
    AdministratorsComponent,
    LanguageComponent,
    AllProjectsComponent
  ],
    imports: [
        CommonModule,
        SuperAdminRoutingModule,
        FormsModule,
        ReactiveFormsModule,
      SharedModule
    ]
})
export class SuperAdminModule { }
