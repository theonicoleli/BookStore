import { TestBed } from '@angular/core/testing';

import { ReadbookService } from './readbook.service';

describe('ReadbookService', () => {
  let service: ReadbookService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReadbookService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
