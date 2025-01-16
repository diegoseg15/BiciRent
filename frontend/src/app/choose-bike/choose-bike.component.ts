import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CookieService } from '../services/cookie.service';
import { Router } from '@angular/router';
import { CabeceraComponent } from "../cabecera/cabecera.component";

@Component({
  selector: 'app-choose-bike',
  standalone: true,
  imports: [CommonModule, CabeceraComponent],
  templateUrl: './choose-bike.component.html',
  styleUrl: './choose-bike.component.css'
})
export class ChooseBikeComponent {

  constructor(
    private cookieService: CookieService,
    private router: Router // Router incluido en el constructor
  ) { }

  ngOnInit(): void {
    // Lógica para verificar la sesión
    const sesion = this.cookieService.getObject('user'); // Obtén el objeto 'usuario'

    if (sesion === undefined) {
      // Si no hay sesión válida, redirige al inicio
      this.router.navigate(['/']);
    }
  }

  activeTab: string = 'nearby'; // Valor inicial
  // Lista de bicicletas cercanas
  nearbyBikes = [
    { name: "Bici Urbana", distance: "0.2 km", price: "$2.50/30min", rating: 4.5 },
    { name: "Mountain Bike", distance: "0.5 km", price: "$3.00/30min", rating: 4.2 },
    { name: "Bici Eléctrica", distance: "0.7 km", price: "$4.00/30min", rating: 4.8 }
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
}
