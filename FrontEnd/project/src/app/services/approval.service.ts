import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AccountApproval } from '../interfaces/account-approval';
import { Role } from '../enums/role';

@Injectable({
  providedIn: 'root',
})
export class ApprovalService {
  readonly endpoint: string = environment.API_URL + '/users/admin';
  readonly approvalaccounts: string = '/approval';
  readonly changerole: string = '/role';
  readonly changecode: string = '/code';

  constructor(private http: HttpClient) {}

  public getAllAccounts(): Observable<AccountApproval[]> {
    return this.http.get<AccountApproval[]>(
      this.endpoint + this.approvalaccounts
    );
  }

  public getAccountDetail(id: number): Observable<AccountApproval> {
    return this.http.get<AccountApproval>(
      this.endpoint + this.approvalaccounts + '/' + id
    );
  }

  public changeApproval(id: number, approval: boolean): Observable<any> {
    return this.http.post<any>(this.endpoint + this.approvalaccounts, {
      id: id,
      approval: approval,
    });
  }

  public changeRole(id: number, role: Role): Observable<any> {
    return this.http.post<any>(this.endpoint + this.changerole, {
      id: id,
      role: role,
    });
  }

  public changeCode(id: number, code: String): Observable<any> {
    return this.http.post<any>(this.endpoint + this.changecode, {
      id: id,
      code: code,
    });
  }

  public deleteAccount(id: number): Observable<any> {
    return this.http.delete<any>(this.endpoint + '/' + id);
  }
}
