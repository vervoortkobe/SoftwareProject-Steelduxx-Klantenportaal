import { OrderDetailsProduct } from './OrderDetailsProduct';
export interface OrderDetails {
  referenceNumber: string;
  customerReferenceNumber: string;
  state: string;
  transportType: string;
  portOfOriginCode: string;
  portOfOriginName: string;
  portOfDestinationCode: string;
  portOfDestinationName: string;
  shipName: string;
  shipIMO: string;
  shipMMSI: string;
  shipType: string;
  ets: string;
  ats: string;
  eta: string;
  ata: string | null;
  preCarriage: string;
  estimatedTimeCargoOnQuay: string;
  actualTimeCargoLoaded: string;
  billOfLadingDownloadLink: string;
  packingListDownloadLink: string;
  customsDownloadLink: string;
  cargoType: string | null;
  products: OrderDetailsProduct[];
}
