import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OptionModelComponent } from './option-model.component';

describe('OptionModelComponent', () => {
  let component: OptionModelComponent;
  let fixture: ComponentFixture<OptionModelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OptionModelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OptionModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
