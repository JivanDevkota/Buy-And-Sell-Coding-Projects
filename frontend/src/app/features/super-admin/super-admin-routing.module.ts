import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {DashboardDataComponent} from "./dashboard-data/dashboard-data.component";
import {CategoryComponent} from "./category/category.component";
import {AllUsersComponent} from "./all-users/all-users.component";
import {AllSellersComponent} from "./all-sellers/all-sellers.component";
import {PendingProjectsComponent} from "./pending-projects/pending-projects.component";
import {AllBuyersComponent} from "./all-buyers/all-buyers.component";
import {PendingApprovalsComponent} from "./pending-approvals/pending-approvals.component";
import {AdministratorsComponent} from "./administrators/administrators.component";
import {LanguageComponent} from "./language/language.component";
import {AllProjectsComponent} from "./all-projects/all-projects.component";

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    children: [
      {path: 'data', component: DashboardDataComponent},
      {path: 'categories', component: CategoryComponent},
      {path:'users',component:AllUsersComponent},
      {path:'sellers',component:AllSellersComponent},
      {path:'buyers',component:AllBuyersComponent},
      {path:'pending-projects',component:PendingProjectsComponent},
      {path:'pending-approvals',component:PendingApprovalsComponent},
      {path:'admins',component:AdministratorsComponent},
      {path:"technologies",component:LanguageComponent},
      {path:'projects',component:AllProjectsComponent},
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SuperAdminRoutingModule {

}
