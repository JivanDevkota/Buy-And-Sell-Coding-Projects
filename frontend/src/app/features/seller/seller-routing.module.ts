import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {DashboardDataComponent} from "./dashboard-data/dashboard-data.component";
import {UploadProjectComponent} from "./upload-project/upload-project.component";
import {ProjectsComponent} from "./projects/projects.component";
import {UploadProjectfileComponent} from "./upload-projectfile/upload-projectfile.component";
import {ProjectComponent} from "./project/project.component";
import {SalesComponent} from "./sales/sales.component";
import {AnalyticsComponent} from "./analytics/analytics.component";
import {ReviewsComponent} from "./reviews/reviews.component";
import {EditProjectComponent} from "./edit-project/edit-project.component";
import {EarningsComponent} from "./earnings/earnings.component";

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
      },
      {
        path:'project/:id/add-file',
        component:UploadProjectfileComponent
      },
      {
        path:'edit',
        component:EditProjectComponent
      },
      {
        path:'project/:id',
        component:ProjectComponent
      },
      {
        path:'sales',
        component:SalesComponent
      },
      {
        path:'earnings',
        component:EarningsComponent
      },
      {
        path:'analytics',
        component:AnalyticsComponent
      },
      {
        path:'reviews',
        component:ReviewsComponent
      }

    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SellerRoutingModule { }
