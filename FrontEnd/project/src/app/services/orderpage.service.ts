import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ShippingData } from '../interfaces/ShippingData';
import { OrderDetails } from '../interfaces/OrderDetails';

@Injectable({
  providedIn: 'root',
})

export class OrderpageService {
  constructor(private http: HttpClient) {}

  public getAllOrders(): Observable<ShippingData[]> {
    return this.http.get<ShippingData[]>(environment.API_URL + '/orders');
  }

  public getOrderById(orderId: string): Observable<OrderDetails> {
    return this.http.get<OrderDetails>(
      environment.API_URL + '/orders/' + orderId
    );
  }
}