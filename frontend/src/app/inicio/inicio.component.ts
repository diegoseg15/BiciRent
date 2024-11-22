import { Component, OnInit, Inject } from '@angular/core';

import { Producto } from '../compartido/producto';

import { ProductoService } from '../services/producto.service';

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.scss']
})
export class InicioComponent implements OnInit {

  productosOferta:Producto[] = [];

  constructor(private productoService:ProductoService,
    @Inject('baseURL') public BaseURL:string) { }

  ngOnInit(): void {
   
    this.productoService.getProductosOferta().subscribe(productos=>this.productosOferta = productos);
  }

}
