import { TestBed } from '@angular/core/testing';

import { SavebookService } from './savebook.service';

describe('SavebookService', () => {
  let service: SavebookService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SavebookService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
