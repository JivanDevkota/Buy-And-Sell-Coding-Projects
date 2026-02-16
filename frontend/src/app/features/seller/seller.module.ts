import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SellerRoutingModule } from './seller-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import {SharedModule} from "../../shared/module/shared.module";
import { DashboardDataComponent } from './dashboard-data/dashboard-data.component';
import { UploadProjectComponent } from './upload-project/upload-project.component';
import { ProjectsComponent } from './projects/projects.component';
import {SellerHeadComponent} from "../../shared/seller-head/seller-head.component";
import {FormsModule} from "@angular/forms";
import { UploadProjectfileComponent } from './upload-projectfile/upload-projectfile.component';
import { ProjectComponent } from './project/project.component';


@NgModule({
  declarations: [
    DashboardComponent,
    DashboardDataComponent,
    UploadProjectComponent,
    ProjectsComponent,
    SellerHeadComponent,
    UploadProjectfileComponent,
    ProjectComponent
  ],
    imports: [
        CommonModule,
        SellerRoutingModule,
        SharedModule,
        FormsModule
    ]
})
export class SellerModule { }
