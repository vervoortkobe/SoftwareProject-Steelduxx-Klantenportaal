import { Component, OnInit } from '@angular/core';
import { OrderpageService } from '../services/orderpage.service';
import { PageEvent } from '@angular/material/paginator';
import { ShippingData } from '../interfaces/ShippingData';

@Component({
  selector: 'app-orderpage',
  templateUrl: './orderpage.component.html',
  styleUrls: ['./orderpage.component.css'],
})
export class OrderpageComponent implements OnInit {
  public data: ShippingData[] = [];
  pageSize = 10;
  pageIndex = 0;
  pagedData: any[] = [];
  searchText = '';
  selectedColumn: string = 'state';
  totalFilteredResults: number = 0;

  constructor(private orderpageService: OrderpageService) {}

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
        this.data = responseData.filter((order) => order.state !== 'CLOSED');
        this.sortData('eta', true);
        this.updatePagedData();
      });
  }

  get pagedOrders() {
    return this.pagedData;
  }

  updatePagedData(): void {
    let filteredData = [...this.data];

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

    filteredData.sort((a, b) => {
      const aValue = a[this.currentSortColumn as keyof ShippingData];
      const bValue = b[this.currentSortColumn as keyof ShippingData];

      if (aValue == null || bValue == null) {
        return 0;
      }

      if (typeof aValue == 'string' && typeof bValue == 'string') {
        const compareValue = aValue
          .toLowerCase()
          .localeCompare(bValue.toLowerCase());
        return this.isAscendingSort ? compareValue : -compareValue;
      } else {
        return this.isAscendingSort
          ? Number(aValue) - Number(bValue)
          : Number(bValue) - Number(aValue);
      }
    });

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

    this.pagedData = this.data.filter((order) =>
      Object.values(order).some(
        (value) =>
          (value &&
            typeof value == 'string' &&
            value.toLowerCase().includes(this.searchText.toLowerCase())) ||
          (typeof value == 'number' &&
            value.toString().includes(this.searchText))
      )
    );

    this.totalFilteredResults = this.pagedData.length;

    this.updatePagedData();
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
        return this.isAscendingSort
          ? (aValue as number) - (bValue as number)
          : (bValue as number) - (aValue as number);
      }

      const aDate = this.convertToDate(aValue as string);
      const bDate = this.convertToDate(bValue as string);

      if (aDate && bDate) {
        return this.isAscendingSort
          ? aDate.getTime() - bDate.getTime()
          : bDate.getTime() - aDate.getTime();
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
