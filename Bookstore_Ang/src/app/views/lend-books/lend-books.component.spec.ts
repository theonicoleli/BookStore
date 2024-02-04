import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LendBooksComponent } from './lend-books.component';

describe('LendBooksComponent', () => {
  let component: LendBooksComponent;
  let fixture: ComponentFixture<LendBooksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LendBooksComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LendBooksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
