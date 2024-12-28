import { TestBed } from '@angular/core/testing';

import { SpotsserviceService } from './spotsservice.service';

describe('SpotsserviceService', () => {
  let service: SpotsserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpotsserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
