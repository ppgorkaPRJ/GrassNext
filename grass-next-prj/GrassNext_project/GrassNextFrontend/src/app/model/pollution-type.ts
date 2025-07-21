/**
 * Represents a type of pollution with its associated details.
 */
export class PollutionType {
    /**
     * Constructor for creating an instance of a pollution entity.
     *
     * @param {number} id - The unique identifier for the pollution type.
     * @param {string} pollutionType - A chemical formula of the pollution type (with footnotes and full chemical formula name).
     * @param {string} description - Additional textual description providing a simplified record of the chemical formula of the pollution type.
     *
     */
    constructor(
      public id: number,
      public pollutionType: string,
      public description: string
    ) {}
}
