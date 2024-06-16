import { AfterViewInit, Component } from '@angular/core';
import { ResetpasswordService } from '../services/resetpassword.service';
import { Modal, ModalOptions } from 'flowbite';
import GeneralUtils from '../utils/general-utils';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
})
export class ForgotPasswordComponent implements AfterViewInit {
  public email: string = '';
  public error: string = '';

  private successfullModal: any;

  constructor(private reset: ResetpasswordService) {}

  resetPassword() {
    if (!GeneralUtils.validateEmail(this.email)) {
      this.error = 'Invalid email address';
      return;
    }

    this.reset.requestTokenForEmail(this.email).subscribe({
      complete: () => {
        this.successfullModal.show();
      },
      error: (error) => {
        this.error = 'An error occurred';
      },
    });
  }

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
}
