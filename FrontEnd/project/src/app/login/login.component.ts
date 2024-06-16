import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import GeneralUtils from '../utils/general-utils';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';

  emailerror: string = '';
  passworderror: string = '';

  constructor(private auth : AuthService) {}

  login() {
    let valid: boolean = true;

    //Validate email and password fields
    if (this.email.length == 0) {
      this.emailerror = "Email is required.";
      valid = false;
    } else if (!GeneralUtils.validateEmail(this.email)) {
      this.emailerror = "Email is invalid.";
      valid = false;
    } else {
      this.emailerror = "";
    }

    if (this.password.length == 0) {
      this.passworderror = "Password is required.";
      valid = false;
    } else {
      this.passworderror = "";
    }

    if (valid) {
      this.auth.login(this.email, this.password).subscribe({complete: () => {
        window.location.href = "/";
      },
      error: (error) => {
        if (error.status == 401) {
          this.passworderror = "Invalid email or password.";
        } else if (error.status == 404) {
          this.passworderror = "User is not yet approved.";
        } else {
          this.passworderror = "An error occurred.";
        }
      }
    });
    }
  }

  public loginfake() {
    window.location.href = "/";
  }
}
