import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderrequestsService } from '../services/orderrequests.service';
import { OrderRequest } from '../interfaces/OrderRequest';
import { Account } from '../interfaces/account';
import { AuthService } from '../services/auth.service';
import { OrderDetailsProduct } from '../interfaces/OrderDetailsProduct';
import { ContainerType } from '../enums/containertype';
import { Role } from '../enums/role';
import { AdminpageService } from '../services/adminpage.service';

@Component({
  selector: 'app-orderrequestdetails',
  templateUrl: './orderrequestdetails.component.html',
  styleUrls: ['./orderrequestdetails.component.css'],
  host: {
    '(document:keydown)': 'handleKeyboardEvent($event)',
  },
})
export class OrderrequestdetailsComponent implements OnInit {
  private orderRequestData: any = {};
  public edit: boolean = false;
  private saved: boolean = false;
  public accountInfo: any = {};
  products: OrderDetailsProduct[] = [];
  private showModal: boolean = false;
  private showConfirmation: boolean = false;
  id: string = '';
  transportType: string = '';
  customerReferenceNumber: string = '';
  portCode: string = '';
  cargoType: string = '';
  containerType: string = 'OT';
  containerSize: string = '20';
  customerCode: string = '';
  companyCodes: string[] = [];
  isDisabled: boolean = true;

  public containerTypes: string[] = Object.keys(ContainerType)
    .filter(
      (key) =>
        typeof ContainerType[key as keyof typeof ContainerType] === 'string'
    )
    .map(
      (key) =>
        ContainerType[key as keyof typeof ContainerType] as unknown as string
    );

  constructor(
    private activatedRoute: ActivatedRoute,
    private orderRequestsService: OrderrequestsService,
    private authService: AuthService,
    private adminService: AdminpageService
  ) {}

  public currentUser: Account = JSON.parse(
    localStorage.getItem('currentUser')!
  );

  loadOrderRequestDetails(id: string): void {
    this.orderRequestsService
      .getOrderRequestById(id)
      .subscribe((responseData: OrderRequest) => {
        this.id = responseData.id;
        this.orderRequestData = responseData;
        this.transportType = this.orderRequestData.transportType;
        this.customerReferenceNumber =
          this.orderRequestData.customerReferenceNumber;
        this.portCode = this.orderRequestData.portCode;
        this.cargoType = this.orderRequestData.cargoType;
        this.products = this.orderRequestData.products;
        this.customerReferenceNumber = responseData.customerReferenceNumber;
        this.customerCode = responseData.customerCode;
      });
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params) => {
      let id = params.get('id')!;
      this.loadOrderRequestDetails(id);
    });

    this.authService.getAccountInfo().subscribe((accountData: Account) => {
      this.accountInfo = accountData;
    });
  }

  toggleEdit(): void {
    this.edit = !this.edit;
  }

  toggleSave(): void {
    this.saved = !this.saved;
  }

  get orderRequest() {
    return this.orderRequestData;
  }

  get editValue() {
    return this.orderRequestData;
  }

  public get Role(): typeof Role {
    return Role;
  }

  getAllCompanyCodes(): void {
    this.adminService.getAllCodes().subscribe((codes: any) => {
      this.companyCodes = codes.map((code: any) => code.toString());
    });
  }

  approve(id: string) {
    this.orderRequestsService.approveOrderRequest(id).subscribe({
      complete: () => {
        window.location.href = '/requests';
      },
    });
  }

  deny(id: string) {
    this.orderRequestsService.denyOrderRequest(id).subscribe({
      complete: () => {
        window.location.href = '/requests';
      },
    });
  }

  saveEdit(
    id: string,
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
      this.cargoType === ''
    ) {
      return alert('Please fill in all fields.');
    }

    this.orderRequestsService
      .overwriteOrderRequest({
        id: id,
        customerCode: customerCode,
        transportType: transportType,
        customerReferenceNumber: customerReferenceNumber,
        portCode: portCode,
        cargoType: cargoType,
        products: products,
      })
      .subscribe({
        complete: () => {
          this.toggleEdit();
          window.location.href = '/requests/' + id;
        },
        error: () => {
          return alert(
            'An OrderRequest with this CustomerReferenceNumber already exists. Please try a different CustomerReferenceNumber.'
          );
        },
      });
  }

  handleKeyboardEvent(event: KeyboardEvent) {
    if (this.showModalValue === true && event.key === 'Escape') {
      this.toggleModal();
    }
    if (this.showConfirmation === true && event.key === 'Escape') {
      this.toggleConfirmation();
    }
  }

  onTransportTypeChange(): void {
    this.isDisabled = this.transportType === 'IMPORT';
  }

  onCustomerReferenceNumberChange(customerReferenceNumber: string): void {
    this.customerReferenceNumber = customerReferenceNumber;
  }

  onPortCodeChange(portCode: string): void {
    this.portCode = portCode;
  }

  onCargoTypeChange(cargoType: string): void {
    this.cargoType = cargoType;
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
    this.showConfirmation = !this.showConfirmation;
  }

  get showConfirmationValue(): boolean {
    return this.showConfirmation;
  }
  set showConfirmationValue(value: boolean) {
    this.showConfirmation = value;
  }

  public addProduct(
    hsCode: string,
    name: string,
    quantity: string,
    weight: string,
    containerNr: string,
    containerSize: string,
    containerType: string
  ): void {
    if (name === '') return alert('The name field is required.');

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
    console.log(this.products);
  }

  public removeProduct(id: string): void {
    this.products = this.products.filter((product) => product.id !== id);
  }

  public onProductDetailChange(
    i: number,
    property: keyof OrderDetailsProduct,
    event: Event
  ): void {
    const value = (event.target as HTMLInputElement).value;

    switch (property) {
      case 'hsCode':
        this.products[i].hsCode = value;
        break;
      case 'name':
        this.products[i].name = value;
        break;
      case 'quantity':
        this.products[i].quantity = value ? +value : null;
        break;
      case 'weight':
        this.products[i].weight = value ? +value : 0;
        break;
      case 'containerNumber':
        this.products[i].containerNumber = value;
        break;
      case 'containerSize':
        this.products[i].containerSize = value ? +value : 20;
        break;
      case 'containerType':
        this.products[i].containerType = value === '' ? null : value;
        break;

      default:
        break;
    }

    console.log(this.products[i]);
  }
}
