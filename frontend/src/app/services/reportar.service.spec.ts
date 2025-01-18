import { TestBed } from '@angular/core/testing';

import { ReportarService } from './reportar.service';

describe('ReportarService', () => {
  let service: ReportarService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportarService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
