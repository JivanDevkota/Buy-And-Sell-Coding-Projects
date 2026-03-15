import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PendingProjectsComponent } from './pending-projects.component';

describe('PendingProjectsComponent', () => {
  let component: PendingProjectsComponent;
  let fixture: ComponentFixture<PendingProjectsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PendingProjectsComponent]
    });
    fixture = TestBed.createComponent(PendingProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
