import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DogsContainer } from './dogs-container';

describe('DogsContainer', () => {
  let component: DogsContainer;
  let fixture: ComponentFixture<DogsContainer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DogsContainer],
    }).compileComponents();

    fixture = TestBed.createComponent(DogsContainer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
