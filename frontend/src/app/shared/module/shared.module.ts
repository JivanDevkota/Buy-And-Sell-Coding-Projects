import {NgModule} from "@angular/core";
import {NavComponent} from "../nav/nav.component";
import {CommonModule} from "@angular/common";
import {RouterModule} from "@angular/router";
import {ReviewModalComponent} from "../review-modal/review-modal.component";
import {FormsModule} from "@angular/forms";

@NgModule({
 declarations: [
   NavComponent,
   ReviewModalComponent,
 ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
  ],
  exports: [
    NavComponent,
    ReviewModalComponent,
  ]
})
export class SharedModule {}
