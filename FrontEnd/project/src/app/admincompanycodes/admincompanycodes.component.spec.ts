import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdmincompanycodesComponent } from './admincompanycodes.component';

describe('AdmincompanycodesComponent', () => {
  let component: AdmincompanycodesComponent;
  let fixture: ComponentFixture<AdmincompanycodesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdmincompanycodesComponent]
    });
    fixture = TestBed.createComponent(AdmincompanycodesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
