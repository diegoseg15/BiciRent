import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IncidentService } from '../services/incident.service';

import { FormsModule } from '@angular/forms'; // Soporte para formularios
import { MatButtonModule } from '@angular/material/button'; // Botones con Angular Material
import { MatInputModule } from '@angular/material/input'; // Campos de entrada de texto
import { MatCardModule } from '@angular/material/card'; // Tarjetas de Angular Material
import { MatFormFieldModule } from '@angular/material/form-field'; // Contenedor para formularios de Material Design

import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'; // Formularios reactivos
import { CommonModule } from '@angular/common';
import { CabeceraComponent } from "../cabecera/cabecera.component"; // Directivas básicas de Angular como *ngIf y *ngFor

@Component({
  selector: 'app-detalles-reparacion', // Selector del componente
  standalone: true, // El componente es independiente y no pertenece a un módulo específico
  imports: [
    MatButtonModule, // Botones
    MatInputModule, // Campos de texto
    MatCardModule, // Tarjetas para diseño visual
    MatFormFieldModule, // Contenedor de formularios
    FormsModule, // Soporte para formularios template-driven
    ReactiveFormsModule, // Soporte para formularios reactivos
    CommonModule // Soporte para directivas básicas como *ngIf y *ngFor
    ,
    CabeceraComponent
],
  templateUrl: './detalles-reparacion.component.html', // Ruta al archivo HTML del componente
  styleUrls: ['./detalles-reparacion.component.css'] // Ruta al archivo CSS del componente
})
export class DetallesReparacionComponent implements OnInit {
  detallesForm!: FormGroup; // Formulario reactivo para capturar los detalles de la reparación
  erroresForm: any = {
    acciones: '', // Mensaje de error para el campo "acciones"
    piezas: '', // Mensaje de error para el campo "piezas"
    detalles: '' // Mensaje de error para el campo "detalles"
  };

  mensajesError: any = {
    // Mensajes de error personalizados para cada campo
    acciones: {
      required: 'Las acciones realizadas son obligatorias.'
    },
    piezas: {
      required: 'La lista de piezas es obligatoria.'
    },
    detalles: {
      required: 'Los detalles adicionales son obligatorios.'
    }
  };

  constructor(
    private fb: FormBuilder, // Inyección del FormBuilder para crear formularios reactivos
    private route: ActivatedRoute, // Para acceder a los parámetros de la ruta
    public router: Router, // Para navegar entre vistas
    private incidentService: IncidentService // Servicio para interactuar con el backend
  ) {}

  // Método que se ejecuta al inicializar el componente
  ngOnInit(): void {
    this.crearFormulario(); // Crea el formulario reactivo
    this.detallesForm.valueChanges.subscribe((datos) => this.onCambioValor(datos)); // Escucha los cambios en los campos
    this.onCambioValor(); // Inicializa los mensajes de error
  }

  // Método para crear y configurar el formulario reactivo
  crearFormulario(): void {
    this.detallesForm = this.fb.group({
      acciones: ['', Validators.required], // Campo obligatorio "acciones"
      piezas: ['', Validators.required], // Campo obligatorio "piezas"
      detalles: ['', Validators.required] // Campo obligatorio "detalles"
    });
  }

  // Método para manejar los cambios en el formulario y actualizar los mensajes de error
  onCambioValor(data?: any): void {
    if (!this.detallesForm) return;
    const form = this.detallesForm;

    // Itera sobre los campos del formulario para verificar su validez
    for (const field in this.erroresForm) {
      this.erroresForm[field] = ''; // Limpia los mensajes de error previos
      const control = form.get(field); // Obtiene el control del campo actual

      // Si el campo es inválido y fue modificado, agrega el mensaje de error correspondiente
      if (control && control.dirty && !control.valid) {
        const messages = this.mensajesError[field];
        for (const key in control.errors) {
          this.erroresForm[field] += messages[key] + ' ';
        }
      }
    }
  }

  // Método para registrar los detalles de la reparación
  registrarDetalles(): void {
    // Verifica si el formulario es inválido antes de enviar los datos
    if (this.detallesForm.invalid) {
      alert('El formulario contiene errores. Por favor, revisa los campos.');
      return;
    }

    // Obtiene los valores de los campos del formulario
    const { acciones, piezas, detalles } = this.detallesForm.value;

    // Obtiene el ID de la incidencia desde la ruta
    const incidentId = this.route.snapshot.paramMap.get('id') || '';

    // Obtiene la bicicleta desde la ruta
    const bicicletaId = this.route.snapshot.paramMap.get('bicicleta') || '';

    // Llama al servicio para registrar los detalles de la reparación en el backend
    this.incidentService
      .registerIncidentDetails(incidentId, acciones, piezas, detalles, bicicletaId)
      .subscribe({
        next: () => {
          alert('Detalles registrados exitosamente'); // Notifica éxito
          this.router.navigate(['/tecnico']); // Redirige a la lista de incidencias
        },
        error: () => {
          alert('Error al registrar los detalles'); // Notifica error
        }
      });

    // Reinicia el formulario después de registrar los datos
    this.detallesForm.reset();
  }
}
