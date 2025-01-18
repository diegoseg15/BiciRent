import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IncidentService } from '../services/incident.service';

import { FormsModule } from '@angular/forms'; // Para el Two-Way Binding
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';



@Component({
  selector: 'app-detalles-reparacion',
  standalone: true,
  imports: [ FormsModule, // Permite usar [(ngModel)]
    MatButtonModule,
    MatInputModule,
    MatCardModule,],
  templateUrl: './detalles-reparacion.component.html',
  styleUrl: './detalles-reparacion.component.css'
})

export class DetallesReparacionComponent implements OnInit {
  incidentId: string = '';
  acciones: string = '';
  piezas: string = '';
  detalles: string = '';

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private incidentService: IncidentService
  ) {}

  ngOnInit(): void {
    // Obtener el ID de la incidencia de la URL
    this.incidentId = this.route.snapshot.paramMap.get('id') || '';
  }

  registrarDetalles(): void {
    this.incidentService
      .registerIncidentDetails(this.incidentId, this.acciones, this.piezas, this.detalles)
      .subscribe({
        next: () => {
          alert('Detalles registrados exitosamente');
          this.router.navigate(['/tecnico']); // Redirigir a la lista de incidencias
        },
        error: () => {
          alert('Error al registrar los detalles');
        }
      });
  }
}

