import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderpageService } from '../services/orderpage.service';
import { OrderDetails } from '../interfaces/OrderDetails';
import { OrderState } from '../enums/order-state';
import { IMOService } from '../services/imo.service';
import { AdminpageService } from '../services/adminpage.service';
import { Account } from '../interfaces/account';
import { Role } from '../enums/role';

@Component({
  selector: 'app-orderdetails',
  templateUrl: './orderdetails.component.html',
  styleUrls: ['./orderdetails.component.css'],
})
export class OrderdetailsComponent implements OnInit {
  public orderId: string | any = null;
  public orderData: OrderDetails | any = null;
  public orderState: number = 0;
  imoNumber: string = '';

  constructor(
    private activatedRoute: ActivatedRoute,
    private orderpageService: OrderpageService,
    private adminPageService: AdminpageService,
    private IMOservice: IMOService
  ) {}

  loadOrderDetails(orderId: string): void {
    this.orderpageService
      .getOrderById(orderId)
      .subscribe((responseData: OrderDetails) => {
        this.orderId = responseData.referenceNumber;
        this.orderState = Number(
          Object.keys(OrderState)[
            Object.values(OrderState).indexOf(responseData.state)
          ]
        );
        this.orderData = responseData;
        this.IMOservice.setIMO(this.orderData.shipIMO);
      });
    console.log(this.orderData);
  }

  loadOrderDetailsAdmin(orderId: string, groupCode: string): void {
    this.adminPageService
      .getOrderById(groupCode, orderId)
      .subscribe((responseData: OrderDetails) => {
        this.orderId = responseData.referenceNumber;
        this.orderState = Number(
          Object.keys(OrderState)[
            Object.values(OrderState).indexOf(responseData.state)
          ]
        );
        this.orderData = responseData;
        this.IMOservice.setIMO(this.orderData.shipIMO);
      });
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params) => {
      let keyId = params.get('key')!;
      let orderId = params.get('id')!;

      let currentUser: Account = JSON.parse(
        localStorage.getItem('currentUser')!
      );

      if (currentUser.role === Role.ADMINISTRATOR)
        this.loadOrderDetailsAdmin(orderId, keyId);
      else this.loadOrderDetails(orderId);
    });
  }

  get order() {
    return this.orderData;
  }
}
