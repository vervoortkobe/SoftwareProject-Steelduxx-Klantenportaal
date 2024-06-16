import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarRoutingLayoutComponent } from './navbar-routing-layout.component';

describe('NavbarRoutingLayoutComponent', () => {
  let component: NavbarRoutingLayoutComponent;
  let fixture: ComponentFixture<NavbarRoutingLayoutComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NavbarRoutingLayoutComponent]
    });
    fixture = TestBed.createComponent(NavbarRoutingLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
