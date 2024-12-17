import { Component, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsuarioService } from '../services/usuario.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Usuario } from '../compartido/usuario';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule, JsonPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';
import { USUARIOS } from '../compartido/usuarios';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, ReactiveFormsModule, MatButtonModule, JsonPipe, MatCardModule, MatGridListModule,CommonModule],
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
    private router: Router
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
    this.usuario = new Usuario;
    this.usuario.correo = this.loginForm.get("correo")?.value;
    this.usuario.password = this.loginForm.get("password")?.value;

    if (this.usuario.correo === USUARIOS[0].correo && this.usuario.password === USUARIOS[0].password) {
      this.router.navigate(["/"]);
      this.loginForm.reset({
        correo: '',
        password: '',
      });
    } else {
      this.passwordValid = false;
    }




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