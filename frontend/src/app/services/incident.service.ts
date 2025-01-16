import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CookieService } from './cookie.service'; // Importa el servicio de cookies

export interface Incident {
  id: string;
  descripcion: string;
  ubicacion: string;
  situacion: string;
  estado: string;
  tecnico: string;
}

@Injectable({
  providedIn: 'root',
})
export class IncidentService {
  private baseUrl = 'http://localhost:8080/twcam-pls-plcv-mdps-spa/api/incidents';

  constructor(private http: HttpClient,  private cookieService: CookieService) {}

  // Obtener todas las incidencias
  getIncidents(): Observable<Incident[]> {
    return this.http.get<Incident[]>(this.baseUrl);
  }

  // Actualizar el estado de una incidencia
  updateIncidentState(id: string, estado: string): Observable<any> {
    return this.http.put(this.baseUrl, null, {
      params: { id, estado },
    });
  }

  // Método específico para cambiar el estado a "completada"
  updateIncidentToCompleted(id: string): Observable<any> {
    return this.updateIncidentState(id, 'completada');
  }

  // Registrar los detalles de una incidencia completada
  registerIncidentDetails(id: string, acciones: string, piezas: string, detalles: string): Observable<any> {
    return this.http.post(`http://localhost:8080/twcam-pls-plcv-mdps-spa/api/incidents/details`, null, {
      params: { id, acciones, piezas, detalles },
    });
  }

  // Enviar el campo dni al backend
  sendDniToBackend(): Observable<any> {
    const user = this.cookieService.getObject('user') as { dni?: string };

    if (!user || !user.dni) {
      throw new Error('No se encontró el campo DNI en la cookie.');
    }

    return this.http.post(`${this.baseUrl}/validate-dni`, { dni: user.dni });
  }

  
}
