import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Login } from '../compartido/login';
import { Observable } from 'rxjs';
import { baseURL } from '../compartido/baseurl'; 
import {HttpHeaders} from '@angular/common/http';


const httpOptions= {
  headers: new HttpHeaders({
  'Content-Type': 'application/x-www-form-urlencoded',
  // 'Authorization': 'my-auth-token'
  })
  };

  @Injectable({
    providedIn: 'root'
  })
export class UsuarioService {

  constructor(private http: HttpClient) { }

  setLogin(user:Login): Observable<Login>{
    console.log(this.http.post<Login>(baseURL + 'api/login', "correo="+user.correo+"&password="+user.password, httpOptions));
    
    return this.http.post<Login>(baseURL + 'api/login', "correo="+user.correo+"&password="+user.password, httpOptions);
  }
}
