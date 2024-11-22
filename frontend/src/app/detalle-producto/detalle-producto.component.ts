import { Component, OnInit, Inject} from '@angular/core';
import { Params, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Producto } from '../compartido/producto';
import { ProductoService } from '../services/producto.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Comentario } from '../compartido/comentario';
import { switchMap } from 'rxjs/operators';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';


@Component({
  selector: 'app-detalle-producto',
  templateUrl: './detalle-producto.component.html',
  styleUrls: ['./detalle-producto.component.scss']
})
export class DetalleProductoComponent implements OnInit {

  producto!:Producto;

  productoIds: number[] = [];
  prev:number = -1;
  post:number = -1;
  
  comentarioForm!: FormGroup;
	comentario: Comentario;
	faChevronLeft = faChevronLeft;
  faChevronRight = faChevronRight;

  
erroresForm:any = {

  'estrellas': '',

  'comentario': '',

  'autor': ''

};


mensajesError:any = {

  'estrellas': {
  'required':	'Las estrellas son obligatorias.'
  },

    'comentario': {
          'required':      'Los comentarios son obligatorios.'
  },

    'autor': {
          'required':      'El autor es obligatorio.',
          'minlength':     'El autor debe tener una longitud mínima de 2 caracteres.',

               
  }

};

  productorest!:Producto;

  
  constructor(private productoService:ProductoService,
              private route:ActivatedRoute,
              private location:Location,
              private fb: FormBuilder,
              private router:Router,
              @Inject('baseURL') public BaseURL:string ) { 
                this.comentario = new Comentario();
                this.crearFormulario();
              }


  ngOnInit(): void {
   
    this.productoService.getProductosIds().subscribe(productoIds => this.productoIds = productoIds);

   
    this.route.params.pipe(switchMap((params: Params) => this.productoService.getProducto(+params['id'])))
           	.subscribe(producto => {
               this.producto = producto;
               this.productorest = producto;
               this.setPrevPost(producto.id);
              });
    
    
 
    
  }

  volver(): void { 
    this.router.navigate(["/productos"]);
}


  
  crearFormulario() 
{
  
  this.comentarioForm = this.fb.group({
    estrellas: [5, Validators.required],
    comentario: ['', Validators.required],
    autor: ['', [Validators.required, Validators.minLength(2) ]]
    });

  this.comentarioForm.valueChanges.subscribe(datos=>this.onCambioValor(datos));
  this.onCambioValor();

 }

 onSubmit() {
        
        let d = new Date();
        let n = d.toISOString();
        this.comentario.fecha = n;
        this.comentario.estrellas = this.comentarioForm.get("estrellas")!.value;
        this.comentario.comentario = this.comentarioForm.get("comentario")!.value;
        this.comentario.autor = this.comentarioForm.get("autor")!.value;
        console.log(this.comentario); 
        this.productorest.comentarios.push(this.comentario);
        this.productoService.setProducto(this.productorest).
        subscribe(producto=>{this.producto = producto});
  
  this.comentarioForm.reset({
  estrellas: 5,
  comentario: '',
  autor: ''
  });


}

setPrevPost(productoId: number) {
  const index = this.productoIds.indexOf(productoId);
  this.prev = this.productoIds[(this.productoIds.length + index - 1)%this.productoIds.length];
  this.post = this.productoIds[(this.productoIds.length + index + 1)%this.productoIds.length];
}



onCambioValor(data?: any) {
  if (!this.comentarioForm) { return; }
  const form = this.comentarioForm;
  for (const field in this.erroresForm) {
    // Se borrarán los mensajes de error previos
    this.erroresForm[field] = '';
    const control = form.get(field);
    if (control && control.dirty && !control.valid) {
      const messages = this.mensajesError[field];
      for (const key in control.errors) {
        this.erroresForm[field] += messages[key] + ' ';
      }
    }
  }

  //Vista Previa
  
  this.comentario = new Comentario();
  this.comentario.autor = (this.comentarioForm.get("autor"))!.value;
  this.comentario.estrellas = (this.comentarioForm.get("estrellas"))!.value;
  this.comentario.comentario = (this.comentarioForm.get("comentario"))!.value;
  
}



}