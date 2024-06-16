import { Account } from './account';

export interface AccountApproval extends Account {
  approved: boolean;
  //Not on server side
  collapsed: boolean;
  customerCodeError: string;
}
