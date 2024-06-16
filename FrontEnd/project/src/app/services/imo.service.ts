import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class IMOService {
  imo:string='';

  setIMO(imo:string){
    this.imo = imo;
  }

  getIMO(){
    return this.imo;
  }
}