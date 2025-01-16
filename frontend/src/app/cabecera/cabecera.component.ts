import { Component } from '@angular/core';
import { CookieService } from '../services/cookie.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-cabecera',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './cabecera.component.html',
  styleUrl: './cabecera.component.css'
})
export class CabeceraComponent {
  userInitials: string = '';
  userRole: string = '';

  constructor(private cookieService: CookieService) { }

  ngOnInit(): void {
    const user = this.cookieService.getObject('user') as { nombre: string, apellido: string, rol: string } | null;

    if (user) {
      this.userInitials = this.getUserInitials(user.nombre, user.apellido);
      this.userRole = user.rol;
      console.log(user.rol);

    }
  }

  getUserInitials(nombre: string, apellido: string): string {
    const nameCapital = nombre[0];
    const lastnameCapital = apellido[0];
    return `${nameCapital}${lastnameCapital}`.toUpperCase();
  }
}
