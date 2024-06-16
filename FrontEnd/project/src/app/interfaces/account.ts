import { Role } from '../enums/role';
import { AccountInformation } from './account-information';

export interface Account {
  id: number;
  email: string;
  role: Role;
  accountInformation: AccountInformation;
  customerCode: string;
}
