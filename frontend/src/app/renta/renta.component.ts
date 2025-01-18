import { Component } from '@angular/core';
import { CabeceraComponent } from "../cabecera/cabecera.component";
import { CookieService } from '../services/cookie.service';
import { Router } from '@angular/router';
import { RentaService } from '../services/renta.service';

@Component({
  selector: 'app-renta',
  standalone: true,
  imports: [CabeceraComponent],
  templateUrl: './renta.component.html',
  styleUrl: './renta.component.css'
})
export class RentaComponent {
  constructor(
    private devolucionBici: RentaService,
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

  handleDevolution() {
    // Verificar si la renta está activa
    const rentaInfo = this.cookieService.getObject('renta') as ({
      dni: String, bicicleta: String, fechaHoraRecogida: String, estado: String, distancia: Number
    } | undefined);

    console.log(rentaInfo);
    

    if (rentaInfo === undefined) {
      this.router.navigate(['/choose-bike']);
    } else {
      // Verificar si las propiedades necesarias están definidas
      if (rentaInfo.dni && rentaInfo.bicicleta && rentaInfo.fechaHoraRecogida && rentaInfo.estado !== undefined) {
        // Realizar la devolución
        this.devolucionBici.devolucionBici({
          usuarioId: rentaInfo.dni.toString(),
          bicicletaId: rentaInfo.bicicleta.toString(),
          fechaHoraRecogida: rentaInfo.fechaHoraRecogida.toString(),
          distancia: 10,
          estado: "devuelto",
        }).subscribe(
          (res) => {
            // Eliminar la cookie de renta
            this.cookieService.deleteCookie('renta');
            this.router.navigate(['/choose-bike']);
          },
          (err) => {
            console.log(err);
          }
        );
      } else {
        // Manejo de error en caso de que alguna propiedad esté indefinida
        console.error("Algunos datos de renta están incompletos.");
        this.router.navigate(['/choose-bike']);
      }
    }
  }

}
