import { Component } from '@angular/core';
import { ToggleService } from '../services/toggle.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { DialogLegendComponent } from '../dialog-legend/dialog-legend.component';

/**
 * MenuComponent is responsible for managing and interacting with the application menu's functionality.
 * It provides methods to toggle the sidenav and open dialogs for additional menu-related features.
 */
@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent {
  /**
   * Initializes a new instance of the class.
   *
   * @param {ToggleService} toggleService - Service used for toggling sidenav.
   * @param {Router} _router - Router service for navigation and routing within the application.
   * @param {MatDialog} dialog - Service used to open and manage Material Design dialogs.
   */
  constructor(public toggleService: ToggleService, public _router: Router, private dialog: MatDialog) {
  }

  /**
   * Toggles the visibility state of the sidenav. Additionally, adjusts the map size if the sidenav visibility changes.
   *
   * @return {void} Does not return a value.
   */
  toggleSidenav() {
    this.toggleService.toggleSidenav();
    setTimeout(() => {
      this.toggleService.map.invalidateSize();
    }, 200);
  }

  /**
   * Opens a dialog using the DialogLegendComponent with a fixed width of 50%.
   *
   */
  openDialog() {
    this.dialog.open(DialogLegendComponent, {
      width: "50%"
    });
  }

  /**
   * Opens a dialog displaying the DialogLegendComponent with a specified width.
   *
   *
   */
  openDialogPhone() {
    this.dialog.open(DialogLegendComponent, {
      width: "80%"
    });
  }
}
