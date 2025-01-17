import { Injectable } from '@angular/core';
import { Renta } from '../compartido/renta';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { baseURL } from '../compartido/baseurl';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
  withCredentials: true
};

@Injectable({
  providedIn: 'root'
})
export class RentaService {

  constructor(private http: HttpClient) { }

  rentaBici(renta: Renta): Observable<any> {
    // Enviar el objeto del usuario directamente como JSON
    return this.http.post<any>(`${baseURL}api/rent`, renta, httpOptions);
  }
}
