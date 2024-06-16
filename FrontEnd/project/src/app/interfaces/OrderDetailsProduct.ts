import { ContainerType } from '../enums/containertype';

export interface OrderDetailsProduct {
  id: string;
  hsCode: string | null;
  name: string | null;
  quantity: number | null;
  weight: number;
  containerNumber: string | null;
  containerSize: number | null;
  containerType: string | null;
}
