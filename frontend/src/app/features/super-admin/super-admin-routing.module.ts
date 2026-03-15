import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {DashboardDataComponent} from "./dashboard-data/dashboard-data.component";
import {CategoryComponent} from "./category/category.component";
import {AllUsersComponent} from "./all-users/all-users.component";
import {AllSellersComponent} from "./all-sellers/all-sellers.component";
import {PendingProjectsComponent} from "./pending-projects/pending-projects.component";

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    children: [
      {path: 'data', component: DashboardDataComponent},
      {path: 'categories', component: CategoryComponent},
      {path:'users',component:AllUsersComponent},
      {path:'sellers',component:AllSellersComponent},
      {path:'pending-projects',component:PendingProjectsComponent},
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SuperAdminRoutingModule {

}
