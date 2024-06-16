import { AfterViewInit, Component } from '@angular/core';
import { IMOService } from '../services/imo.service';

@Component({
  selector: 'app-location',
  templateUrl: './location.component.html',
  styleUrls: ['./location.component.css'],
})
export class LocationComponent implements AfterViewInit {
  
  shipIMO: string ='9622629';
  link1: string = 'https://www.vesselfinder.com/aismap?width=100%25&height=300&names=true&imo=';
  link2: string = '&track=true&store_pos=true';

  constructor(private IMOservice:IMOService) {
  }

  ngAfterViewInit(): void {
    const iframe = document.getElementById('vesselfinder') as HTMLIFrameElement;
    this.shipIMO = this.IMOservice.getIMO();
    iframe.src = this.link1 + this.shipIMO + this.link2;
  }
}