import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { baseURL } from '../compartido/baseurl';

@Injectable({
  providedIn: 'root',
})
export class TecnicoService {
  constructor(private http: HttpClient) {}

  obtenerTecnicos(): Observable<string[]> {
    return this.http.get<string[]>(`${baseURL}api/tecnicos`);
  }
}
