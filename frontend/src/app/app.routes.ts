import { Routes } from '@angular/router';
import { ReportarComponent } from './reportar/reportar.component'; 
import { VerIncidenciasComponent } from './ver-incidencias/ver-incidencias.component';

export const routes: Routes = [
    { path: 'reportar', component: ReportarComponent },
    { path: 'ver-incidencias', component: VerIncidenciasComponent }, 
    { path: '', redirectTo: 'reportar', pathMatch: 'full' } // Redirigir a 'reportar' por defecto
];
