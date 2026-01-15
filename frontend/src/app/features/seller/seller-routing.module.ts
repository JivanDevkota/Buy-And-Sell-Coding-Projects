import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {DashboardDataComponent} from "./dashboard-data/dashboard-data.component";
import {UploadProjectComponent} from "./upload-project/upload-project.component";
import {ProjectsComponent} from "./projects/projects.component";

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    children:[
      // {
      //   path: 'dashboard',
      //   component: DashboardComponent
      // },
      {
        path: 'data',
        component:DashboardDataComponent
      },
      {
        path: 'upload',
        component:UploadProjectComponent
      },
      {
        path: 'projects',
        component:ProjectsComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SellerRoutingModule { }
