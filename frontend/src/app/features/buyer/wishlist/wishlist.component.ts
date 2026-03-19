import {Component, OnInit} from '@angular/core';
import {WishlistResponse} from "../../../core/model/WishlistResponse";
import {BuyerService} from "../../../core/services/buyer.service";
import {LocalstorageService} from "../../../core/services/localstorage.service";

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent implements OnInit {
  wishlist:WishlistResponse[]=[]
  searchText: string = '';

  constructor(private buyerService:BuyerService) {
  }

  getBannerClass(index: number): string {
    const classes = [
      'banner-purple',
      'banner-amber',
      'banner-coral',
      'banner-teal',
      'banner-blue'
    ];
    return classes[index % classes.length];
  }
  ngOnInit() {
    this.loadWishlists()
  }

  loadWishlists(){
    const buyerId=Number(LocalstorageService.getUserId());
    this.buyerService.getAllMyWishlist(buyerId).subscribe({
      next: data=>{
        this.wishlist=data;
        console.log(data);
      },
      error: error=>{
        console.log(error);
      }
    })
  }

  // deleteMyWishlist(projectId:number){
  //   this.buyerService.deleteMyWishlist(projectId).subscribe({
  //     next: data=>{
  //       console.log(data);
  //       this.loadWishlists();
  //     },
  //     error: error=>{
  //       console.log(error);
  //     }
  //   })
  // }

  deleteMyWishlist(projectId: number) {
    // ✅ remove from UI instantly
    this.wishlist = this.wishlist.filter(item => item.id !== projectId);

    // ✅ then call backend
    this.buyerService.deleteMyWishlist(projectId).subscribe({
      next: () => {
        console.log("Deleted successfully");
      },
      error: (error) => {
        console.log(error);

        // ❗ rollback if API fails
        this.loadWishlists();
      }
    });
  }
}
