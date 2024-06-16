import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Country } from '../interfaces/country';

@Injectable({
  providedIn: 'root',
})
export class CountryService {
  readonly endpoint: string = environment.API_URL + '/countries';

  constructor(private http: HttpClient) {}

  public getCountries(): Observable<Country[]> {
    return this.http.get<Country[]>(this.endpoint);
  }
}
