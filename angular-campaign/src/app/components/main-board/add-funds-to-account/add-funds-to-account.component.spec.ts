import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddFundsToAccountComponent } from './add-funds-to-account.component';

describe('AddFundsToAccountComponent', () => {
  let component: AddFundsToAccountComponent;
  let fixture: ComponentFixture<AddFundsToAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddFundsToAccountComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddFundsToAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
