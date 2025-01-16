import { Component, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsuarioService } from '../services/usuario.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Usuario } from '../compartido/usuario';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule, JsonPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';
import { USUARIOS } from '../compartido/usuarios';
import { CookieService } from '../services/cookie.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, ReactiveFormsModule, MatButtonModule, JsonPipe, MatCardModule, MatGridListModule, CommonModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent {
  passwordValid: boolean = true;

  loginForm!: FormGroup;  //estamos llenando este formulario
  usuario!: Usuario;    //estamos obteniendo la info del usuario

  erroresForm: any = {
    'correo': '',
    'password': ''
  };

  mensajesError: any = {
    'correo': {
      'required': 'El correo es obligatorio.',
      'email': 'El formato del correo no es correcto.',
    },

    'password': {
      'required': 'La contraseña es obligatoria.'
    }
  };

  constructor(private usuarioService: UsuarioService,
    //private route: ActivatedRoute, private location: Location,
    private fb: FormBuilder,
    private router: Router,
    private cookieService: CookieService
  ) {
    this.crearFormulario();
    this.usuario = new Usuario;
  }

  crearFormulario() {
    this.loginForm = this.fb.group({
      correo: ['', [Validators.required, Validators.email]],  //aparece por defecto en la pantalla inicial
      password: ['', Validators.required]
    })
    this.loginForm.valueChanges.subscribe(datos => this.onCambioValor(datos));
    this.onCambioValor();
  }

  onSubmit() {
    this.usuario = new Usuario();
    this.usuario.correo = this.loginForm.get('correo')?.value;
    this.usuario.password = this.loginForm.get('password')?.value;

    const sessionData = {
      dni: '12345678',
      nombre: 'Juan',
      apellido: 'Pérez',
      telefono: '123456789',
      correo: 'juan@gmail.com',
      rol: 'tecnico'
    };

    // this.cookieService.setObject('user', sessionData, 7);

    // console.log(this.cookieService.getObject('user'));
    // this.router.navigate(['/choose-bike']);


    this.usuarioService.setLogin(this.usuario).subscribe(
      (response: any) => {
        // Asume que `response` contiene los datos de la sesión
        const sessionData = {
          dni: response.dni,
          nombre: response.nombre,
          apellido: response.apellido,
          telefono: response.telefono,
          correo: response.correo,
          rol: response.rol
        };

        // Guardar los datos de la sesión en el almacenamiento local
        this.cookieService.setObject('user', sessionData, 7);

        // Redirigir al usuario después del login exitoso
        this.router.navigate(['/choose-bike']);

        // Reiniciar el formulario
        this.loginForm.reset({
          correo: '',
          password: ''
        });
      },
      (error) => {
        console.error('Error al iniciar sesión:', error);
        this.passwordValid = false;
      }
    );
  }

  onCambioValor(data?: any) {
    if (!this.loginForm) { return; }
    const form = this.loginForm;
    for (const field in this.erroresForm) {
      // Se borrarán los mensajes de error previos
      this.erroresForm[field] = '';
      const control = form.get(field);
      if (control && control.dirty && !control.valid) {
        const messages = this.mensajesError[field];
        for (const key in control.errors) {
          this.erroresForm[field] += messages[key] + ' ';
        }
      }
    }


  }
}