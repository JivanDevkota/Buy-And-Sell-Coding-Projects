import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {DashboardDataComponent} from "./dashboard-data/dashboard-data.component";
import {MyPurchaseComponent} from "./my-purchase/my-purchase.component";


const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    // children:[
    //   {
    //     path: 'data',
    //     component:DashboardDataComponent,
    //
    //   },
    // ]
    children: [
      {path: '', redirectTo: 'overview', pathMatch: 'full'},
      {path: 'overview',   component: DashboardDataComponent},
      {path: 'cart',       component: DashboardDataComponent},  // same component, different tab
      {path: 'purchases',  component: MyPurchaseComponent},
      {path: 'downloads',  component: DashboardDataComponent},
      {path: 'wishlist',   component: DashboardDataComponent},
      {path: 'reviews',    component: DashboardDataComponent},
      {path: 'orders',     component: DashboardDataComponent},
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BuyerRoutingModule { }
