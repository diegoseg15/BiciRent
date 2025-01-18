export class Renta {
    usuarioId: string;
    bicicletaId: string;
    fechaHoraRecogida: string;
    distancia: number
    estado: string;

    constructor() {
        this.usuarioId = "";
        this.bicicletaId = "";
        this.fechaHoraRecogida = "";
        this.distancia = 0;
        this.estado = "";
    }
}