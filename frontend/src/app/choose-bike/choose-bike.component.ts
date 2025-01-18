import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CookieService } from '../services/cookie.service';
import { Router } from '@angular/router';
import { CabeceraComponent } from "../cabecera/cabecera.component";
import { RentaService } from '../services/renta.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-choose-bike',
  standalone: true,
  imports: [CommonModule, CabeceraComponent, MatFormFieldModule, MatInputModule, ReactiveFormsModule],
  templateUrl: './choose-bike.component.html',
  styleUrls: ['./choose-bike.component.css']
})
export class ChooseBikeComponent {

  searchForm!: FormGroup;

  mensajesError: any = {
    'searchStation': {
      'required': 'La búsqueda es obligatoria.'
    }
  };

  erroresForm: any = {
    'searchStation': ''
  };

  constructor(
    private fb: FormBuilder,
    private rentaBici: RentaService,
    private cookieService: CookieService,
    private router: Router
  ) {
    this.crearFormularioSearch();
  }

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

    // Llamada a bikeList al iniciar el componente
    this.loadNearbyBikes();
    this.loadStations()
    this.loadRentas()
  }

  crearFormularioSearch() {
    this.searchForm = this.fb.group({
      searchStation: ['', Validators.required]
    });
    this.searchForm.valueChanges.subscribe(datos => this.onCambioValor(datos));
  }

  onCambioValor(data?: any) {
    if (!this.searchForm) { return; }
    const form = this.searchForm;
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
  }

  activeTab: string = 'nearby'; // Valor inicial

  // Lista de bicicletas
  nearbyBikes: any[] = [];


  stations: any[] = [];

  // Lista de viajes recientes
  recentTrips: any[] = [];

  // Cambiar el tab activo
  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  showModal: boolean = false; // Valor inicial

  selectedBike: any = {
    id: "",
    nombre: "",
    estacionId: "",
    costo: "",
    estado: ""
  }; // Valor inicial

  showBikeModal(): void {
    this.showModal = !this.showModal;
    this.selectedBike = {
      id: "",
      nombre: "",
      estacionId: "",
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
          const rentaBiciData = { bicicleta: this.selectedBike.id, dni: usuarioData.dni, fechaHoraRecogida: new Date().toISOString(), distancia: 0, estado: "rentado" };
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

  onSubmitSearch() {
    let busqueda = this.searchForm.get('searchStation')?.value;
    if (busqueda) {

      let terminoBusqueda = busqueda.split(' - ')[0];

      console.log('Término de búsqueda: ', terminoBusqueda);

      this.rentaBici.bikeList(terminoBusqueda).subscribe(
        (resultado) => {
          console.log('Resultado de la búsqueda: ', resultado);

          this.nearbyBikes = resultado;
        },
        (error) => {
          console.error('Error al realizar la búsqueda: ', error);
        }
      );
    } else {
      console.log('Por favor ingresa un término de búsqueda');
    }

  }

  // Método para cargar las bicicletas cercanas al inicio
  loadNearbyBikes() {
    this.rentaBici.bikeList("").subscribe(
      (resultado) => {
        console.log('Bicicletas cercanas al iniciar: ', resultado);
        this.nearbyBikes = resultado;  // Asegúrate de que esto sea un array
      },
      (error) => {
        console.error('Error al obtener las bicicletas cercanas: ', error);
      }
    );
  }

  loadStations() {
    this.rentaBici.stationsList().subscribe(
      (resultado) => {
        this.stations = resultado
      },
      (error) => {
        console.error('Error al obtener las estaciones: ', error);

      }
    )
  }

  loadRentas() {
    const usuarioData = this.cookieService.getObject('user') as ({ dni: string } | undefined);
    this.rentaBici.rentasList(usuarioData?.dni ? usuarioData?.dni : "").subscribe(
      (resultado) => {
        this.recentTrips = resultado
      },
      (error) => {
        console.error('Error al obtener las estaciones: ', error);

      }
    )
  }
}
