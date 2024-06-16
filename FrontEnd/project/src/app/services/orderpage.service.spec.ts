import { TestBed } from '@angular/core/testing';

import { OrderpageService } from './orderpage.service';

describe('OrderpageService', () => {
  let service: OrderpageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrderpageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
