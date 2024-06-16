import { AccountInformationBase } from './account-information-base';
import { Country } from './country';

export interface AccountInformation extends AccountInformationBase {
  country: Country;
}
