import { Injectable } from '@angular/core';
import Cookies from 'js-cookie';

@Injectable({
  providedIn: 'root' // Esto asegura que el servicio es globalmente accesible
})
export class CookieService {

  constructor() { }

  // Método para guardar un objeto en la cookie
  setObject(name: string, value: object, days: number): void {
    const valueString = JSON.stringify(value); // Convertir a cadena JSON
    Cookies.set(name, valueString, { expires: days });
  }

  // Método para obtener un objeto de la cookie
  getObject(name: string): object | undefined {
    const value = Cookies.get(name);
    if (value) {
      return JSON.parse(value); // Convertir de cadena JSON a objeto
    }
    return undefined;
  }

  // Método para eliminar una cookie
  deleteCookie(name: string): void {
    Cookies.remove(name);
  }
}
