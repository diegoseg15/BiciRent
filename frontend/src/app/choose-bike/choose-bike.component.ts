import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CookieService } from '../services/cookie.service';
import { Router } from '@angular/router';
import { CabeceraComponent } from "../cabecera/cabecera.component";
import { RentaService } from '../services/renta.service';

@Component({
  selector: 'app-choose-bike',
  standalone: true,
  imports: [CommonModule, CabeceraComponent],
  templateUrl: './choose-bike.component.html',
  styleUrl: './choose-bike.component.css'
})
export class ChooseBikeComponent {

  constructor(
    private rentaBici: RentaService,
    private cookieService: CookieService,
    private router: Router 
  ) { }

  ngOnInit(): void {
    // Lógica para verificar la sesión
    const sesion = this.cookieService.getObject('user'); // Obtén el objeto 'usuario'
    const rentaInfo = this.cookieService.getObject('renta'); // Obtén el objeto 'renta'

    if (sesion === undefined) {
      // Si no hay sesión válida, redirige al inicio
      this.router.navigate(['/']);
    } else if (rentaInfo !== undefined) {
      // Si hay una renta activa, redirige a la página de renta
      this.router.navigate(['/renta']);
    }
  }

  activeTab: string = 'nearby'; // Valor inicial
  // Lista de bicicletas cercanas
  nearbyBikes = [
    { id: "Bici123", nombre: "Bici Urbana", precio: "2.50", estacion: "Estación 1" },
    { id: "Bici456", nombre: "Mountain Bike", precio: "3.00", estacion: "Estación 2" },
    { id: "Bici789", nombre: "Bici Eléctrica", precio: "4.00", estacion: "Estación 3" }
  ];

  // Lista de bicicletas favoritas
  favoriteBikes = [
    { name: "Mi Favorita", distance: "1.2 km", price: "$2.50/30min", rating: 4.7 }
  ];

  // Lista de viajes recientes
  recentTrips = [
    { destination: "Parque Central", date: "2025-01-15", duration: "15min", cost: "$2.50" },
    { destination: "Centro Comercial", date: "2025-01-14", duration: "20min", cost: "$3.00" },
    { destination: "Estación de Tren", date: "2025-01-13", duration: "10min", cost: "$1.50" }
  ];

  // Cambiar el tab activo
  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  showModal: boolean = false; // Valor inicial

  selectedBike: any = {
    id: "",
    nombre: "",
    estacion: "",
    precio: "",
    estado: ""
  }; // Valor inicial

  showBikeModal(): void {
    this.showModal = !this.showModal;
    this.selectedBike = {
      id: "",
      nombre: "",
      estacion: "",
      precio: "",
      estado: ""
    };
  } // Método para mostrar el modal

  selectBike(bike: any): void {
    this.showModal = true;
    this.selectedBike = bike;
    this.showModal = true;
  } // Método para seleccionar una bicicleta

  onSubmit(): void {
    //Verifica si el envio de datos es inválido.
    if (this.selectedBike.id === "") {
      return;
    }

    const usuarioData = this.cookieService.getObject('user') as ({ dni: string } | undefined);

    if (usuarioData === undefined) {
      return;
    } else {
      this.rentaBici.rentaBici({
        usuarioId: usuarioData?.dni,
        bicicletaId: this.selectedBike.id,
        fechaHoraRecogida: new Date().toISOString(),
        distancia: 0,
        estado: "rentado"
      }).subscribe(
        (res) => {
          console.log(res);
          const rentaBiciData = { bicicletaId: this.selectedBike.id, fechaHoraRecogida: new Date().toISOString(), distancia: 0, estado: "rentado" };
          this.cookieService.setObject('renta', rentaBiciData, 0.5);
          this.selectedBike = { id: "", nombre: "", estacion: "", precio: "" };
          this.showModal = false;
          this.router.navigate(['/renta']);
        },
        (error) => {
          console.error(error);
        }
      );
    }
  }
}
