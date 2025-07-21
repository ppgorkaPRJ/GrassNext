import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogLegendComponent } from './dialog-legend.component';

describe('DialogLegendComponent', () => {
  let component: DialogLegendComponent;
  let fixture: ComponentFixture<DialogLegendComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DialogLegendComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DialogLegendComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
