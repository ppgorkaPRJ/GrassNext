#pragma once

#ifndef Java_JAVA_JNI_H
#define Java_JAVA_JNI_H

#include <string>
#include <vector>
#include <opencv2/opencv.hpp>

#include "jni.h"
#include "json.hpp"    

using namespace std;
using namespace cv;
using nlohmann::json;

///
/// 
/// \brief "Echo" function available in the backend server that prints a sample message to verify if the application is working
/// 
extern "C" JNIEXPORT jstring JNICALL Java_com_grassnext_grassnextserver_jni_GrassNextJni_Echo(JNIEnv * env, jclass /* this */);
///
/// 
/// \brief "Count" function available in the backend server that sends the data after computation in the form of JSON
/// 
extern "C" JNIEXPORT jstring JNICALL Java_com_grassnext_grassnextserver_jni_GrassNextJni_Count(JNIEnv * env, jclass /* this */, jstring jStr);

class Vehicle;

///
///
/// \brief The overloaded constructor of the Car class
/// \param speed - speed value of the vehicle (in segments)
/// \param carLength - the length of the car
///
class GaussianPlume {
public:
    ///
    ///
    /// \brief The constructor of the GaussainPlume class that is responsible for air pollution dispersion calculations based on given data and the Gaussian plume equation
    /// \param roadStartLat - latitude of the starting point of the road segment
    /// \param roadStartLon - longitude of the starting point of the road segment
    /// \param roadEndLat - latitude of the ending point of the road segment
    /// \param roadEndLon - longitude of the ending point of the road segment
    /// \param vehicleGroupsNum - number of chosen vehicle group (...)
    /// \param chosenVehicleTypes - array of markers describing which vehicle group was chosen for calculation
    /// \param vehicleCount - number of vehicles in each group
    /// \param vehicleAvgVelocity - average velocity of vehicles in each group
    /// \param windSpeed - wind speed in kmph
    /// \param windDirection - the direction of wind in degrees
    /// \param pollution - type of pollution chosen by the user
    /// \param emittersHeight - the height on which emitters were installed
    /// \param weatherStability - the value representing the atmospheric stability class 
    /// \param resolution - the size of one side of the calculated area (in meters)
    /// \param matrixSizeStart - the starting index of the matrix representing calculater area
    /// \param matrixSizeEnd - the ending index of the matrix representing calculater area
    /// \param concentrationHeight - the height at which the concentration of air pollution will be calculated
    /// \param contourThresholdMin - the minimal threshold for calculating isolines
    /// \param contourThresholdMax - the maximal threshold for calculating isolines
    /// \param contourThresholdStep - the step between isolines
    ///
    GaussianPlume(const char *jsonIn);
    ///
    /// 
    /// \brief Function that prepares the output data in the form of JSON
    /// 
    string gaussianPlume();
    ///
    /// 
    /// \brief The destructor of GaussianPlume class
    /// 
    ~GaussianPlume();

private:
    ///
    /// 
    /// \brief The structure defining parameters necessary to calculate sigma value as a part of the main equation
    /// \param y - container for y value of sigma
    /// \param z - container for z value of sigma
    ///
    typedef struct {
        double y;
        double z;
    } Sigmas;

    ///
    /// 
    /// \brief The intermediary structure for matrix calculations
    /// \param Q - value of emission per emitter
    /// \param windSpeed - wind speed in kmph
    /// \param windDirection - the direction of wind in degrees
    /// \param x - start of matrix fragment
    /// \param y - end of matrix fragment
    /// \param z - air pollution concentration height
    /// \param xs - a double that is part of the equation calculated based on azimuth (x axis)
    /// \param ys - a double that is part of the equation calculated based on azimuth (y axis)
    /// \param H - the height of the emitter
    /// \param weatherStab - the value representing the atmospheric stability class
    /// \param isLast - the value for checking the last emitter 
    ///
    typedef struct {
        int idx;
        double Q;
        double windSpeed;
        int windDir;
        double x;
        double y;
        double z;
        double xs;
        double ys;
        double H;
        int weatherStab;
        bool isLast;
    } GaussianParams;

    ///
    ///
    /// \brief The structure PlumeParams that contains data necessary for calculating air pollution
    /// \param roadStartLat - latitude of the starting point of the road segment
    /// \param roadStartLon - longitude of the starting point of the road segment
    /// \param roadEndLat - latitude of the ending point of the road segment
    /// \param roadEndLon - longitude of the ending point of the road segment
    /// \param windSpeed - wind speed in kmph
    /// \param windDirection - the direction of wind in degrees
    /// \param pollution - type of pollution chosen by the user
    /// \param emittersHeight - the height on which emitters were installed
    /// \param weatherStability - the value representing the atmospheric stability class 
    /// \param resolution - the size of one side of the calculated area (in meters)
    /// \param matrixSize - the size of the matrix
    /// \param matrixSizeStart - the starting index of the matrix representing calculater area
    /// \param matrixSizeEnd - the ending index of the matrix representing calculater area
    /// \param concentrationHeight - the height at which the concentration of air pollution will be calculated
    /// \param thresholds - a value of a singular treshold
    /// \param thresholdList - list of tresholds for the matrix
    /// \param divMatrixSide - the number, by which the matrix will be divided into smaller chunks for parallelization
    /// \param mean - a variable for storing mean of the data
    /// \param stdDev - a variable for storing standard deviation of the data
    /// \param an instance of the Vehicle class with data read from backend server
    /// \param gpv - a vector structure containing parameters necessary for Gaussian Plume calculation for each emitter
    ///
    typedef struct {
        double roadStartLat;
        double roadStartLon;
        double roadEndLat;
        double roadEndLon;
        double windSpeed;
        int windDirection;
        int pollution;
        double emittersHeight;
        int weatherStability;
        double resolution;
        int matrixSize;
        int matrixSizeStart;
        int matrixSizeEnd;
        int concentrationHeight;
        int thresholds;
        vector<double> thresholdList;
        int divMatrixSide;
        double mean;
        double stdDev;

        vector<nlohmann::json> vehicles;

        vector<GaussianParams> gpv;
    } PlumeParams;
    
    /// 
    /// 
    /// \brief A structure describing the matrix fragment size
    /// \param min - minimum value of the matrix
    /// \param max - maximum value of the matrix
    /// \param sum - sum of all the matrix values
    /// 
    typedef struct {
        double min;
        double max;
        double sum;
    } MinMax;

    /// 
    /// 
    /// \brief A structure describing the GPS coordinates of a point on the map
    /// \param latitude - the latitude of the GPS point
    /// \param longitude - the longitude of the GPS point
    ///
    class GpsPoint {
    public:
        double latitude;
        double longitude;

        /// 
        /// 
        /// \brief A method for converting GPS coordinates into a JSON format 
        /// \return A JSON containing latitude and a longitude of a GPS point
        /// 
        json toJson() const {
            return {
                {"lat", latitude},
                {"lon", longitude}
            };
        }
    };

    /// 
    /// 
    /// \brief A class describing the contour data for a single treshold
    /// \param threshold - the treshold value 
    /// \param thresholdStr - a string describing the treshold value
    /// \param color - color of a treshold
    /// \param contourPoints - list of countour points of the treshold
    ///
    class Contour {
    public:
        double threshold;
        string thresholdStr;
        string color;
        vector<GpsPoint> contourPoints;

        /// 
        /// 
        /// \brief A method for converting contour point data into a JSON format 
        /// \return A JSON containing contour point data
        /// 
        json contourPointsToJson() const {
            json arrayJson = json::array();

            for (const auto& p : contourPoints) {
                arrayJson.push_back(p.toJson());
            }

            return arrayJson;
        }

        /// 
        /// 
        /// \brief A method for converting threshold, color and contour points into a JSON format 
        /// \return A JSON containing threshold, color and contour points
        /// 
        json toJson() const {
            return {
                {"threshold", thresholdStr},
                {"color", color},
				{"points", contourPointsToJson()}
            };
        }
    };

    /// 
    /// 
    /// \brief A class describing the contour data for a single treshold
    /// \param error - a boolean informing if an error occured during the simulation
    /// \param msg - a descriptive message 
    /// \param duration - duration of the simulation
    /// \param contourList - list of all contours for all tresholds
    ///
    class Contours {
    public:
        bool error;
        string msg;
        double duration;
        vector<Contour> contourList;

        /// 
        /// 
        /// \brief A method converting contour list to JSON
        /// \return A JSON with a contour list
        /// 
        json contourListToJson() const {
            json arrayJson = json::array();

            for (const auto& c : contourList) {
                arrayJson.push_back(c.toJson());
            }

            return arrayJson;
        }

        /// 
        /// 
        /// \brief A method Contours object JSON
        /// \return A JSON with a Contours object
        /// 
        json toJson() const {
            return contourListToJson();
        }
    };

    ///
    /// 
    /// \brief A method converting simulation data to JSON
    ///
    json prepareJson(double secDuration);

    ///
    /// 
    /// \brief A constant representing Earth's radius
    ///
    const double EARTH_RADIUS = 6378.0;

    ///
    /// 
    /// \brief PlumeParams structure
    /// \see PlumeParams
    ///
    PlumeParams pp = { 0 };
    ///
    /// 
    /// \brief Output matrix
    ///
    double **C1;
    ///
    /// 
    /// \brief An list of the MinMax objects for a matrix array
    /// \see MinMax
    ///
    MinMax **minMax;
    ///
    /// 
    /// \brief An instance of the MinMax object for a single matrix
    /// \see MinMax
    ///
    MinMax minMaxMatrix;
    ///
    /// 
    /// \brief An instance of the Contours object for storing contour data
    /// \see Contours
    ///
    Contours contours;

    /// 
    /// 
    /// \brief Main function responsible for calculating air pollution dispersion based on arguments from PlumeParams structure
    /// \return Matrix with air pollution results for each cell
    /// 
    void gaussianPlumeModel();
    ///
    /// 
    /// \brief Function that calculates emitters for a set section of the matrix
    /// \param xStart - starting cell of the matrix (x-axis)
    /// \param xEnd - last cell of the matrix (x-axis)
    /// \param yStart - starting cell of the matrix (y-axis)
    /// \param yEnd - last cell of the matrix (y-axis)
    ///
    inline void calculateEmitters(int fragmentX, int fragmentY, int xStart, int yStart, int xEnd, int yEnd);
    ///
    /// 
    /// \brief Function that calculates Gaussian function for a part of the matrix
    /// \param gp - parameters necessary for Gaussian function calculation
    /// \see GaussianParams
    ///
    inline double gaussianFunction(GaussianParams& gp);
    ///
    /// 
    /// \brief Function that calculates y and z values of sigma as part of the Gaussian plume equation
    /// \param x - downwind value
    /// \param weatherStab - the value representing the atmospheric stability class
    /// \param sigmas - the structure that stores the result of sigma x and y values
    /// \see Sigmas
    ///
    inline void calculateSigmas(double x, int weatherStab, Sigmas& sigmas);

    ///
    /// 
    /// \brief Function that determines the angular distance between two points on a sphere using Haversine formula
    /// \param lat1 - latitude of the first point
    /// \param lon1 - longitude of the first point
    /// \param lat2 - latitude of the second point
    /// \param lon2 - longitude of the second point
    /// \return Angular distance between two points on a sphere in meters
    ///
    inline double haversineDistance(double lat1, double lon1, double lat2, double lon2);
    ///
    /// 
    /// \brief Function that determines the azimuth between two points
    /// \param lat1 - latitude of the first point
    /// \param lon1 - longitude of the first point
    /// \param lat2 - latitude of the second point
    /// \param lon2 - longitude of the second point
    /// \return Azimuth between two points in degrees
    ///
    inline double azimuth(double lat1, double lon1, double lat2, double lon2);
    ///
    /// 
    /// \brief Function converting degrees to radians
    /// \param degrees - value in degrees
    /// \return Converted value in radians
    ///
    inline double toRadians(double degrees);
    ///
    /// 
    /// \brief Function converting radians to degrees
    /// \param radians - value in radians
    /// \return Converted value in degrees
    ///
    inline double toDegrees(double radians);

    ///
    /// 
    /// \brief Function calculating the standard deviation of the matrix
    ///
    void calculateStdDev();
    ///
    /// 
    /// \brief Function for finding optimal tresholds based on matrix data
    ///
    void findOptimalThresholds();

    ///
    /// 
    /// \brief Function for finding isolines for specific contours
    ///
    void findIsolines(int contourIdx);
    ///
    /// 
    /// \brief Function for creating Contour objects based on data provided
    ///
    void createContours();
};

/// 
/// 
/// \brief A class describing the input data for vehicles 
/// \param choses - a boolean describing if vehicle was chosen for the simulation
/// \param count - a number of vehicles that passed the road during a chosen period 
/// \param avgVelocity - the average velocity of the vehicle during a chosen period
///
class Vehicle {
public:
    bool    chosen;
    int     count;
    double  avgVelocity;

    /// 
    /// 
    /// \brief The constructor of the Vehicle class that takes the data from a backend server 
    /// 
    Vehicle(const nlohmann::json& json) {
        if (json.contains("chosen") && json.contains("count") && json.contains("avgVelocity")) {
            chosen = json["chosen"];
            count = json["count"];
            avgVelocity = json["avgVelocity"];
        }
        else {
            throw invalid_argument("Lack of necessary data in JSON object.");
        }
    }
};

#endif //Java_JAVA_JNI_H
