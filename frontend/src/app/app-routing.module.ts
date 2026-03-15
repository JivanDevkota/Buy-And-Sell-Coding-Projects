import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./features/home/home.component";
import {sellerGuard} from "./core/guards/seller.guard";
import {buyerGuard} from "./core/guards/buyer.guard";
import {superAdminGuard} from "./core/guards/super-admin.guard";
import {noauthGuard} from "./core/guards/noauth.guard";
import {ProjectDetailsComponent} from "./features/project-details/project-details.component";

const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'projects/:id', component: ProjectDetailsComponent},
  {path: 'home', component: HomeComponent},
  {
    path: "auth",
    canActivate: [noauthGuard],
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'seller',
    canActivate: [sellerGuard],
    loadChildren: () =>
      import('./features/seller/seller.module').then((s) => s.SellerModule)
  },
  {
    path: 'buyer',
    canActivate: [buyerGuard],
    loadChildren: () =>
      import('./features/buyer/buyer.module').then((b) => b.BuyerModule)

  },
  {
    path: 'admin',
    canActivate: [superAdminGuard],
    loadChildren: () =>
      import('./features/super-admin/super-admin.module').then((s) => s.SuperAdminModule)
  },
  // Fallback
  {path: '**', redirectTo: '/home'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
