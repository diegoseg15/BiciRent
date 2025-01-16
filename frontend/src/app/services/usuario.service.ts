import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { baseURL } from '../compartido/baseurl';
import { HttpHeaders } from '@angular/common/http';
import { Usuario } from '../compartido/usuario';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
  withCredentials: true
};

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private http: HttpClient) {}

  setLogin(user: { correo: string; password: string }): Observable<any> {
    return this.http.post<any>(`${baseURL}api/login`, user, httpOptions);
  }

  registerUser(user: Usuario): Observable<any> {
    // Enviar el objeto del usuario directamente como JSON
    return this.http.post<any>(`${baseURL}api/register`, user, httpOptions);
  }
}
