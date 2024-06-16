import { Component } from '@angular/core';
import { ShippingData } from '../interfaces/ShippingData';
import { OrderpageService } from '../services/orderpage.service';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent {
  public data: ShippingData[] = [];
  pageSize = 10;
  pageIndex = 0;
  pagedData: any[] = [];
  searchText = '';
  selectedColumn: string = 'state';
  totalFilteredResults: number = 0;
  showClosedOrders: boolean = true;

  constructor(private orderpageService: OrderpageService) { }

  ngOnInit(): void {
    this.getAll();
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.updatePagedData();
  }

  getAll(): void {
    this.orderpageService
      .getAllOrders()
      .subscribe((responseData: ShippingData[]) => {
        this.data = responseData;
        this.updatePagedData();
      });
  }

  get pagedOrders() {
    return this.pagedData;
  }

  updatePagedData(): void {
    let filteredData = this.data.filter(order => order.state === 'CLOSED');

    if (this.searchText) {
      filteredData = filteredData.filter((order) =>
        Object.values(order).some(
          (value) =>
            (value &&
              typeof value == 'string' &&
              value.toLowerCase().includes(this.searchText.toLowerCase())) ||
            (typeof value == 'number' &&
              value.toString().includes(this.searchText))
        )
      );
    }

    this.totalFilteredResults = filteredData.length;

    const startIndex = this.pageIndex * this.pageSize;
    const endIndex = startIndex + this.pageSize;

    this.pagedData = filteredData.slice(startIndex, endIndex);
  }

  filterData(): void {
    if (!this.searchText) {
      this.getAll();
      return;
    }

    this.pageIndex = 0;

    let filteredData = this.data.filter(order => order.state === 'CLOSED');

    if (this.searchText) {
      filteredData = filteredData.filter((order) =>
        Object.values(order).some(
          (value) =>
            (value &&
              typeof value == 'string' &&
              value.toLowerCase().includes(this.searchText.toLowerCase())) ||
            (typeof value == 'number' &&
              value.toString().includes(this.searchText))
        )
      );
    }

    this.totalFilteredResults = filteredData.length;

    this.updatePagedData();
  }

  toggleClosedOrders(): void {
    this.showClosedOrders = !this.showClosedOrders;
    this.filterData();
  }

  currentSortColumn: string = 'state';
  isAscendingSort: boolean = true;

  sortData(column: keyof ShippingData, sortAllOrders: boolean = false): void {
    if (this.currentSortColumn !== column) {
      this.currentSortColumn = column;
      this.isAscendingSort = true;
    } else {
      this.isAscendingSort = !this.isAscendingSort;
    }

    this.data.sort((a, b) => {
      const aValue = a[column];
      const bValue = b[column];

      if (aValue == null || aValue === '') return this.isAscendingSort ? 1 : -1;
      if (bValue == null || bValue === '') return this.isAscendingSort ? -1 : 1;

      if (!isNaN(aValue as any) && !isNaN(bValue as any)) {
        return this.isAscendingSort ? (aValue as number) - (bValue as number) : (bValue as number) - (aValue as number);
      }

      const aDate = this.convertToDate(aValue as string);
      const bDate = this.convertToDate(bValue as string);

      if (aDate && bDate) {
        return this.isAscendingSort ? aDate.getTime() - bDate.getTime() : bDate.getTime() - aDate.getTime();
      } else {
        const compareValue = aValue
          .toString()
          .toLowerCase()
          .localeCompare(bValue.toString().toLowerCase());
        return this.isAscendingSort ? compareValue : -compareValue;
      }
    });

    this.updatePagedData();
  }

  convertToDate(dateString: string): Date | null {
    const parts = dateString.split('-');
    if (parts.length === 3) {
      const day = parseInt(parts[0], 10);
      const month = parseInt(parts[1], 10) - 1;
      const year = parseInt(parts[2], 10);
      return new Date(year, month, day);
    }
    return null;
  }

  get sortOrderLabel(): string {
    return this.isAscendingSort ? 'Ascending' : 'Descending';
  }
}
