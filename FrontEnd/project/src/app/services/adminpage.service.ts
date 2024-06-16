import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from 'src/environments/environment';
import { OrderDetails } from '../interfaces/OrderDetails';
import { AdminOrder } from '../interfaces/AdminOrder';

@Injectable({
  providedIn: 'root',
})
export class AdminpageService {
  constructor(private http: HttpClient) { }

  public getAllCodes(): Observable<String[]> {
    return this.http.get<String[]>(environment.API_URL + '/admin/codes');
  }
  public getAllOrders(): Observable<AdminOrder[]> {
    return this.http.get<AdminOrder[]>(environment.API_URL + '/admin/orders');
  }

  public getOrderById(
    groupCode: string,
    orderId: string
  ): Observable<OrderDetails> {
    return this.http.get<OrderDetails>(
      environment.API_URL + '/admin/orders/' + groupCode + '/' + orderId
    );
  }
}
