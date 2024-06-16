import { OrderRequest } from '../interfaces/OrderRequest';
import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { OrderrequestsService } from './orderrequests.service';
import { environment } from '../../environments/environment';

describe('OrderrequestsService', () => {
  let service: OrderrequestsService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [OrderrequestsService],
    });
    service = TestBed.inject(OrderrequestsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all order requests', () => {
    const mockOrderRequests: OrderRequest[] = [
      // Define your mock order requests here
    ];

    service.getAllOrderRequests().subscribe((orderRequests) => {
      expect(orderRequests).toEqual(mockOrderRequests);
    });

    const req = httpMock.expectOne(environment.API_URL + '/orderrequests');
    expect(req.request.method).toBe('GET');
    req.flush(mockOrderRequests);
  });

  it('should get order request by customer reference number', () => {
    const mockOrderRequest: OrderRequest = {
      transportType: '',
      customerReferenceNumber: '',
      customerCode: '',
      portCode: '',
      cargoType: '',
      products: [],
    };
    const customerReferenceNumber = '12345';

    service
      .getOrderRequestByCustomerReferenceNumber(customerReferenceNumber)
      .subscribe((orderRequest) => {
        expect(orderRequest).toEqual(mockOrderRequest);
      });

    const req = httpMock.expectOne(
      environment.API_URL + '/orderrequests/' + customerReferenceNumber
    );
    expect(req.request.method).toBe('GET');
    req.flush(mockOrderRequest);
  });
});
