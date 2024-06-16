import { Component, OnInit } from '@angular/core';
import { AdminOrder } from '../interfaces/AdminOrder';
import { AdminpageService } from '../services/adminpage.service';
import { PageEvent } from '@angular/material/paginator';
import { MatPaginatorModule } from '@angular/material/paginator';


@Component({
  selector: 'app-adminpage',
  templateUrl: './adminpage.component.html',
  styleUrls: ['./adminpage.component.css']
})
export class AdminpageComponent implements OnInit {
  orders: AdminOrder[] = [];
  pageSize = 10;
  pageIndex = 0;
  pagedData: AdminOrder[] = [];
  totalFilteredResults: number = 0;
  searchText = '';

  currentSortColumn: keyof AdminOrder = 'eta';
  isAscendingSort: boolean = false;

  constructor(private adminService: AdminpageService) { }

  ngOnInit(): void {
    this.getOrders();
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.updatePagedData();
  }

  getOrders(): void {
    this.adminService.getAllOrders()
      .subscribe((responseData: AdminOrder[]) => {

        this.orders = responseData.filter(order => order.state.toLowerCase() !== 'closed');;
        this.sortData(this.currentSortColumn, true);
        this.updatePagedData();
      });
  }

  get pagedOrders() {
    return this.pagedData;
  }

  updatePagedData(): void {
    let filteredData = [...this.orders];

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
    this.pageIndex = 0;

    this.pagedData = this.orders.filter((order) =>
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

  sortData(column: keyof AdminOrder, sortAllOrders: boolean = false): void {
    if (this.currentSortColumn !== column) {
      this.currentSortColumn = column;
      this.isAscendingSort = true;
    } else {
      this.isAscendingSort = !this.isAscendingSort;
    }

    this.orders.sort((a, b) => {
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
