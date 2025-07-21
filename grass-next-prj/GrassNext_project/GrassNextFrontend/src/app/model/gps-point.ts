/**
 * Class representing a geographical point using GPS coordinates.
 */
export class GpsPoint {
    /**
     * Constructs a new instance with the specified latitude and longitude.
     *
     * @param {number} latitude - The geographic latitude.
     * @param {number} longitude - The geographic longitude.
     *
     */
    constructor(
      public latitude: number,
      public longitude: number
    ) {}
}
