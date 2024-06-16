import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderrequestdetailsComponent } from './orderrequestdetails.component';

describe('OrderrequestdetailsComponent', () => {
  let component: OrderrequestdetailsComponent;
  let fixture: ComponentFixture<OrderrequestdetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrderrequestdetailsComponent]
    });
    fixture = TestBed.createComponent(OrderrequestdetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
