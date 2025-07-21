/**
 * Represents data related to plume detection and environmental parameters.
 */
export class PlumeData {
    /**
     * Constructs an instance of the class PlumeData with information retrieved from Gaussian Plume calculations.
     *
     * @param {Array<number>} vehicles - An array representing the vehicles detected.
     * @param {number} weatherStability - A numeric value depicting the stability class of the weather.
     * @param {number} pollutionType - A numeric code indicating the type of pollution detected.
     * @param {number} detectorId - A unique identifier for the detector device.
     * @param {String} date - The date of the detection, represented as a string.
     * @param {number} time - The time that the calculations took.
     * @param {number} area - The area that the value was calculated for.
     *
     */
    constructor(
      public vehicles: Array<number>,
      public weatherStability: number,
      public pollutionType: number,
      public detectorId: number,
      public date: String,
      public time: number,
      public area: number,
    ) {}
}
