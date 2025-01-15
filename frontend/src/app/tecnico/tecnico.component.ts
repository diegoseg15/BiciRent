import { Component, OnInit } from '@angular/core';
import { Incident, IncidentService } from '../services/incident.service';

// Angular Material Modules
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { RouterModule, Router } from '@angular/router';

@Component({
  host: { ngSkipHydration: "true" },
  selector: 'app-tecnico',
  standalone: true,
  imports: [
    MatCardModule,
    MatGridListModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    RouterModule
  ],
  templateUrl: './tecnico.component.html',
  styleUrl: './tecnico.component.css'
})
export class TecnicoComponent implements OnInit {
  incidents: Incident[] = [];
  loading: boolean = true;
  error: string = '';

  constructor(private incidentService: IncidentService, private router: Router) {}

  ngOnInit(): void {
    this.loadIncidents();
  }

  // Cargar las incidencias desde el backend
  loadIncidents(): void {
    this.loading = true;
    this.incidentService.getIncidents().subscribe({
      next: (data) => {
        this.incidents = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Error cargando las incidencias.';
        this.loading = false;
      },
    });
  }

  // Validar transiciones de estado
  private isValidTransition(currentState: string, newState: string): boolean {
    const validTransitions: { [key: string]: string[] } = {
      asignada: ['en proceso'],
      'en proceso': ['Esperando repuestos', 'completada'],
      'Esperando repuestos': ['en proceso', 'completada'], // Permitir transición directa a "completada"
    };
    return validTransitions[currentState]?.includes(newState) || false;
  }
  

  // Cambiar el estado de una incidencia
  changeIncidentState(incidentId: string, newState: string): void {
    const incident = this.incidents.find((inc) => inc.id === incidentId);
    if (!incident) {
      alert('Incidencia no encontrada');
      return;
    }

    if (!this.isValidTransition(incident.estado, newState)) {
      alert('Transición de estado no válida.');
      return;
    }

    this.incidentService.updateIncidentState(incidentId, newState).subscribe({
      next: () => {
        alert(`Estado actualizado a "${newState}"`);
        this.loadIncidents();
        if (newState === 'completada') {
          this.router.navigate(['/detalles-reparacion', incidentId]);
        }
      },
      error: () => {
        alert(`Error actualizando el estado a "${newState}".`);
      },
    });
  }

  // Métodos para facilitar el cambio de estado desde el template
  updateState(incidentId: string): void {
    this.changeIncidentState(incidentId, 'en proceso');
  }

  updateToWaitingParts(incidentId: string): void {
    this.changeIncidentState(incidentId, 'Esperando repuestos');
  }

  completeIncident(incidentId: string): void {
    this.changeIncidentState(incidentId, 'completada');
  }
}
