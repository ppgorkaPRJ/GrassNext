package com.grassnext.grassnextserver.util;

import java.time.format.DateTimeFormatter;

/**
 * The Consts class contains a collection of constant attributes used across the application.
 *
 */
public class Consts {
    /**
     * A constant representing the number of hours for the topo data scanning through ther scheduler.
     *
     */
    public static int TOPO_HOURS = 24;

    /**
     * Represents the identifier for the header in the the topo data structure.
     *
     */
    public static int TOPO_HEADER_ID = 0;
    /**
     * Represents the identifier for the main data in the topo data structure.
     *
     */
    public static int TOPO_MAIN_DATA_ID = 1;
    /**
     * Represents the ID for the extra data in the topo data structure.
     *
     */
    public static int TOPO_EXTRA_DATA_ID = 2;
    /**
     * Represents the identifier for the "remaining time" data in the topo data structure.
     *
     */
    public static int TOPO_REMAINING_TIME_ID = 3;

    /**
     * A `DateTimeFormatter` constant used to define the pattern for formatting and parsing
     * dates in the "dd.MM.yyyy" format.
     *
     */
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    /**
     * A `DateTimeFormatter` constant used to define the pattern for formatting and parsing
     * time in the "HH:mm:ss" format.
     *
     */
    public static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    /**
     * A `DateTimeFormatter` constant used to define the pattern for formatting and parsing
     * dates and time in the "dd.MM.yyyy HH:mm:ss" format.
     *
     */
    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    /**
     * A `DateTimeFormatter` constant used to define the pattern for formatting and parsing
     * dates in the "yyyy-MM-dd" format.
     *
     */
    public static DateTimeFormatter DATE_FORMATTER_WEATHER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * A `DateTimeFormatter` constant used to define the pattern for formatting and parsing
     * dates in the "yyyy-MM-dd" format.
     *
     */
    public static DateTimeFormatter DATE_FORMATTER_WEB = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Defines the minimum threshold value used in the Gaussian Plume calculation.
     *
     */
    public static double THRESHOLD_MIN = 0.0000001;
    /**
     * Defines the maximum threshold value used in the Gaussian Plume calculation.
     *
     */
    public static double THRESHOLD_MAX = 0.000001;
    /**
     * Defines the minimum threshold step used in the Gaussian Plume calculation.
     *
     */
    public static double THRESHOLD_STEP = 0.00003;

    /**
     * Defines the default height (in meters) at which emitters are positioned
     * relative to the ground level.
     *
     */
    public static double EMITTERS_HEIGHT = 0.3;
    /**
     * Defines the default height (in meters) at which the air pollution concentration is calculated.
     *
     */
    public static double CONCENTRATION_HEIGHT = 2.0;
    /**
     * Represents the defualt resolution of a computational cell in a matrix.
     *
     */
    public static double CELL_RESOLUTION = 1.0;
    /**
     * Represents the default size of a square matrix side used for dividing or processing data through threads.
     *
     */
    public static int DIVISOR_MATRIX_SIDE = 8;
    /**
     * Represents the maximum number of thresholds for the Gaussian Plume calculations.
     *
     */
    public static int MAX_THRESHOLDS = 5;

    /**
     * A constant string used to represent the header for incorrect data.
     *
     */
    public static String INCORRECT_DATA_HEADER = "ERROR:";
}
