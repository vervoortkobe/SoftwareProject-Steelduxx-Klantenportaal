import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ResetpasswordService } from '../services/resetpassword.service';
import { ActivatedRoute } from '@angular/router';
import { Modal, ModalOptions } from 'flowbite';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
})
export class ResetPasswordComponent implements AfterViewInit {
  public password: string = '';
  public confirmPassword: string = '';

  public error: string = '';

  public token: any = this.route.snapshot.paramMap.get('token');

  private successfullModal: any;

  constructor(
    private reset: ResetpasswordService,
    private route: ActivatedRoute
  ) {}

  ngAfterViewInit(): void {
    // set the modal menu element
    const $targetEl = document.getElementById('popup-modal');

    // options with default values
    const options: ModalOptions = {
      placement: 'center',
      backdrop: 'dynamic',
      backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
      closable: true,
    };

    // instance options object
    const instanceOptions = {
      id: 'popup-modal',
      override: true,
    };

    this.successfullModal = new Modal($targetEl, options, instanceOptions);
  }

  resetPassword() {
    this.error = '';

    if (this.password !== this.confirmPassword) {
      this.error = 'Passwords do not match';
      return;
    }

    this.reset.resetPasswordWithToken(this.token, this.password).subscribe({
      complete: () => {
        this.successfullModal.show();
      },
      error: (error) => {
        if (error.status === 401) {
          this.error = 'The token has expired or is invalid.';
        } else {
          this.error = 'An error occurred';
        }
      },
    });
  }
}
