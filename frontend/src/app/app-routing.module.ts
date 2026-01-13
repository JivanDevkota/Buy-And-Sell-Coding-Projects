import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./features/home/home.component";

const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent},
  {
    path:"auth",
    loadChildren:()=>import('./features/auth/auth.module').then(m=>m.AuthModule)
  },
  {
    path: 'seller',
    loadChildren: () =>
      import('./features/seller/seller.module').then((s) => s.SellerModule)
  },
  {
    path: 'buyer',
    loadChildren: () =>
      import('./features/buyer/buyer.module').then((b) => b.BuyerModule)

  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
