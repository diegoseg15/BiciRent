import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CookieService } from './cookie.service'; // Importa el servicio de cookies para gestionar datos del usuario

// Interfaz que define la estructura de una incidencia
export interface Incident {
  id: string; // Identificador único de la incidencia
  descripcion: string; // Descripción de la incidencia
  ubicacion: string; // Ubicación donde ocurrió la incidencia
  situacion: string; // Nivel o situación de la incidencia (ej. alta, media, baja)
  estado: string; // Estado actual de la incidencia (ej. asignada, en proceso, completada)
  tecnico: string; // Técnico asignado a la incidencia
  bicicleta: string; // Bicicleta asignada a la incidencia
}

@Injectable({
  providedIn: 'root', // Proporciona el servicio a nivel global en la aplicación
})
export class IncidentService {
  private baseUrl = 'http://localhost:8080/twcam-pls-plcv-mdps-spa/api/incidents'; // URL base del backend para gestionar incidencias

  constructor(private http: HttpClient, private cookieService: CookieService) {}

  // Obtener todas las incidencias desde el backend
  getIncidents(): Observable<Incident[]> {
    // Realiza una solicitud GET a la URL base para obtener la lista de incidencias
    return this.http.get<Incident[]>(this.baseUrl);
  }

  // Actualizar el estado de una incidencia específica
  updateIncidentState(id: string, estado: string): Observable<any> {
    // Envía una solicitud PUT al backend con los parámetros "id" y "estado"
    return this.http.put(this.baseUrl, null, {
      params: { id, estado },
    });
  }

  // Método para cambiar el estado de una incidencia a "completada"
  updateIncidentToCompleted(id: string): Observable<any> {
    // Reutiliza el método `updateIncidentState` con el estado "completada"
    return this.updateIncidentState(id, 'completada');
  }

  // Registrar los detalles de una incidencia que ha sido completada
  registerIncidentDetails(id: string, acciones: string, piezas: string, detalles: string, bicicleta: string): Observable<any> {
    // Envía una solicitud POST al backend con los detalles de la reparación
    return this.http.post(`http://localhost:8080/twcam-pls-plcv-mdps-spa/api/incidents/details`, null, {
      params: { id, acciones, piezas, detalles, bicicleta },
    });
  }

  // Enviar el DNI del usuario al backend para validación
  sendDniToBackend(): Observable<any> {
    // Obtiene el usuario desde las cookies
    const user = this.cookieService.getObject('user') as { dni?: string };

    // Verifica si el campo DNI está disponible en la cookie
    if (!user || !user.dni) {
      throw new Error('No se encontró el campo DNI en la cookie.');
    }

    // Envía una solicitud POST con el DNI del usuario para validación
    return this.http.post(`${this.baseUrl}/validate-dni`, { dni: user.dni });
  }
}
