import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { OrderRequest } from '../interfaces/OrderRequest';
import { OrderrequestsService } from '../services/orderrequests.service';
import { Account } from '../interfaces/account';
import { Role } from '../enums/role';

@Component({
  selector: 'app-orderrequests',
  templateUrl: './orderrequests.component.html',
  styleUrls: ['./orderrequests.component.css'],
})
export class OrderrequestsComponent implements OnInit {
  public data: OrderRequest[] = []; // Definieer een array van OrderRequests
  pageSize = 10;
  pageIndex = 0;
  pagedData: any[] = [];
  searchText = ''; // Tekst voor zoekopdracht
  selectedColumn: string = 'transportType'; // Geselecteerde kolom voor filtering
  totalFilteredResults: number = 0;

  constructor(private orderrequestsService: OrderrequestsService) {}

  public currentUser: Account = JSON.parse(
    localStorage.getItem('currentUser')!
  );

  public get Role(): typeof Role {
    return Role;
  }

  ngOnInit(): void {
    this.getAll();
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.updatePagedData();
  }

  getAll(): void {
    this.orderrequestsService
      .getAllOrderRequests()
      .subscribe((responseData: OrderRequest[]) => {
        this.data = responseData;
        this.updatePagedData();
      });
  }

  get pagedOrders() {
    return this.pagedData;
  }

  updatePagedData(): void {
    let filteredData = [...this.data]; // Maak een kopie van de gegevens

    // Filter de gegevens op basis van de zoektekst
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

    // Sorteer de gefilterde gegevens
    filteredData.sort((a, b) => {
      const aValue = a[this.currentSortColumn as keyof OrderRequest];
      const bValue = b[this.currentSortColumn as keyof OrderRequest];

      if (aValue == null || bValue == null) {
        return 0; // Behoud de volgorde als een van de waarden null is
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

    // Update het totale aantal gefilterde resultaten voor de paginering
    this.totalFilteredResults = filteredData.length;

    // Bereken de juiste pagina-index op basis van het aantal gefilterde resultaten per pagina
    const startIndex = this.pageIndex * this.pageSize;
    const endIndex = startIndex + this.pageSize;

    // Update de pagedData met de gefilterde en gesorteerde gegevens
    this.pagedData = filteredData.slice(startIndex, endIndex);
  }

  filterData(): void {
    if (!this.searchText) {
      this.getAll();
      return;
    }

    // Reset de pageIndex naar 0 bij het toepassen van een filter
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

    // Sorteer de gefilterde gegevens
    this.sortData(this.currentSortColumn);

    // Update totalFilteredResults
    this.totalFilteredResults = this.pagedData.length;

    // Update de paginering
    this.updatePagedData();
  }

  currentSortColumn: string = 'transportType';
  isAscendingSort: boolean = true;

  sortData(column: string): void {
    if (this.currentSortColumn !== column) {
      this.currentSortColumn = column;
      this.isAscendingSort = true;
    } else {
      this.isAscendingSort = !this.isAscendingSort;
    }

    this.pagedData.sort((a, b) => {
      const aValue = a[column];
      const bValue = b[column];

      // Behandel het geval waarin de waarde null is
      if (aValue == null || bValue == null) {
        return 0; // Behoud de volgorde als een van de waarden null is
      }

      // Controleer of de waarden numeriek zijn
      if (typeof aValue == 'number' && typeof bValue == 'number') {
        // Sorteer op numerieke waarden
        return this.isAscendingSort ? aValue - bValue : bValue - aValue;
      } else {
        // Sorteer op stringwaarden (ongewijzigd)
        const compareValue = aValue
          .toString()
          .toLowerCase()
          .localeCompare(bValue.toString().toLowerCase());
        return this.isAscendingSort ? compareValue : -compareValue;
      }
    });

    // Update de paginering
    this.updatePagedData();
  }
}
