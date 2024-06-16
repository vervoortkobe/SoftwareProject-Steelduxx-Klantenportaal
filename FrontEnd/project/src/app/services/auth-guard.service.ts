import { inject, Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';
import { Account } from '../interfaces/account';

@Injectable({
  providedIn: 'root',
})
export class AuthGuardService {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return new Observable<boolean>((obs) => {
      this.auth.isAuthenticated().subscribe({
        complete: () => {
          this.auth.getAccountInfo().subscribe((account: Account) => {
            localStorage.setItem('currentUser', JSON.stringify(account));
            if (next.data['role'] != null) {
              let currentUser: Account = JSON.parse(
                localStorage.getItem('currentUser')!
              );
              if (next.data['role'] == currentUser.role) {
                window.location.href = next.data['success'];
                obs.next(true);
              } else if (next.data['fail'] != null) {
                window.location.href = next.data['fail'];
                obs.next(true);
              }
            } else {
              obs.next(true);
            }
          });
        },
        error: () => {
          window.location.href = '/login';
          obs.next(false);
        },
      });
    });
  }
}

export const AuthGuard: CanActivateFn = (
  next: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
): Observable<boolean> => {
  return inject(AuthGuardService).canActivate(next, state);
};
