import { HttpClient } from '@angular/common/http';
import { Token } from '@angular/compiler';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ResetpasswordService {
  readonly endpoint: string = environment.API_URL + '/password-reset';
  readonly requestToken: string = '/request-token';
  readonly resetPassword: string = '/reset-password';

  constructor(private http: HttpClient) {}

  public requestTokenForEmail(email: string): Observable<any> {
    return this.http.post(this.endpoint + this.requestToken, { email: email });
  }

  public resetPasswordWithToken(
    token: string,
    newPassword: string
  ): Observable<any> {
    return this.http.post(this.endpoint + this.resetPassword, {
      token: token,
      newPassword: newPassword,
    });
  }
}
