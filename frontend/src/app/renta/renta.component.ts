import { Component } from '@angular/core';
import { CabeceraComponent } from "../cabecera/cabecera.component";
import { CookieService } from '../services/cookie.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-renta',
  standalone: true,
  imports: [CabeceraComponent],
  templateUrl: './renta.component.html',
  styleUrl: './renta.component.css'
})
export class RentaComponent {
  constructor(
    private cookieService: CookieService,
    private router: Router
  ) { }
  ngOnInit() {
    const sesion = this.cookieService.getObject('user');
    const rentaInfo = this.cookieService.getObject('renta');
    if (sesion === undefined) {
      this.router.navigate(['/']);
    } else if (rentaInfo === undefined) {
      this.router.navigate(['/choose-bike']);
    }
  }
}
