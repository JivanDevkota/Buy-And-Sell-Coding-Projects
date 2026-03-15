import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyPurchaseComponent } from './my-purchase.component';

describe('MyPurchaseComponent', () => {
  let component: MyPurchaseComponent;
  let fixture: ComponentFixture<MyPurchaseComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MyPurchaseComponent]
    });
    fixture = TestBed.createComponent(MyPurchaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
