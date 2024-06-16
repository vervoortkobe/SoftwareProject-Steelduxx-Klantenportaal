export interface AdminOrder {
  customerCode: string;
  referenceNumber: string;
  customerReference: string;
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
  eta: string;
  ata: string;
  cargoType: string | null;
  totalWeight: number;
  totalContainers: number;
}
