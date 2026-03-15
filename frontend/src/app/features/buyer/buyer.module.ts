import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BuyerRoutingModule } from './buyer-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DashboardDataComponent } from './dashboard-data/dashboard-data.component';
import { WishlistComponent } from './wishlist/wishlist.component';
import {SharedModule} from "../../shared/module/shared.module";
import { MyPurchaseComponent } from './my-purchase/my-purchase.component';


@NgModule({
  declarations: [
    DashboardComponent,
    DashboardDataComponent,
    WishlistComponent,
    MyPurchaseComponent
  ],
  imports: [
    CommonModule,
    BuyerRoutingModule,
    SharedModule
  ]
})
export class BuyerModule { }
