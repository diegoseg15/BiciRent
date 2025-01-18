import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseBikeComponent } from './choose-bike.component';

describe('ChooseBikeComponent', () => {
  let component: ChooseBikeComponent;
  let fixture: ComponentFixture<ChooseBikeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChooseBikeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChooseBikeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
