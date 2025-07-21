import { AfterViewInit, Component, HostListener, Input, OnInit, Output } from '@angular/core';
import { ToggleService } from '../services/toggle.service';

/**
 * Represents a reusable panel component within the application.
 * The component contains logic for displaying and managing a panel's state,
 * with interaction handled through a toggle service.
 *
 */
@Component({
  selector: 'app-panel',
  templateUrl: './panel.component.html',
  styleUrl: './panel.component.css'
})
export class PanelComponent {
  /**
   * Initializes a new instance of the class with the provided ToggleService.
   *
   * @param {ToggleService} toggleService - The service used to toggle sidenav.
   */
  constructor(public toggleService: ToggleService) {
  }
}
