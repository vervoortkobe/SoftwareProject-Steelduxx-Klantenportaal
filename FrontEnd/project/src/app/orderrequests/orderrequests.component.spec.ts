import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderrequestsComponent } from './orderrequests.component';

describe('OrderrequestsComponent', () => {
  let component: OrderrequestsComponent;
  let fixture: ComponentFixture<OrderrequestsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrderrequestsComponent]
    });
    fixture = TestBed.createComponent(OrderrequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
