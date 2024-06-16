import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { initFlowbite } from 'flowbite';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Steelduxx Track & Trace';

  ngOnInit(): void {
    initFlowbite();
  }

  constructor(private titleService: Title) {
    this.titleService.setTitle(this.title);
  }
}
