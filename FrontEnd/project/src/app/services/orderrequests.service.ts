import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OrderRequest } from '../interfaces/OrderRequest';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class OrderrequestsService {
  private endpoint: string = environment.API_URL + '/requests';

  constructor(private http: HttpClient) {}

  public getAllOrderRequests(): Observable<OrderRequest[]> {
    return this.http.get<OrderRequest[]>(this.endpoint);
  }

  public getOrderRequestById(id: string): Observable<OrderRequest> {
    return this.http.get<OrderRequest>(this.endpoint + '/' + id);
  }

  public createNewOrderRequest(
    orderRequest: OrderRequest
  ): Observable<OrderRequest> {
    return this.http.post<OrderRequest>(this.endpoint, orderRequest);
  }

  public approveOrderRequest(id: String): Observable<OrderRequest> {
    return this.http.post<OrderRequest>(
      environment.API_URL + '/admin/requests/' + id,
      null
    );
  }

  public denyOrderRequest(id: string): Observable<OrderRequest> {
    return this.http.delete<OrderRequest>(
      environment.API_URL + '/admin/requests/' + id
    );
  }

  public overwriteOrderRequest(
    updatedOrderRequest: OrderRequest
  ): Observable<OrderRequest> {
    return this.http.patch<OrderRequest>(
      this.endpoint + '/' + updatedOrderRequest.id,
      updatedOrderRequest
    );
  }
}
