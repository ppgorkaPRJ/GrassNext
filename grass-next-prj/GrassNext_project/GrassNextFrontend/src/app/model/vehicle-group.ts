/**
 * Represents a vehicle group with associated details.
 */
export class VehicleGroup {
    /**
     * Constructs a new instance of a vehicle with specified details.
     *
     * @param {number} id - The unique identifier for the vehicle group.
     * @param {string} vehicleGroup - A field containing an HTML icon for a specified vehicle group.
     * @param {string} description - Textual description providing a vehicle category name.
     *
     */
    constructor(
      public id: number,
      public vehicleGroup: string,
      public description: string
    ) {}
}
