/**
 * Represents the stability class of the weather with associated details.
 *
 */
export class WeatherStability {
    /**
     * Creates an instance of a class representing weather data.
     *
     * @param {number} id - The unique identifier for the weather stability class.
     * @param {string} weatherStability - Name of weather stability class.
     * @param {string} description - Additional textual description of weather stability.
     *
     */
    constructor(
      public id: number,
      public weatherStability: string,
      public description: string
    ) {}
}
