import { Component, OnInit } from '@angular/core';
import { initFlowbite } from 'flowbite';

@Component({
  selector: 'app-navbar-routing-layout',
  templateUrl: './navbar-routing-layout.component.html',
  styleUrls: ['./navbar-routing-layout.component.css']
})
export class NavbarRoutingLayoutComponent implements OnInit {
  ngOnInit(): void {
    initFlowbite();
  }
}
