import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistorypageadminComponent } from './historypageadmin.component';

describe('HistorypageadminComponent', () => {
  let component: HistorypageadminComponent;
  let fixture: ComponentFixture<HistorypageadminComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HistorypageadminComponent]
    });
    fixture = TestBed.createComponent(HistorypageadminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
