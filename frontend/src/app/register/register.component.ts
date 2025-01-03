import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Usuario } from '../compartido/usuario';
import { UsuarioService } from '../services/usuario.service';
import { Router, RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule, JsonPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule,
    JsonPipe,
    MatCardModule,
    MatGridListModule,
    CommonModule,
    RouterModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm!: FormGroup;
  usuario!: Usuario;

  // Errores de validación.
  erroresForm: any = {
    'dni': '',
    'nombre': '',
    'apellido': '',
    'telefono': '',
    'correo': '',
    'password': '',
    'confirmPassword': ''
  };

  // Mensajes de error personalizados.
  mensajesError: any = {
    'dni': {
      'required': 'El DNI es obligatorio.',
      'pattern': 'El DNI debe tener 8 números y una letra.'
    },
    'nombre': {
      'required': 'El nombre es obligatorio.',
      'pattern': 'El nombre no puede contener números.'
    },
    'apellido': {
      'required': 'El apellido es obligatorio.',
      'pattern': 'El apellido no puede contener números.'
    },
    'telefono': {
      'required': 'El teléfono es obligatorio.',
      'pattern': 'El teléfono debe tener 9 números.'
    },
    'correo': {
      'required': 'El correo es obligatorio.',
      'email': 'El formato del correo no es correcto.'
    },
    'password': {
      'required': 'La contraseña es obligatoria.',
      'minlength': 'La contraseña debe tener al menos 8 caracteres.',
      'pattern': 'Debe incluir al menos una mayúscula, una minúscula y un número.'
    },
    'confirmPassword': {
      'required': 'La confirmación de contraseña es obligatoria.',
      'passwordMatch': 'Las contraseñas no coinciden.'
    }
  };

  constructor(
    private usuarioService: UsuarioService, 
    private fb: FormBuilder, // Para crear el formulario.
    private router: Router // Para redirigir al usuario.
  ) {
    this.crearFormulario();
    this.usuario = new Usuario();
  }

  crearFormulario() {
    this.registerForm = this.fb.group({
      dni: ['', [Validators.required, Validators.pattern('^[0-9]{8}[A-Za-z]$')]], // 8 números y una letra.
      nombre: ['', [Validators.required, Validators.pattern('[a-zA-Z ]*')]], // Letras y espacios.
      apellido: ['', [Validators.required, Validators.pattern('[a-zA-Z ]*')]], // Letras y espacios.
      telefono: ['', [Validators.required, Validators.pattern('^[0-9]{9}$')]], // 9 números.
      correo: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).+$/) // Una mayúscula, una minúscula y un número.
        ]
      ],
      confirmPassword: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });

    this.registerForm.valueChanges.subscribe(datos => this.onCambioValor(datos)); 
    this.onCambioValor();
  }

  passwordMatchValidator(formGroup: FormGroup): null | object {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;

    return password === confirmPassword ? null : { passwordMismatch: true }; 
  }

  onCambioValor(data?: any) {
    if (!this.registerForm) { return; }
    const form = this.registerForm;

    for (const field in this.erroresForm) {
      this.erroresForm[field] = '';
      const control = form.get(field);

      if (control && control.dirty && !control.valid) {
        const messages = this.mensajesError[field];
        for (const key in control.errors) {
          this.erroresForm[field] += messages[key] + ' ';
        }
      }
    }

    // Validación para confirmPassword.
    const passwordControl = form.get('password');
    const confirmPasswordControl = form.get('confirmPassword');

    if (passwordControl && confirmPasswordControl && passwordControl.value !== confirmPasswordControl.value) {
      this.erroresForm['confirmPassword'] = this.mensajesError['confirmPassword']['passwordMatch'];
    }
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      return;
    }

    this.usuario = new Usuario();
    this.usuario.dni = this.registerForm.get('dni')?.value;
    this.usuario.nombre = this.registerForm.get('nombre')?.value;
    this.usuario.apellido = this.registerForm.get('apellido')?.value;
    this.usuario.telefono = this.registerForm.get('telefono')?.value;
    this.usuario.correo = this.registerForm.get('correo')?.value;
    this.usuario.password = this.registerForm.get('password')?.value;

    if (this.registerForm.get('password')?.value === this.registerForm.get('confirmPassword')?.value) {
      this.router.navigate(['/']);
      this.registerForm.reset();
    }
  }
}
