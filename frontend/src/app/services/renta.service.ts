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

  devolucionBici(renta: Renta): Observable<any> {
    // Enviar el objeto del usuario directamente como JSON
    return this.http.post<any>(`${baseURL}api/devolucion`, renta, httpOptions);
  }

  bikeList(estacion: string): Observable<any> {
    if (estacion) {
      return this.http.get<any>(`${baseURL}api/bikes?estacionId=${estacion}`, httpOptions);
    } else {
      return this.http.get<any>(`${baseURL}api/bikes`, httpOptions);
    }
  }

  rentasList(usuarioId: string): Observable<any> {
    return this.http.get<any>(`${baseURL}api/rents?usuarioId=${usuarioId}`, httpOptions);
  }

  stationsList(): Observable<any> {
    return this.http.get<any>(`${baseURL}api/stations`, httpOptions)
  }
}
