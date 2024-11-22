import { Component, OnInit } from '@angular/core';


import { Empleado } from '../compartido/empleado';

import { EmpleadoService } from '../services/empleado.service';



@Component({
  selector: 'app-nosotros',
  templateUrl: './nosotros.component.html',
  styleUrls: ['./nosotros.component.scss']
})
export class NosotrosComponent implements OnInit {

  vEmpleados:Empleado[] = [];

  constructor(private empleadoService:EmpleadoService) { }

  ngOnInit(): void {
    this.empleadoService.getEmpleados().subscribe(empleados =>this.vEmpleados = empleados);
  }

}
