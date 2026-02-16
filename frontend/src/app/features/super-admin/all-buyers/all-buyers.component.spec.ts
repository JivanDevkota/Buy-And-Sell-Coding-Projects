import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllBuyersComponent } from './all-buyers.component';

describe('AllBuyersComponent', () => {
  let component: AllBuyersComponent;
  let fixture: ComponentFixture<AllBuyersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AllBuyersComponent]
    });
    fixture = TestBed.createComponent(AllBuyersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
