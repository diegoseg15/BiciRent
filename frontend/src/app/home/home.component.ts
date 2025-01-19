import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router'; // Router y RouterModule importados
import { CookieService } from '../services/cookie.service';
import { CabeceraComponent } from "../cabecera/cabecera.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule, CabeceraComponent], // Importa RouterModule para habilitar la navegación
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'] // Cambiado a 'styleUrls' en plural
})
export class HomeComponent implements OnInit {
  constructor(
    private cookieService: CookieService,
    private router: Router // Router incluido en el constructor
  ) {}

  ngOnInit(): void {
    // Lógica para verificar la sesión
    const sesion = this.cookieService.getObject('user'); // Obtén el objeto 'usuario'

    if (sesion !== undefined) {
      // Si no hay sesión válida, redirige al inicio
      this.router.navigate(['/choose-bike']);
    }
  }
}
