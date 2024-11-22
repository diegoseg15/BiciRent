import { TestBed } from '@angular/core/testing';

import { ProcesaHTTPMsjService } from './procesa-httpmsj.service';

describe('ProcesaHTTPMsjService', () => {
  let service: ProcesaHTTPMsjService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProcesaHTTPMsjService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
