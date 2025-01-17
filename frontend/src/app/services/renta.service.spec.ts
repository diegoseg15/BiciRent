import { TestBed } from '@angular/core/testing';

import { RentaService } from './renta.service';

describe('RentaService', () => {
  let service: RentaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RentaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
