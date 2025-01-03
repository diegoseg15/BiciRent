import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ChooseBikeComponent } from './choose-bike/choose-bike.component';

//iniciado sesión
export const routes: Routes = [
    {path:'', component:ChooseBikeComponent},
    {path:'login', component:LoginComponent},
    {path:'signup', component:RegisterComponent}
];

//sin iniciar sesión
/**
 export const routes: Routes = [
    {path:'', component:homeComponent},
];
 */