import { Component } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { AccountApproval } from '../interfaces/account-approval';
import { ApprovalService } from '../services/approval.service';
import { Role } from '../enums/role';
import { Account } from '../interfaces/account';
import { AdminpageService } from '../services/adminpage.service';
import { ModalType } from '../enums/modal-type';
import { Modal, ModalOptions } from 'flowbite';

@Component({
  selector: 'app-account-requests',
  templateUrl: './account-requests.component.html',
  styleUrls: ['./account-requests.component.css'],
})
export class AccountRequestsComponent {
  public data: AccountApproval[] = []; // Definieer een array van OrderRequests
  public codes: String[] = [];
  public modalType: ModalType = ModalType.DELETION;
  public selectedAccount: AccountApproval | null = null;
  public modal: Modal | any = null;

  public currentUser: Account = JSON.parse(
    localStorage.getItem('currentUser')!
  );

  pageSize = 10;
  pageIndex = 0;
  pagedData: any[] = [];
  searchText = ''; // Tekst voor zoekopdracht
  selectedColumn: string = 'transportType'; // Geselecteerde kolom voor filtering
  totalFilteredResults: number = 0;

  public get Role(): typeof Role {
    return Role;
  }
  public get modalTypeEnum(): typeof ModalType {
    return ModalType;
  }
  role = Object.values(Role).splice(0, Object.keys(Role).length / 2);

  constructor(
    private approvalservice: ApprovalService,
    private adminpageService: AdminpageService
  ) {}

  ngOnInit(): void {
    // set the modal menu element
    const $targetEl = document.getElementById('popup-modal');
    // options with default values
    const options: ModalOptions = {
      placement: 'bottom-right',
      backdrop: 'dynamic',
      backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
      closable: true,
    };

    // instance options object
    const instanceOptions = {
      id: 'popup-modal',
      override: true,
    };

    this.modal = new Modal($targetEl, options, instanceOptions);

    this.adminpageService.getAllCodes().subscribe((codes) => {
      this.codes = codes;
    });
    this.getAll();
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.updatePagedData();
  }

  getAll(): void {
    this.approvalservice
      .getAllAccounts()
      .subscribe((responseData: AccountApproval[]) => {
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
      const aValue = a[this.currentSortColumn as keyof AccountApproval];
      const bValue = b[this.currentSortColumn as keyof AccountApproval];

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

  public changeApprovalFinal(account: AccountApproval) {
    account.approved = !account.approved;
    this.approvalservice
      .changeApproval(account.id, account.approved)
      .subscribe({
        error: (error) => {
          account.approved = !account.approved;
        },
      });
  }

  public changeApproval(account: AccountApproval) {
    if (
      account.customerCode !== undefined &&
      account.customerCode !== '' &&
      account.customerCode !== null
    ) {
      account.customerCodeError = '';

      this.selectedAccount = account;
      this.modalType = ModalType.APPROVAL_CHANGE;
      this.modal.show();
    } else {
      account.customerCodeError = 'Cannot be empty.';
    }
  }

  public toggleAccordion(AccountApproval: AccountApproval) {
    AccountApproval.collapsed = !AccountApproval.collapsed;
  }

  public deleteUserFinal(account: AccountApproval) {
    this.approvalservice.deleteAccount(account.id).subscribe({
      next: (response) => {
        this.data = this.data.filter((x) => x.id !== account.id);
        this.updatePagedData();
      },
    });
  }

  public deleteUser(account: AccountApproval) {
    this.selectedAccount = account;
    this.modalType = ModalType.DELETION;
    this.modal.show();
  }

  public changeRole(account: AccountApproval, role: any) {
    let savedRole: Role = account.role;
    this.approvalservice.changeRole(account.id, role.value).subscribe({
      next: (response) => {
        account.role = role.value;
      },
      error: (error) => {
        role.value = savedRole;
      },
    });
  }

  public changeCustomerCode(account: AccountApproval, code: any) {
    if (code.value) {
      let savedCustomerCode = account.customerCode;
      this.approvalservice.changeCode(account.id, code.value).subscribe({
        next: (response) => {
          account.customerCode = code.value;
          account.customerCodeError = '';
        },
        error: (error) => {
          code.value = savedCustomerCode;
        },
      });
    } else {
      code.value = account.customerCode;
      account.customerCodeError = 'Cannot be empty.';
    }
  }

  public hideModal() {
    this.modal.hide();
  }

  public modalClicked() {
    switch (this.modalType) {
      case ModalType.DELETION:
        this.deleteUserFinal(this.selectedAccount!);
        break;
      case ModalType.APPROVAL_CHANGE:
        this.changeApprovalFinal(this.selectedAccount!);
        break;
    }
    this.modal.hide();
  }
}
