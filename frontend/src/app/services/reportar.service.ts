// Este servicio envía los datos de la incidencia al backend

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { baseURL } from '../compartido/baseurl';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
  withCredentials: true, 
};


@Injectable({
  providedIn: 'root'
})
export class ReportarService {

  constructor(private http: HttpClient) { }

  reportarIncidencia(incidencia: any): Observable<any> {
    return this.http.post<any>(baseURL + 'api/incidencias', incidencia, httpOptions);
  }
}
