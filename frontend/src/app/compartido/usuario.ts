export class Usuario {
    dni:string;
    nombre:string;
    apellido:string;
    telefono:string;
    correo:string;
    password:string;
    rol:string;
    
    constructor(){
        this.dni = "";
        this.nombre = "";
        this.apellido = "";
        this.telefono = "";
        this.correo = "";
        this.password = "";
        this.rol= "";
    }
}