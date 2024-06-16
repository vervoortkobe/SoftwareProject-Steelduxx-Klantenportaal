import { AccountInformationCreation } from './account-information-creation';

export interface AccountCreation {
  email: string;
  password: string;
  accountInformation: AccountInformationCreation;
}
