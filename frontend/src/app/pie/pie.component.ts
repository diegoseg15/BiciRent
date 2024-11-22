import { Component, OnInit } from '@angular/core';

import { faHome, faInfo, faList, faAddressCard,
  faPhone, faFax, faEnvelope } from '@fortawesome/free-solid-svg-icons';
import { faGoogle, faFacebook, faLinkedin, faTwitter, faYoutube } from '@fortawesome/free-brands-svg-icons';


@Component({
  selector: 'app-pie',
  templateUrl: './pie.component.html',
  styleUrls: ['./pie.component.scss']
})
export class PieComponent implements OnInit {

  faHome = faHome;
  faInfo = faInfo;
	faList = faList;
	faAddressCard = faAddressCard;
	faPhone = faPhone;
	faFax = faFax;
	faEnvelope = faEnvelope;
	faGoogle = faGoogle;
	faFacebook = faFacebook;
	faLinkedin = faLinkedin;
	faTwitter = faTwitter;
	faYoutube = faYoutube;


  constructor() { }

  ngOnInit(): void {
  }

}
