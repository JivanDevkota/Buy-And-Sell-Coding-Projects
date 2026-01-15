import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SuperAdminRoutingModule } from './super-admin-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DashboardDataComponent } from './dashboard-data/dashboard-data.component';
import {FormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    DashboardComponent,
    DashboardDataComponent
  ],
    imports: [
        CommonModule,
        SuperAdminRoutingModule,
        FormsModule
    ]
})
export class SuperAdminModule { }
