import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ChooseBikeComponent } from './choose-bike/choose-bike.component';
import { HomeComponent } from './home/home.component';

//iniciado sesión
export const routes: Routes = [
    {path:'', component:HomeComponent},
    {path:'login', component:LoginComponent},
    {path:'register', component:RegisterComponent},
    {path:'choose-bike', component:ChooseBikeComponent}
];

//sin iniciar sesión
/**
 export const routes: Routes = [
    {path:'', component:homeComponent},
];
 */