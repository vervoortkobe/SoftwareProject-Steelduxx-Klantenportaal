import { OrderDetailsProduct } from './OrderDetailsProduct';

export interface OrderRequest {
  id: string;
  customerCode: string;
  transportType: string;
  customerReferenceNumber: string;
  portCode: string;
  cargoType: string;
  products: OrderDetailsProduct[];
}
