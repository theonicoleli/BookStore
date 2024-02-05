import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentinfoComponent } from './commentinfo.component';

describe('CommentinfoComponent', () => {
  let component: CommentinfoComponent;
  let fixture: ComponentFixture<CommentinfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommentinfoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CommentinfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
