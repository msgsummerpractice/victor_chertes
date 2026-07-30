import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DogCard } from './dog-card';

describe('DogCard', () => {
  let component: DogCard;
  let fixture: ComponentFixture<DogCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DogCard],
    }).compileComponents();

    fixture = TestBed.createComponent(DogCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
