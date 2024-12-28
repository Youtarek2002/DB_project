import { TestBed } from '@angular/core/testing';

import { LotsserviceService } from './lotsservice.service';

describe('LotsserviceService', () => {
  let service: LotsserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LotsserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
