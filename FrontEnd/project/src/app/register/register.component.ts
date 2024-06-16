import { AfterViewInit, Component } from '@angular/core';
import { Modal, ModalOptions } from 'flowbite';
import { AccountInformation } from '../interfaces/account-information';
import { AccountCreation } from '../interfaces/account-creation';
import { AuthService } from '../services/auth.service';
import GeneralUtils from '../utils/general-utils';
import { AccountInformationCreation } from '../interfaces/account-information-creation';
import { Country } from '../interfaces/country';
import { CountryService } from '../services/country.service';
import { Registersteps } from '../enums/registersteps';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements AfterViewInit {
  //In hindsight I should have just done this with an array or something
  //But in the end that wastes too much time
  //Time for boilerplate code!!! :D

  //TODO: refactor this shitty code

  /* ERROR MESSAGES */
  public emailError: string = '';
  public passwordError: string = '';
  public passwordRepeatError: string = '';

  public companyError: string = '';
  public countryError: string = '';
  public contact_firstnameError: string = '';
  public contact_lastnameError: string = '';
  public contact_phonenumberError: string = '';
  public streetError: string = '';
  public postalCodeError: string = '';
  public cityError: string = '';
  public numberEror: string = '';
  public boxError: string = '';

  public street_secondaryError: string = '';
  public vatError: string = '';

  public generalError: string = '';

  public account_information: AccountInformationCreation = {
    company_name: '',
    contact_firstname: '',
    contact_lastname: '',
    postal_code: '',
    city: '',
    street: '',
    street_number: '',
    box: '',
    btw_number: '',
    countryId: 0,
    contact_telephone: '',
    street_secondary: '',
  };

  public account: AccountCreation = {
    email: '',
    password: '',
    accountInformation: this.account_information,
  };

  public countries: Country[] = [];

  public confirmPassword: string = '';

  constructor(
    private auth: AuthService,
    private countryService: CountryService
  ) {
    countryService.getCountries().subscribe((countries) => {
      this.countries = countries;
    });
  }

  ngAfterViewInit(): void {
    
  }

  public registerUser() {
    let valid: boolean = true;

    //Validate email and password fields
    if (this.account.email.length == 0) {
      this.emailError = 'Email is required.';
      valid = false;
    } else if (!GeneralUtils.validateEmail(this.account.email)) {
      this.emailError = 'Email is invalid.';
      valid = false;
    } else {
      this.emailError = '';
    }

    if (this.account.password.length == 0) {
      this.passwordError = 'Password is required.';
      valid = false;
    } else {
      this.passwordError = '';
    }

    if (this.confirmPassword != this.account.password) {
      this.passwordRepeatError = 'Passwords do not match.';
      valid = false;
    } else {
      this.passwordRepeatError = '';
    }

    if (valid) this.step++;
  }

  public contactValidate() {
    let valid: boolean = true;

    //Validate account information fields

    if (this.account_information.company_name.length == 0) {
      this.companyError = 'Company name is required.';
      valid = false;
    } else {
      this.companyError = '';
    }

    if (this.account_information.countryId == 0) {
      this.countryError = 'Please select a country.';
      valid = false;
    } else {
      this.countryError = '';
    }

    if (this.account_information.contact_firstname.length == 0) {
      this.contact_firstnameError = 'First name is required.';
      valid = false;
    } else {
      this.contact_firstnameError = '';
    }

    if (this.account_information.contact_lastname.length == 0) {
      this.contact_lastnameError = 'Last name is required.';
      valid = false;
    } else {
      this.contact_lastnameError = '';
    }

    if (
      this.account_information.contact_telephone != '' &&
      !GeneralUtils.validatePhone(this.account_information.contact_telephone)
    ) {
      this.contact_phonenumberError = 'Phone number is invalid.';
      valid = false;
    } else {
      this.contact_phonenumberError = '';
    }

    if (valid) this.step++;
  }

  public finalRegister() {
    let valid: boolean = true;

    if (this.account_information.street.length == 0) {
      this.streetError = 'Street is required.';
      valid = false;
    } else {
      this.streetError = '';
    }

    if (this.account_information.street_number.length == 0) {
      this.numberEror = 'Number is required.';
      valid = false;
    } else {
      this.numberEror = '';
    }

    if (this.account_information.postal_code.length == 0) {
      this.postalCodeError = 'Postal code is required.';
      valid = false;
    } else {
      this.postalCodeError = '';
    }

    if (this.account_information.city.length == 0) {
      this.cityError = 'City is required.';
      valid = false;
    } else {
      this.cityError = '';
    }

    if (
      this.account_information.btw_number != '' &&
      !GeneralUtils.validateVAT(this.account_information.btw_number)
    ) {
      this.vatError = 'VAT is invalid.';
      valid = false;
    } else {
      this.vatError = '';
    }

    if (valid) {
      this.auth
        .createAccount(
          this.account.email,
          this.account.password,
          this.account_information
        )
        .subscribe({
          complete: () => {
            this.step++;
          },
          error: (error) => {
            if (error.status == 409) {
              this.generalError = 'An account with this email already exists.';
            } else if (error.status == 400) {
              this.generalError = 'Validation failed. Please check your input.';
            } else {
              this.generalError = 'An error occurred. Please try again later.';
            }
          },
        });
    }
  }

  public consoleLog(value: any) {
    console.log(value);
  }

  public insertSpaces(value: string) {
    let spaced = value.replace(/([A-Z])/g, ' $1').trim();

    return spaced.charAt(0).toUpperCase() + spaced.slice(1);
  }

  public step: Registersteps = Registersteps.AccountInfo;

  public nextStep() {
    switch (this.step) {
      case Registersteps.AccountInfo:
        this.registerUser();
        break;
      case Registersteps.ContactInfo:
        this.contactValidate();
        break;
      case Registersteps.CompanyInfo:
        this.finalRegister();
        break;
    }
  }

  public previousStep() {
    this.step--;
  }

  public stepsString: string[] = Object.values(Registersteps)
    .splice(0, Object.keys(Registersteps).length / 2)
    .map((value) => {
      return value.toString();
    });

  public getPreviousStep(): string {
    if (this.step > 0)
      return this.insertSpaces(this.stepsString[this.step - 1]);

    return '';
  }

  public getNextStep(): string {
    if (this.step < this.stepsString.length)
      return this.insertSpaces(this.stepsString[this.step + 1]);
    return '';
  }

  public goToStep(step: number) {
    this.step = step;
  }
}
