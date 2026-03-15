import {NgModule} from "@angular/core";
import {NavComponent} from "../nav/nav.component";
import {CommonModule} from "@angular/common";
import {RouterLink, RouterLinkActive, RouterModule} from "@angular/router";

@NgModule({
 declarations: [
   NavComponent,
 ],
  imports: [
    CommonModule,
    RouterModule,
    RouterLinkActive,
    RouterLink
  ],
  exports: [
    NavComponent,
  ]
})
export class SharedModule {}
