import { Injectable } from '@angular/core';

import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';


import { Empleado } from '../compartido/empleado';
import { EMPLEADOS } from '../compartido/empleados';


@Injectable({
  providedIn: 'root'
})
export class EmpleadoService {

  constructor() { }

  
  getEmpleados(): Observable<Empleado[]>{
    return of(EMPLEADOS).pipe(delay(1000));
      

  }

}
