import { Comentario } from './comentario';

export class Producto{
    id:number;
    nombre: string;
    precio: number;
    imagen: string;
    oferta: boolean;
    comentarios: Comentario[];
    

    constructor(){
        this.id = -1;
        this.nombre = "";
        this.precio = 0;
        this.imagen = "";
        this.oferta = false;
        this.comentarios = [];
        
        
    }
}