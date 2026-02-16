import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadProjectfileComponent } from './upload-projectfile.component';

describe('UploadProjectfileComponent', () => {
  let component: UploadProjectfileComponent;
  let fixture: ComponentFixture<UploadProjectfileComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UploadProjectfileComponent]
    });
    fixture = TestBed.createComponent(UploadProjectfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
