import { Component, OnInit, Inject } from '@angular/core';

import { faHome, faInfo, faList, faAddressCard, faSignInAlt } from '@fortawesome/free-solid-svg-icons';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { LoginComponent } from '../login/login.component';

@Component({
  selector: 'app-cabecera',
  templateUrl: './cabecera.component.html',
  styleUrls: ['./cabecera.component.scss']
})
export class CabeceraComponent implements OnInit {

  faHome = faHome;
  faInfo = faInfo;
  faList = faList;
  faAddressCard = faAddressCard;
  faSignInAlt = faSignInAlt;

  constructor(public dialogo: MatDialog,
    @Inject('baseURL') public BaseURL:string) { }

  ngOnInit(): void {
  }

  abrirFormularioLogin() {   
    this.dialogo.open(LoginComponent, {width: '500px', height: '450px'}); 
    }
   

}
