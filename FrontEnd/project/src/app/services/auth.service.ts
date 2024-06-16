import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Account } from '../interfaces/account';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  readonly endpoint: string = environment.API_URL + '/users';

  constructor(private http: HttpClient) {}

  public login(email: string, password: string): Observable<any> {
    return this.http.post<any>(this.endpoint + '/login', {
      email: email,
      password: password,
    });
  }

  public logout(): Observable<any> {
    return this.http.get<any>(this.endpoint + '/logout');
  }

  public isAuthenticated(): Observable<boolean> {
    return this.http.get<boolean>(this.endpoint + '/authenticated');
  }

  public getAccountInfo(): Observable<Account> {
    return this.http.get<Account>(this.endpoint);
  }

  public createAccount(
    email: string,
    password: string,
    account_information: any
  ): Observable<any> {
    return this.http.post<any>(this.endpoint + '/register', {
      email: email,
      password: password,
      accountInformation: account_information,
    });
  }
}
