import { Component } from '@angular/core';
import { OrderDetailsProduct } from '../interfaces/OrderDetailsProduct';
import { OrderrequestsService } from '../services/orderrequests.service';
import { OrderRequest } from '../interfaces/OrderRequest';
import { Account } from '../interfaces/account';
import { Role } from '../enums/role';
import { AdminpageService } from '../services/adminpage.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-newrequest',
  templateUrl: './newrequest.component.html',
  styleUrls: ['./newrequest.component.css'],
  host: {
    '(document:keydown)': 'handleKeyboardEvent($event)',
  },
})
export class NewrequestComponent {
  products: OrderDetailsProduct[] = [];
  private showModal: boolean = false;
  private showConfirmation: boolean = false;
  transportType: string = 'IMPORT';
  containerType: string = 'OT';
  containerSize: string = '20';
  companyCode: string = '';
  cargoType: string = 'CONTAINER';
  companyCodes: string[] = [];
  public customerCodeError: string = '';

  public currentUser: Account = JSON.parse(
    localStorage.getItem('currentUser')!
  );

  constructor(
    private orderRequestsService: OrderrequestsService,
    private adminService: AdminpageService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    if (this.currentUser.role === Role.CUSTOMER)
      this.companyCode = this.currentUser.customerCode;

    if (this.currentUser.role === Role.ADMINISTRATOR) this.getAllCompanyCodes();
  }

  handleKeyboardEvent(event: KeyboardEvent) {
    if (this.showModalValue === true && event.key === 'Escape') {
      this.toggleModal();
    }
    if (this.showConfirmation === true && event.key === 'Escape') {
      this.toggleConfirmation();
    }
  }

  getAllCompanyCodes(): void {
    this.adminService.getAllCodes().subscribe((codes: any) => {
      this.companyCodes = codes.map((code: any) => code.toString());
    });
  }

  public toggleModal(): void {
    this.showModal = !this.showModal;
  }

  get showModalValue(): boolean {
    return this.showModal;
  }
  set showModalValue(value: boolean) {
    this.showModal = value;
  }

  public toggleConfirmation(): void {
    if (!this.showConfirmation && this.companyCode === '') {
      this.customerCodeError = 'Customer code cannot be empty.';
      return;
    }
    this.customerCodeError = '';
    this.showConfirmation = !this.showConfirmation;
  }

  get showConfirmationValue(): boolean {
    return this.showConfirmation;
  }
  set showConfirmationValue(value: boolean) {
    this.showConfirmation = value;
  }

  public get Role(): typeof Role {
    return Role;
  }

  addProduct(
    hsCode: string,
    name: string,
    quantity: string,
    weight: string,
    containerNr: string,
    containerSize: string,
    containerType: string
  ): void {
    if (name === '') {
      return alert('Naam is verplicht.');
    }

    this.products.push({
      id: '',
      hsCode: hsCode,
      name: name,
      quantity: +quantity,
      weight: +weight,
      containerNumber: containerNr === '' ? null : containerNr,
      containerSize: +containerSize,
      containerType:
        this.cargoType === 'BULK' && containerType === ''
          ? null
          : containerType,
    });
    this.toggleModal();
  }

  removeProduct(i: number): void {
    this.products.splice(i, 1);
  }

  submitProduct(
    customerCode: string,
    transportType: string,
    customerReferenceNumber: string,
    portCode: string,
    cargoType: string,
    products: OrderDetailsProduct[]
  ) {
    if (
      transportType === '' ||
      customerReferenceNumber === '' ||
      portCode === '' ||
      cargoType === ''
    ) {
      return alert('Please fill in all fields.');
    }

    if (this.currentUser.role === Role.CUSTOMER)
      customerCode = this.currentUser.customerCode;

    this.orderRequestsService
      .createNewOrderRequest({
        id: '',
        customerCode: customerCode,
        transportType: transportType,
        customerReferenceNumber: customerReferenceNumber,
        portCode: portCode,
        cargoType: cargoType,
        products: products,
      })
      .subscribe({
        complete: () => {
          window.location.href = '/requests';
        },
        error: (error) => {
          if (error.status == 401) {
            return alert(
              'An OrderRequest with this CustomerReferenceNumber already exists. Please try a different CustomerReferenceNumber.'
            );
          } else {
            return alert('An error occurred. Please try again later.');
          }
        },
      });
  }

  onCargoTypeChange(cargoType: string): void {
    cargoType === 'CONTAINER'
      ? (this.containerType = 'OT')
      : (this.containerType = 'null');
  }
}
