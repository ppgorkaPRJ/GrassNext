/**
 * Represents the result of a contour processing operation.
 **
 * @property {Contour[]} contours - An array of Contour objects representing the detected contours.
 * @property {number} duration - The time taken to process the contours, typically in milliseconds.
 * @property {boolean} error - Indicates whether an error occurred during processing.
 * @property {string} msg - A message providing details about the operation or any encountered error.
 */
export interface Contours {
  contours: Contour[];
  duration: number;
  error: boolean;
  msg: string;
}

/**
 * Represents a contour line or shape defined by points and associated with a specific color and threshold.
 *
 * @property {string} color - The visual representation color of the contour.
 * @property {Point[]} points - An array of points defining the vertices or defining positions of the contour.
 * @property {string} threshold - A value representing the threshold level associated with the contour.
 *
 */
export interface Contour {
  color: string;
  points: Point[];
  threshold: string;
}

/**
 * Represents a geographical point with latitude and longitude coordinates.
 *
 * @property {number} lat - The latitude of the point.
 * @property {number} lon - The longitude of the point.
 *
 */
export interface Point {
  lat: number;
  lon: number;
}
