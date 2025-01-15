import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { TecnicoComponent } from './tecnico/tecnico.component';
import { DetallesReparacionComponent } from './detalles-reparacion/detalles-reparacion.component';

export const routes: Routes = [
    // Ruta para el login
    { path: 'login', component: LoginComponent },
    
    // Ruta para la vista de detalles de reparación
    { path: 'detalles-reparacion/:id', component: DetallesReparacionComponent },
    
    // Ruta para la vista principal del técnico
    { path: 'tecnico', component: TecnicoComponent },
    
    // Ruta predeterminada (redirige a login o técnico según prefieras)
    { path: '', redirectTo: '/login', pathMatch: 'full' }
];
