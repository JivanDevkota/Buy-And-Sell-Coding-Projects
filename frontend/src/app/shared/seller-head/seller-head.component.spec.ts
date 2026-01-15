import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SellerHeadComponent } from './seller-head.component';

describe('SellerHeadComponent', () => {
  let component: SellerHeadComponent;
  let fixture: ComponentFixture<SellerHeadComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SellerHeadComponent]
    });
    fixture = TestBed.createComponent(SellerHeadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
