import { Injectable } from '@angular/core';

/**
 * A service responsible for managing the state of a toggleable sidenav.
 *
 */
@Injectable({providedIn: 'root'})
export class ToggleService {
    public opened: boolean = true;
    public map: L.Map;

    toggleSidenav() {
        this.opened = !this.opened;
    }
}
