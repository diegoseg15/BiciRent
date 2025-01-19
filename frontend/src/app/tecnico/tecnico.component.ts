import { Component, OnInit } from '@angular/core';
import { Incident, IncidentService } from '../services/incident.service';

// Angular Material Modules
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

import { RouterModule, Router } from '@angular/router';
import { CookieService } from '../services/cookie.service';

import { CommonModule } from '@angular/common';
import { CabeceraComponent } from "../cabecera/cabecera.component"; 

@Component({
  host: { ngSkipHydration: "true" }, // Evita rehidratación para compatibilidad SSR
  selector: 'app-tecnico',
  standalone: true, // Componente independiente, sin necesidad de un módulo
  imports: [
    MatCardModule, // Tarjetas para las incidencias
    MatGridListModule, // Diseño responsivo en grid para las tarjetas
    MatButtonModule, // Botones de acciones en cada incidencia
    MatProgressSpinnerModule, // Spinner para indicar carga de datos
    MatFormFieldModule, // Campo de selección para filtrar incidencias
    MatSelectModule, // Dropdown para seleccionar técnico
    RouterModule, // Navegación entre vistas
    CommonModule // Soporte para directivas como *ngFor y *ngIf
    ,
    CabeceraComponent
],
  templateUrl: './tecnico.component.html',
  styleUrl: './tecnico.component.css'
})
export class TecnicoComponent implements OnInit {
  incidents: Incident[] = []; // Lista completa de incidencias
  filteredIncidents: Incident[] = []; // Lista de incidencias filtradas por técnico
  loading: boolean = true; // Estado de carga de datos
  error: string = ''; // Mensaje de error si ocurre algún problema al cargar datos
  selectedTechnician: string = ''; // Técnico seleccionado para filtrar las incidencias

  constructor(
    private incidentService: IncidentService, // Servicio para obtener y actualizar incidencias
    private router: Router, // Navegación entre vistas
    private cookieService: CookieService // Servicio para gestionar cookies del usuario
  ) {}

  // Método que se ejecuta al inicializar el componente
  ngOnInit(): void {
    this.validateAccess(); // Verifica que el usuario tenga acceso como técnico
    this.loadIncidents(); // Carga las incidencias desde el backend
  }

  // Valida el acceso del usuario según el rol almacenado en las cookies
  validateAccess(): void {
    const user = this.cookieService.getObject('user') as { rol?: string };

    // Si el usuario no tiene el rol de técnico, redirigir al login
    if (!user || user.rol !== 'tecnico') {
      this.router.navigate(['/login']);
    }
  }

  // Carga las incidencias desde el backend y las inicializa
  loadIncidents(): void {
    this.loading = true; // Activa el estado de carga
    this.incidentService.getIncidents().subscribe({
      next: (data) => {
        this.incidents = data; // Almacena todas las incidencias
        this.filteredIncidents = data; // Inicializa el filtro con todas las incidencias
        this.loading = false; // Desactiva el estado de carga
      },
      error: () => {
        this.error = 'Error cargando las incidencias.'; // Mensaje de error
        this.loading = false;
      },
    });
  }

  // Filtra las incidencias según el técnico seleccionado
  filterByTechnician(): void {
    if (this.selectedTechnician) {
      // Filtrar incidencias por el técnico asignado
      this.filteredIncidents = this.incidents.filter(
        (incident) => incident.tecnico === this.selectedTechnician
      );
    } else {
      // Si no se selecciona un técnico, mostrar todas las incidencias
      this.filteredIncidents = [...this.incidents];
    }
  }

  // Verifica si la transición de estado es válida
  private isValidTransition(currentState: string, newState: string): boolean {
    const validTransitions: { [key: string]: string[] } = {
      asignada: ['en proceso'], // Transición válida desde "asignada"
      'en proceso': ['Esperando repuestos', 'completada'], // Transiciones válidas desde "en proceso"
      'Esperando repuestos': ['en proceso', 'completada'], // Transiciones válidas desde "Esperando repuestos"
    };
    return validTransitions[currentState]?.includes(newState) || false;
  }

  // Cambia el estado de una incidencia
  changeIncidentState(incidentId: string, newState: string): void {
    // Encuentra la incidencia por su ID
    const incident = this.incidents.find((inc) => inc.id === incidentId);
    if (!incident) {
      alert('Incidencia no encontrada'); // Muestra alerta si no existe la incidencia
      return;
    }

    // Valida si la transición de estado es válida
    if (!this.isValidTransition(incident.estado, newState)) {
      alert('Transición de estado no válida.'); // Alerta si la transición es inválida
      return;
    }

    // Actualiza el estado de la incidencia en el backend
    this.incidentService.updateIncidentState(incidentId, newState).subscribe({
      next: () => {
        alert(`Estado actualizado a "${newState}"`); // Notifica éxito
        this.loadIncidents(); // Recarga la lista de incidencias
       
      },
      error: () => {
        alert(`Error actualizando el estado a "${newState}".`); // Notifica error
      },
    });
  }

  // Atajo para marcar una incidencia como "en proceso"
  updateState(incidentId: string): void {
    this.changeIncidentState(incidentId, 'en proceso');
  }

  // Atajo para marcar una incidencia como "Esperando repuestos"
  updateToWaitingParts(incidentId: string): void {
    this.changeIncidentState(incidentId, 'Esperando repuestos');
  }

  // Atajo para marcar una incidencia como "completada"
  completeIncident(incidentId: string): void {
    this.changeIncidentState(incidentId, 'completada');
  }
}
