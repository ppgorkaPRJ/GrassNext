import { LatLngTuple } from "leaflet";
import { GpsPoint } from "./gps-point";

/**
 * Represents a topo detector with associated coordinates and properties.
 */
export class Detector {
    /**
     * Creates an instance of a location with relevant details.
     *
     * @param {number} id - The unique identifier for the location.
     * @param {string} locationName - The name of the location.
     * @param {GpsPoint} topoDetectorCoordinates - The GPS coordinates of the topo detector.
     * @param {GpsPoint} roadStartCoordinates - The GPS coordinates marking the start of the road.
     * @param {GpsPoint} roadEndCoordinates - The GPS coordinates marking the end of the road.
     */
    constructor(
      public id: number,
      public locationName: string,
      public topoDetectorCoordinates: GpsPoint,
      public roadStartCoordinates: GpsPoint,
      public roadEndCoordinates: GpsPoint
    ) {}

    /**
     * Retrieves the geographical coordinates of the TopoDetector point.
     *
     * @return {LatLngTuple} A tuple containing the latitude and longitude of the TopoDetector point.
     */
    getTopoDetectorPoint() : LatLngTuple {
      return [this.topoDetectorCoordinates.latitude, this.topoDetectorCoordinates.longitude];
    }

    /**
     * Retrieves the starting point of a road defined by its coordinates.
     *
     * @return {LatLngTuple} A tuple representing the latitude and longitude of the road's starting point.
     */
    getRoadStartPoint() : LatLngTuple {
      return [this.roadStartCoordinates.latitude, this.roadStartCoordinates.longitude];
    }

    /**
     * Retrieves the ending point of a road defined by its coordinates.
     * *
     * @return {LatLngTuple} A tuple representing the latitude and longitude of the road's ending point.
     *
     */
    getRoadEndPoint() : LatLngTuple {
      return [this.roadEndCoordinates.latitude, this.roadEndCoordinates.longitude];
    }
}
