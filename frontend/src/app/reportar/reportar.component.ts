import { Component, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';
import { Router } from '@angular/router';
import { ReportarService } from '../services/reportar.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CookieService } from '../services/cookie.service';

@Component({
  selector: 'app-reportar',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatGridListModule,
    CommonModule,
  ],
  templateUrl: './reportar.component.html',
  styleUrls: ['./reportar.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ReportarComponent {
  intentoEnvio = false; // Flag para controlar los mensajes de error
  maxFecha: string = ''; // Fecha máxima para el campo de fecha
  incidenciaEnviada = false; // Flag para mostrar mensaje de incidencia enviada

  reportForm!: FormGroup;
  erroresForm: any = {
    fecha: '',
    hora: '',
    ubicacion: '',
    descripcion: '',
  };

  mensajesError: any = {
    fecha: {
      required: 'La fecha es obligatoria.',
    },
    hora: {
      required: 'La hora es obligatoria.',
    },
    ubicacion: {
      required: 'La ubicación es obligatoria.',
    },
    descripcion: {
      required: 'La descripción es obligatoria.',
    },
  };

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private reportarService: ReportarService,
    private snackBar: MatSnackBar,
    private cookieService: CookieService // Inyecta el servicio de cookies
  ) {
    this.crearFormulario();
    this.setFechaMaxima();
  }

  // Método para establecer la fecha máxima
  setFechaMaxima() {
    const hoy = new Date();
    const dia = hoy.getDate().toString().padStart(2, '0'); // Formato dd
    const mes = (hoy.getMonth() + 1).toString().padStart(2, '0'); // Formato mm
    const año = hoy.getFullYear();
    this.maxFecha = `${año}-${mes}-${dia}`; // Formato: yyyy-mm-dd
  }

  crearFormulario() {
    this.reportForm = this.fb.group({
      fecha: ['', Validators.required],
      hora: ['', Validators.required],
      ubicacion: ['', Validators.required],
      descripcion: ['', Validators.required],
    });

    this.reportForm.valueChanges.subscribe((datos) => this.onCambioValor(datos));
    this.onCambioValor();
  }

  onSubmit() {
    this.intentoEnvio = true; // Bandera para mostrar errores
    this.reportForm.markAllAsTouched();
    this.onCambioValor();
  
    if (this.reportForm.valid) {
      // Obtener los datos de la cookie
      const renta = this.cookieService.getObject('renta') as { dni: string, bicicleta: string } | null;
  
      if (!renta) {
        console.error('No se encontraron datos de renta en las cookies.');
        this.snackBar.open('No se encontraron datos de renta. Por favor, inténtalo de nuevo.', 'Cerrar', {
          duration: 3000,
        });
        return;
      }
  
      // Construir el objeto incidencia
      const incidencia = {
        fecha: this.reportForm.get('fecha')?.value,
        hora: this.reportForm.get('hora')?.value,
        ubicacion: this.reportForm.get('ubicacion')?.value,
        descripcion: this.reportForm.get('descripcion')?.value,
        dni: renta.dni, // Agregar dni desde la cookie
        bicicleta: renta.bicicleta, // Agregar bicicleta desde la cookie
      };
  
      console.log('Reporte enviado:', incidencia);
  
      // Enviar la incidencia al backend
      this.reportarService.reportarIncidencia(incidencia).subscribe({
        next: (res) => {
          console.log('Incidencia enviada:', res);
          this.incidenciaEnviada = true;
          this.reportForm.reset();
          this.intentoEnvio = false; // Reset de la bandera
          this.onCambioValor();
          setTimeout(() => (this.incidenciaEnviada = false), 3500); // Ocultar mensaje tras 3s
        },
        error: (err) => {
          console.error('Error al enviar la incidencia:', err);
          this.snackBar.open('Error al enviar la incidencia. Por favor, inténtalo de nuevo.', 'Cerrar', {
            duration: 3000,
          });
        },
      });
    } else {
      console.error('Formulario inválido');
    }
  }
  

  onCambioValor(data?: any) {
    if (!this.reportForm) return;

    const form = this.reportForm;
    for (const field in this.erroresForm) {
      this.erroresForm[field] = ''; // Limpia mensajes previos
      const control = form.get(field);
      if (control && !control.valid && this.intentoEnvio) {
        // Solo muestra errores si se ha intentado enviar el formulario
        const messages = this.mensajesError[field];
        for (const key in control.errors) {
          this.erroresForm[field] += messages[key] + ' ';
        }
      }
    }
  }
}
