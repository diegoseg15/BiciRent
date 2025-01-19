import { Component } from '@angular/core';
import { CabeceraComponent } from "../cabecera/cabecera.component";
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-contactos',
  standalone: true,
  imports: [CabeceraComponent, MatIconModule],
  templateUrl: './contactos.component.html',
  styleUrl: './contactos.component.css'
})
export class ContactosComponent {

}
