export class Consulta {
    nombre: string;
    apellidos: string;
    telefono: number;
    email: string;
    contactar: boolean;
    tipocontacto: string;
    mensaje: string;


    constructor(){
        this.nombre = "";
        this.apellidos = "";
        this.telefono = 0;
        this.email = ""; 
        this.contactar = false;
        this.tipocontacto = 'Ninguno';
        this.mensaje  = "";
    }
};

export const TipoContacto = ['Ninguno', 'Teléfono', 'Email'];

