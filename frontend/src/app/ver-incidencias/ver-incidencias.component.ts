import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Incidencia } from '../compartido/incidencia.model';
import { baseURL } from '../compartido/baseurl';
import { TecnicoService } from '../services/tecnico.service';
import { Router } from '@angular/router';
import { CookieService } from '../services/cookie.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CabeceraComponent } from "../cabecera/cabecera.component";


@Component({
  selector: 'app-ver-incidencias',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, CabeceraComponent],
  templateUrl: './ver-incidencias.component.html',
  styleUrls: ['./ver-incidencias.component.scss'],
})
export class VerIncidenciasComponent implements OnInit {
  incidencias: Incidencia[] = [];
  incidenciasAsignadas: Incidencia[] = [];
  tecnicos: string[] = [];
  asignarForm: FormGroup;
  incidenciaAsignada = false;

  constructor(
    private http: HttpClient,
    private fb: FormBuilder,
    private tecnicoService: TecnicoService,
    private cookieService: CookieService,
    private router: Router, // Para redirigir en caso de que no tenga acceso
    private snackBar: MatSnackBar,

  ) {
    // Inicializar el formulario vacío
    this.asignarForm = this.fb.group({});
  }

  ngOnInit(): void {
    // Verificar el rol del usuario
    const user = this.cookieService.getObject('user') as { rol: string } | null; // Obtener el objeto del usuario desde la cookie
    
    if (!user || user['rol'] !== 'personalMantenimiento') {
      this.snackBar.open('No tienes permiso para ver esa página', 'Cerrar', {
        duration: 3000,
      });

      this.router.navigate(['/']); // Redirigir al login si no tiene el rol adecuado
      return;
    }

    // Cargar los datos si el usuario tiene acceso
    this.cargarIncidencias();
    this.cargarIncidenciasAsignadas();
    this.cargarTecnicos();
  }

  getControl(incidenciaId: string): FormControl {
    return (this.asignarForm.get(incidenciaId) as FormControl) || new FormControl('');
  }

  cargarIncidencias() {
    this.http.get<Incidencia[]>(`${baseURL}api/incidencias?estado=reportada`).subscribe({
      next: (data) => {
        this.incidencias = data;

        // Crear controles dinámicos para cada incidencia
        data.forEach((incidencia) => {
          if (!this.asignarForm.contains(incidencia.id)) {
            this.asignarForm.addControl(
              incidencia.id,
              this.fb.control('', Validators.required)
            );
          }
        });
      },
      error: (err) => console.error('Error al cargar incidencias sin asignar:', err),
    });
  }

  cargarIncidenciasAsignadas() {
    this.http.get<Incidencia[]>(`${baseURL}api/incidencias?estado=asignada`).subscribe({
      next: (data) => (this.incidenciasAsignadas = data),
      error: (err) => console.error('Error al cargar incidencias asignadas:', err),
    });
  }

  cargarTecnicos() {
    this.tecnicoService.obtenerTecnicos().subscribe({
      next: (data: string[]) => (this.tecnicos = data),
      error: (err) => console.error('Error al cargar técnicos:', err),
    });
  }

  asignarTecnico(incidenciaId: string) {
    const tecnico = this.asignarForm.get(incidenciaId)?.value;
  
    if (tecnico) {
      const body = { id: incidenciaId, tecnico };
      console.log('Enviando solicitud PUT:', body); // Verifica la estructura del body
  
      this.http.put(`${baseURL}api/incidencias`, body, {
        headers: { 'Content-Type': 'application/json' },
      }).subscribe({
      next: () => {
        this.incidenciaAsignada = true;
        this.cargarIncidencias();
        this.cargarIncidenciasAsignadas();
        setTimeout(() => (this.incidenciaAsignada = false), 2000); // Ocultar mensaje tras 2s
      },
      error: (err) => {
        console.error('Error al asignar la incidencia:', err);
        this.snackBar.open('Error al asignar la incidencia. Por favor, inténtalo de nuevo.', 'Cerrar', {
          duration: 3000,
        });
      }
    });
  } else {
    this.snackBar.open('Por favor selecciona un técnico', 'Cerrar', {
      duration: 3000,
    });  }
}
  
  
}
