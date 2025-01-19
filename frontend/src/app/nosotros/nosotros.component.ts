import { Component } from '@angular/core';
import { CabeceraComponent } from "../cabecera/cabecera.component";

@Component({
  selector: 'app-nosotros',
  standalone: true,
  imports: [CabeceraComponent],
  templateUrl: './nosotros.component.html',
  styleUrl: './nosotros.component.css'
})
export class NosotrosComponent {

}
