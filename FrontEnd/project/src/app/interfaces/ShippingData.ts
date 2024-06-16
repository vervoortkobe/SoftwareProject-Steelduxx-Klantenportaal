export interface ShippingData {
  referenceNumber: string;
  customerReferenceNumber: string;
  state: string;
  transportType: string;
  portOfOriginCode: string;
  portOfOriginName: string;
  portOfDestinationCode: string;
  portOfDestinationName: string;
  shipName: string;
  shipMMSI: string;
  ets: string;
  ats: string;
  eta: string | null;
  ata: string | null;
  cargoType: string | null;
  totalWeight: number;
  totalContainers: number;
  containerTypes: string[];
}
