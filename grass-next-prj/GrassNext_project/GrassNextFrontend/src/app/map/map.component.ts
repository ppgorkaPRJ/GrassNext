import { Component, AfterViewInit, OnInit, Output } from '@angular/core';
import * as L from 'leaflet';
import {control} from "leaflet";
import zoom = control.zoom;
import { ToggleService } from '../services/toggle.service';

/**
 * The MapComponent is responsible for rendering and initializing a Leaflet-based map
 * within the Angular application. It uses the Leaflet library to create and manage
 * the map, including its tiles, center point, and zoom functionality.
 *
 */
@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})
export class MapComponent implements OnInit {
  /**
   * Represents the geographical center point used for map rendering.
   * The centroid specifies a latitude and longitude coordinate in the format of [lat, lng].
   */
  centroid: L.LatLngExpression = [53.4481, 14.5372];

  /**
   * Creates an instance of the MapComponent class.
   *
   * @param {ToggleService} toggleService - The service used to manage toggle operations.
   *
   */
  constructor(public toggleService: ToggleService) { }

  /**
   * Lifecycle hook called after Angular has initialized all data-bound properties of a directive.
   * This method is used to perform component initialization, such as initializing the map and ensuring the map size is updated properly after rendering.
   *
   */
  ngOnInit(): void {
    setTimeout(() => {
      this.initMap();
      this.toggleService.map.invalidateSize();
    });
  }

  /**
   * Initializes the map instance with specified configurations and adds a tile layer.
   * The map is centered on a predefined centroid, set to zoom level 13, and uses a canvas renderer.
   * A tile layer based on OpenStreetMap data is added to the map with specified zoom limits and attribution.
   *
   */
  private initMap(): void {
    this.toggleService.map = L.map('map', {
      center: this.centroid,
      zoom: 13,
      renderer: L.canvas()
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 10,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.toggleService.map);
  }
}
