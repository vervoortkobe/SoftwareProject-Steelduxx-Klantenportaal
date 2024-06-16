import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Account } from '../interfaces/account';
import { Role } from '../enums/role';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  public get Role(): typeof Role {
    return Role;
  }

  public account: Account = JSON.parse(localStorage.getItem('currentUser')!);

  constructor(private router: Router, private auth: AuthService) {}
  ngOnInit() {}

  logout() {
    this.auth.logout().subscribe(() => {
      window.location.href = '/login';
    });
  }

  orderDetails(): string {
    let splitUrl = this.router.url.split('/');
    return this.router.url.includes('/orders/') && splitUrl.length <= 3
      ? splitUrl[splitUrl.length - 1]
      : 'nil';
  }

  orderAdminDetails(): string {
    let splitUrl = this.router.url.split('/');
    return this.router.url.includes('/orders/') && splitUrl.length >= 4
      ? splitUrl[splitUrl.length - 1]
      : 'nil';
  }

  extractCustomerCode(): string {
    let splitUrl = this.router.url.split('/');
    return this.router.url.includes('/orders/') && splitUrl.length >= 3
      ? splitUrl[splitUrl.length - 2]
      : 'nil';
  }

  requestDetails(): string {
    return this.router.url.includes('/requests/')
      ? this.router.url.split('/')[this.router.url.split('/').length - 1]
      : 'nil';
  }
  adminOrders(): string {
    return this.router.url.includes('/admin/')
      ? this.router.url.split('/')[this.router.url.split('/').length - 1]
      : 'nil';
  }
}
