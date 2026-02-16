import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllSellersComponent } from './all-sellers.component';

describe('AllSellersComponent', () => {
  let component: AllSellersComponent;
  let fixture: ComponentFixture<AllSellersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AllSellersComponent]
    });
    fixture = TestBed.createComponent(AllSellersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
