import { Component, OnInit, Inject } from '@angular/core';

import { Producto } from '../compartido/producto';

import { ProductoService } from '../services/producto.service';


@Component({
  selector: 'app-productos',
  templateUrl: './productos.component.html',
  styleUrls: ['./productos.component.scss']
})
export class ProductosComponent implements OnInit {

  vProductos:Producto[] = [];
  errorMensaje:string="";
  productoSeleccionado = new Producto();

  constructor(private productoService:ProductoService,
    @Inject('baseURL') public BaseURL:string) { }

  ngOnInit(): void {
    
    this.productoService.getProductos().subscribe(productos=>this.vProductos = productos,
      errorMensaje => this.errorMensaje = <any>errorMensaje);
   
  }

  onSeleccionado(producto:Producto){
    this.productoSeleccionado = producto;
  }

}
