/**
 * Represents a date in the calendar with additional functionality.
 */
export class CalendarDate {
  /**
   * A boolean flag indicating the selected state of an item.
   * When true, the item is considered selected. When false, the item
   * is not selected.
   */
  isSelected: boolean;

  /**
   * Constructs a new instance of a date object with the specified day, month, and year.
   *
   * @param {number} day - The day of the date.
   * @param {number} month - The month of the date.
   * @param {number} year - The year of the date.
   *
   */
  constructor(
    public day: number,
    public month: number,
    public year: number
) {}
}
