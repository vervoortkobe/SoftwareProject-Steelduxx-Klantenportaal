import { Component, OnInit } from '@angular/core';
import { AdminpageService } from '../services/adminpage.service';
import { AdminOrder } from '../interfaces/AdminOrder';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-admincompanycodes',
  templateUrl: './admincompanycodes.component.html',
  styleUrls: ['./admincompanycodes.component.css']
})
export class AdmincompanycodesComponent implements OnInit {
  companyCodes: string[] = [];
  orders: AdminOrder[] = [];
  filteredOrders: AdminOrder[] = [];
  selectedCustomerCode: string | undefined;
  pageSizeCompanyCodes = 10;
  pageSizeOrders = 10;
  currentPageCompanyCodes = 0;
  currentPageOrders = 0;

  constructor(private adminService: AdminpageService) { }

  ngOnInit(): void {
    this.getAllCompanyCodes();
    this.getAllOrders();
  }

  getAllCompanyCodes(): void {
    this.adminService.getAllCodes()
      .subscribe((codes: any) => {
        this.companyCodes = codes.map((code: any) => code.toString());
      });
  }

  getAllOrders(): void {
    this.adminService.getAllOrders()
      .subscribe((orders: AdminOrder[]) => {
        this.orders = orders;
        this.filteredOrders = orders;
      });
  }

  getOrdersByCustomerCode(customerCode: string): void {
    this.selectedCustomerCode = customerCode;
    this.filteredOrders = this.orders.filter(order => order.customerCode === customerCode);
  }

  onPageChangeCompanyCodes(event: PageEvent): void {
    this.pageSizeCompanyCodes = event.pageSize;
    this.currentPageCompanyCodes = event.pageIndex;
  }

  onPageChangeOrders(event: PageEvent): void {
    this.pageSizeOrders = event.pageSize;
    this.currentPageOrders = event.pageIndex;
  }

  get pagedCompanyCodes(): string[] {
    const startIndex = this.currentPageCompanyCodes * this.pageSizeCompanyCodes;
    const endIndex = startIndex + this.pageSizeCompanyCodes;
    return this.companyCodes.slice(startIndex, endIndex);
  }

  get pagedFilteredOrders(): AdminOrder[] {
    const startIndex = this.currentPageOrders * this.pageSizeOrders;
    const endIndex = startIndex + this.pageSizeOrders;
    return this.filteredOrders.slice(startIndex, endIndex);
  }
}
