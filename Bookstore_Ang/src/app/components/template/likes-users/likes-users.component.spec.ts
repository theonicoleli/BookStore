import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LikesUsersComponent } from './likes-users.component';

describe('LikesUsersComponent', () => {
  let component: LikesUsersComponent;
  let fixture: ComponentFixture<LikesUsersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LikesUsersComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LikesUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
