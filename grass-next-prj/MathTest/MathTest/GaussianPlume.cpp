#define _USE_MATH_DEFINES

#include <cmath>
#include <memory.h>
#include <iostream>
#include <vector>
#include <thread>
#include <chrono>
#include <stdio.h>

#ifdef _WIN32
#define SPRINTF sprintf_s
#   else
#define SPRINTF sprintf
#   endif

#include "GaussianPlume.h"

auto jsonIn = R"(
{
    "vehicles": [
        {
            "chosen": false,
            "count": 2,
            "avgVelocity": 38.5
        },
        {
            "chosen": false,
            "count": 251,
            "avgVelocity": 36.94422310756972
        },
        {
            "chosen": false,
            "count": 38,
            "avgVelocity": 32.39473684210526
        },
        {
            "chosen": true,
            "count": 9,
            "avgVelocity": 25.444444444444443
        },
        {
            "chosen": false,
            "count": 13,
            "avgVelocity": 30.153846153846153
        }
    ],
    "wind": {
        "speed": 1.7,
        "direction": 90,
        "stability": 4
    },
    "roadStart": {
        "lon": 18.2101124,
        "lat": 50.3436049
    },
    "roadEnd": {
        "lon": 18.2120329,
        "lat": 50.3442988
    },
    "thresholds": 5,
    "pollutionType": 3,
    "emittersHeight": 0.3,
    "concentrationHeight": 2.0,
    "matrixSize": 1000,
    "cellResolution": 1.0,
    "divMatrixSide": 8
}
)";

GaussianPlume::GaussianPlume(const char* jsonIn) {
	json jsonPP = json::parse(jsonIn);

	pp.vehicles = jsonPP["vehicles"];

	pp.windSpeed = jsonPP["wind"]["speed"];
	pp.windDirection = jsonPP["wind"]["direction"];
	pp.weatherStability = jsonPP["wind"]["stability"];

	pp.roadStartLon = jsonPP["roadStart"]["lon"];
	pp.roadStartLat = jsonPP["roadStart"]["lat"];

	pp.roadEndLon = jsonPP["roadEnd"]["lon"];
	pp.roadEndLat = jsonPP["roadEnd"]["lat"];

	pp.thresholds = jsonPP["thresholds"];

	pp.pollution = jsonPP["pollutionType"];

	pp.emittersHeight = jsonPP["emittersHeight"];
	pp.concentrationHeight = jsonPP["concentrationHeight"];
	pp.resolution = jsonPP["cellResolution"];
	pp.matrixSize = jsonPP["matrixSize"];
	pp.matrixSizeStart = -(pp.matrixSize / 2);
	pp.matrixSizeEnd = (pp.matrixSize / 2) - 1;
	pp.divMatrixSide = jsonPP["divMatrixSide"];
	pp.mean = 0.0;
	pp.stdDev = 0.0;

	C1 = new double *[pp.matrixSize];
	for (int i = 0; i < pp.matrixSize; i++) {
		C1[i] = new double[pp.matrixSize];
		memset(C1[i], 0, sizeof(double) * pp.matrixSize);
	}

	minMax = new MinMax *[pp.divMatrixSide * pp.divMatrixSide];
	for (int i = 0; i < pp.divMatrixSide; i++) {
		minMax[i] = new MinMax[pp.divMatrixSide];

		for (int j = 0; j < pp.divMatrixSide; j++) {
			minMax[i][j].min = 1000000.0;
			minMax[i][j].max = 0.0;
			minMax[i][j].sum = 0.0;
		}
	}

	minMaxMatrix.min = 1000000.0;
	minMaxMatrix.max = 0.0;
	minMaxMatrix.sum = 0.0;
}

GaussianPlume::~GaussianPlume() {
	for (int i = 0; i < pp.matrixSize; i++) {
		delete[] C1[i];
	}

	for (int i = 0; i < pp.divMatrixSide; i++) {
		delete[] minMax[i];
	}

	delete[] C1, minMax;
}

json GaussianPlume::prepareJson(double secDuration) {
	json jsonOut;

	contours.error = false;
	contours.msg = "success";
	contours.duration = secDuration;

	jsonOut["error"] = contours.error;
	jsonOut["msg"] = contours.msg;
	jsonOut["duration"] = contours.duration;
	jsonOut["contours"] = contours.toJson();

	return jsonOut;
}

void GaussianPlume::gaussianPlumeModel() {
	Vehicle motorcycles = pp.vehicles[0];
	Vehicle passengerCars = pp.vehicles[1];
	Vehicle lightCommercials = pp.vehicles[2];
	Vehicle heavyGoods = pp.vehicles[3];
	Vehicle publicTransports = pp.vehicles[4];

	double* bV = new double[pp.vehicles.size()];

	switch (pp.pollution) {
	case 1:
		bV[0] = (-5.24e-5 * pow(motorcycles.avgVelocity, 3)) +
			(1.01e-2 * pow(motorcycles.avgVelocity, 2)) -
			(5.67e-1 * motorcycles.avgVelocity) +
			1.54e+1;

		bV[1] = (7.2308e-7 * pow(passengerCars.avgVelocity, 3)) +
			(3.0186e-5 * pow(passengerCars.avgVelocity, 2)) -
			(1.3833e-2 * passengerCars.avgVelocity) +
			8.4204e-1;

		bV[2] = (-5.0796e-7 * pow(lightCommercials.avgVelocity, 3)) +
			(2.0073e-4 * pow(lightCommercials.avgVelocity, 2)) -
			(2.2681e-2 * lightCommercials.avgVelocity) +
			1.0605;

		bV[3] = (-6.7894e-6 * pow(heavyGoods.avgVelocity, 3)) +
			(1.6767e-3 * pow(heavyGoods.avgVelocity, 2)) -
			(1.2667e-1 * heavyGoods.avgVelocity) +
			4.0416;

		bV[4] = (-2.15e-5 * pow(publicTransports.avgVelocity, 3)) +
			(3.17e-3 * pow(publicTransports.avgVelocity, 2)) -
			(1.61e-1 * publicTransports.avgVelocity) +
			3.32e+0;

		break;
	case 2:
		bV[0] = (4.96e-6 * pow(motorcycles.avgVelocity, 3)) -
			(6.22e-4 * pow(motorcycles.avgVelocity, 2)) +
			(2.66e-2 * motorcycles.avgVelocity) -
			2.98e-1;

		bV[1] = (-1.056e-6 * pow(passengerCars.avgVelocity, 3)) +
			(2.856e-4 * pow(passengerCars.avgVelocity, 2)) -
			(2.378e-2 * passengerCars.avgVelocity) +
			8.708e-1;

		bV[2] = (4.2504e-7 * pow(lightCommercials.avgVelocity, 3)) +
			(2.5959e-5 * pow(lightCommercials.avgVelocity, 2)) -
			(1.0673e-2 * lightCommercials.avgVelocity) +
			1.2729;

		bV[3] = (-6.1422e-5 * pow(heavyGoods.avgVelocity, 3)) +
			(1.0505e-2 * pow(heavyGoods.avgVelocity, 2)) -
			(6.2867e-1 * heavyGoods.avgVelocity) +
			1.6783e1;

		bV[4] = (-1.32e-4 * pow(publicTransports.avgVelocity, 3)) +
			(1.85e-2 * pow(publicTransports.avgVelocity, 2)) -
			(9.07e-1 * publicTransports.avgVelocity) +
			1.91e+1;

		break;
	case 3:
		bV[0] = (-6.98e-6 * pow(motorcycles.avgVelocity, 3)) +
			(1.39e-3 * pow(motorcycles.avgVelocity, 2)) -
			(9.12e-2 * motorcycles.avgVelocity) +
			3.62e+0;

		bV[1] = (-1.4513e-7 * pow(passengerCars.avgVelocity, 3)) +
			(3.8118e-5 * pow(passengerCars.avgVelocity, 2)) -
			(3.2385e-3 * passengerCars.avgVelocity) +
			1.1068e-1;

		bV[2] = (-8.2530e-7 * pow(lightCommercials.avgVelocity, 3)) +
			(1.2798e-4 * pow(lightCommercials.avgVelocity, 2)) -
			(7.1490e-3 * lightCommercials.avgVelocity) +
			1.9300e-1;

		bV[3] = (-3.8695e-6 * pow(heavyGoods.avgVelocity, 3)) +
			(6.1712e-4 * pow(heavyGoods.avgVelocity, 2)) -
			(3.4409e-2 * heavyGoods.avgVelocity) +
			8.2150e-1;

		bV[4] = (-2.90e-6 * pow(publicTransports.avgVelocity, 3)) +
			(3.80e-4 * pow(publicTransports.avgVelocity, 2)) -
			(1.74e-2 * publicTransports.avgVelocity) +
			3.58e-1;

		break;
	}

	int vehicleCountSum = 0;
	for (int i = 0; i < pp.vehicles.size(); i++) {
		vehicleCountSum += (int) pp.vehicles[i]["count"];
	}

	double distance = GaussianPlume::haversineDistance(pp.roadStartLat, pp.roadStartLon, pp.roadEndLat, pp.roadEndLon);
	double azimuth = GaussianPlume::azimuth(pp.roadStartLat, pp.roadStartLon, pp.roadEndLat, pp.roadEndLon);

	int emittersCnt = (int)distance / 10.0;

	double* x2 = new double[emittersCnt];
	double* y2 = new double[emittersCnt];
	if (azimuth > 0 && azimuth < 90) {
		double alpha = 90 - azimuth;
		double xym = 5.0;
		for (int i = 0; i < emittersCnt; i++) {
			x2[i] = cos(toRadians(alpha)) * xym;

			x2[i] = cos(toRadians(alpha)) * xym;
			y2[i] = sin(toRadians(alpha)) * xym;
			xym = xym + 10.0;
		}
	}
	else if (azimuth > 90 && azimuth < 180) {
		double alpha = azimuth - 90;
		double xym = 5.0;
		for (int i = 0; i < emittersCnt; i++) {
			x2[i] = cos(toRadians(alpha)) * xym;
			y2[i] = sin(toRadians(alpha)) * xym * -1;
			xym = xym + 10.0;
		}
	}
	else if (azimuth > 180 && azimuth < 270) {
		double alpha = 270 - azimuth;
		double xym = 5.0;
		for (int i = 0; i < emittersCnt; i++) {
			x2[i] = cos(toRadians(alpha)) * xym * -1;
			y2[i] = sin(toRadians(alpha)) * xym * -1;

			xym = xym + 10.0;
		}
	}
	else if (azimuth > 270 && azimuth < 360) {
		double alpha = 360 - azimuth;
		double xym = 5.0;
		for (int i = 0; i < emittersCnt; i++) {
			x2[i] = sin(toRadians(alpha)) * xym * -1;
			y2[i] = cos(toRadians(alpha)) * xym;
			xym = xym + 10.0;
		}
	}
	else if (azimuth == 0 || azimuth == 360) {
		double xym = 5.0;
		for (int i = 0; i < emittersCnt; i++) {
			x2[i] = 0.0;
			y2[i] = xym;
			xym = xym + 10.0;
		}
	}
	else if (azimuth == 90) {
		double xym = 5.0;
		for (int i = 0; i < emittersCnt; i++) {
			x2[i] = xym;
			y2[i] = 0.0;
			xym = xym + 10.0;
		}
	}
	else if (azimuth == 180) {
		double xym = 5.0;
		for (int i = 0; i < emittersCnt; i++) {
			x2[i] = 0.0;
			y2[i] = xym * -1;
			xym = xym + 10.0;
		}
	}
	else if (azimuth == 270) {
		double xym = 5.0;
		for (int i = 0; i < emittersCnt; i++) {
			x2[i] = xym * -1;
			y2[i] = 0.0;
			xym = xym + 10.0;
		}
	}

	double* emissions = new double[pp.vehicles.size()];
	emissions[0] = vehicleCountSum != 0 ? motorcycles.count * ((double)motorcycles.count / (double)vehicleCountSum) * bV[0] : 0;
	emissions[1] = vehicleCountSum != 0 ? passengerCars.count * ((double)passengerCars.count / (double)vehicleCountSum) * bV[1] : 0;
	emissions[2] = vehicleCountSum != 0 ? lightCommercials.count * ((double)lightCommercials.count / (double)vehicleCountSum) * bV[2] : 0;
	emissions[3] = vehicleCountSum != 0 ? heavyGoods.count * ((double)heavyGoods.count / (double)vehicleCountSum) * bV[3] : 0;
	emissions[4] = vehicleCountSum != 0 ? publicTransports.count * ((double)publicTransports.count / (double)vehicleCountSum) * bV[4] : 0;

	double chosenVehicleEmissionSum = 0;
	for (int i = 0; i < pp.vehicles.size(); i++) {
		if (pp.vehicles[i]["chosen"]) {
			chosenVehicleEmissionSum += emissions[i];
		}
	}

	double emissionPerEmitter = ((chosenVehicleEmissionSum * distance / 1000.0) * (1000.0 / 3600.0)) * (10.0 / (double)distance);

	for (int e = 0; e < emittersCnt; e++) {
		GaussianParams gp;

		gp.idx = e;
		gp.Q = emissionPerEmitter;
		gp.windSpeed = pp.windSpeed;
		gp.windDir = pp.windDirection;
		gp.x = 0;
		gp.y = 0;
		gp.z = pp.concentrationHeight;
		gp.xs = x2[e];
		gp.ys = y2[e];
		gp.H = pp.emittersHeight;
		gp.weatherStab = pp.weatherStability;
		gp.isLast = (e == (emittersCnt - 1) ? true : false);

		pp.gpv.push_back(gp);
	}

	vector<thread> threads;

	int fragmentSize = pp.matrixSize / pp.divMatrixSide;
	int xIdx = 0;
	int yIdx = 0;
	for (int ty = 0; ty < pp.divMatrixSide; ty++) {
		for (int tx = 0; tx < pp.divMatrixSide; tx++) {
			threads.emplace_back([=]() {
				calculateEmitters(tx, ty, xIdx, yIdx, (xIdx + fragmentSize), (yIdx + fragmentSize));
				});

			xIdx += fragmentSize;
		}

		xIdx = 0;
		yIdx += fragmentSize;
	}

	for (auto& th : threads) {
		th.join();
	}

	for (int ty = 0; ty < pp.divMatrixSide; ty++) {
		for (int tx = 0; tx < pp.divMatrixSide; tx++) {
			minMaxMatrix.min = minMax[ty][tx].min < minMaxMatrix.min ? minMax[ty][tx].min : minMaxMatrix.min;
			minMaxMatrix.max = minMax[ty][tx].max > minMaxMatrix.max ? minMax[ty][tx].max : minMaxMatrix.max;
			minMaxMatrix.sum += minMax[ty][tx].sum;
		}
	}

	delete[] bV, x2, y2, emissions;
}

inline void GaussianPlume::calculateEmitters(int fragmentX, int fragmentY, int xStart, int yStart, int xEnd, int yEnd) {
	for (GaussianParams gp : pp.gpv) {
		for (int col = xStart; col < xEnd; col++) {
			gp.x = pp.matrixSizeStart + col * pp.resolution;

			for (int row = yStart; row < yEnd; row++) {
				gp.y = pp.matrixSizeStart + row * pp.resolution;

				C1[row][col] += gaussianFunction(gp);

				if ( gp.isLast && C1[row][col] != 0 ) {
					minMax[fragmentX][fragmentY].min = C1[row][col] < minMax[fragmentX][fragmentY].min ? C1[row][col] : minMax[fragmentX][fragmentY].min;
					minMax[fragmentX][fragmentY].max = C1[row][col] > minMax[fragmentX][fragmentY].max ? C1[row][col] : minMax[fragmentX][fragmentY].max;
					minMax[fragmentX][fragmentY].sum += C1[row][col];
				}
			}
		}
	}
}

inline double GaussianPlume::haversineDistance(double lat1, double lon1, double lat2, double lon2) {
	double dLat = toRadians(lat2 - lat1);
	double dLon = toRadians(lon2 - lon1);

	double a = sin(dLat / 2) * sin(dLat / 2) + cos(toRadians(lat1)) * cos(toRadians(lat2)) * sin(dLon / 2) * sin(dLon / 2);

	double c = 2 * atan2(sqrt(a), sqrt(1 - a));

	return EARTH_RADIUS * c * 1000;
}

inline double GaussianPlume::azimuth(double lat1, double lon1, double lat2, double lon2) {
	double dLon = toRadians(lon2 - lon1);

	double y = sin(dLon) * cos(toRadians(lat2));
	double x = cos(toRadians(lat1)) * sin(toRadians(lat2)) - sin(toRadians(lat1)) * cos(toRadians(lat2)) * cos(dLon);

	double azimuth = toDegrees(atan2(y, x));
	return fmod(azimuth + 360, 360);
}

inline double GaussianPlume::toRadians(double degrees) {
	return degrees * (M_PI / 180.0);
};

inline double GaussianPlume::toDegrees(double radians) {
	return radians * (180.0 / M_PI);
};

inline double GaussianPlume::gaussianFunction(GaussianPlume::GaussianParams& gp) {
	double x1 = gp.x - gp.xs;
	double y1 = gp.y - gp.ys;

	double wx = sin((gp.windDir - 180) * M_PI / 180);
	double wy = cos((gp.windDir - 180) * M_PI / 180);

	double subtended = acos((wx * x1 + wy * y1) / sqrt((pow(x1, 2) + pow(y1, 2)) * (pow(wx, 2) + pow(wy, 2))));

	double hypotenuse = sqrt(pow(x1, 2) + pow(y1, 2));
	double downwind = cos(subtended) * hypotenuse;

	if (downwind > 0) {
		Sigmas sigmas = { 0 };
		calculateSigmas(downwind, gp.weatherStab, sigmas);

		return (gp.Q / (2 * M_PI * gp.windSpeed * sigmas.y * sigmas.z)) *
			(exp(((-1) * pow(sin(subtended) * hypotenuse, 2)) / (2 * pow(sigmas.y, 2)))) *
			(
				exp((-1) * pow((gp.z - gp.H), 2) / (2 * pow(sigmas.z, 2))) +
				exp((-1) * pow((gp.z + gp.H), 2) / (2 * pow(sigmas.z, 2)))
				);
	}
	else {
		return 0;
	}
}

inline void GaussianPlume::calculateSigmas(double x, int weatherStab, GaussianPlume::Sigmas& sigmas) {
	switch (weatherStab) {
	case 1:
	case 2:
		sigmas.z = 0.24 * x * pow(1 + 0.001 * x, -0.5);
		sigmas.y = 0.32 * x * pow(1 + 0.0004 * x, -0.5);
		break;
	case 3:
		sigmas.z = x * 0.20;
		sigmas.y = 0.22 * x * pow(1 + 0.0004 * x, -0.5);
		break;
	case 4:
		sigmas.z = 0.14 * x * pow(1 + 0.0003 * x, -0.5);
		sigmas.y = 0.16 * x * pow(1 + 0.0004 * x, -0.5);
		break;
	case 5:
	case 6:
		sigmas.z = 0.08 * x * pow(1 + 0.0015 * x, -0.5);
		sigmas.y = 0.11 * x * pow(1 + 0.0004 * x, -0.5);
		break;
	}
}

string GaussianPlume::gaussianPlume() {
	auto start = chrono::high_resolution_clock::now();
	createContours();
	auto stop = chrono::high_resolution_clock::now();

	auto duration = chrono::duration_cast<chrono::microseconds>(stop - start);

	auto seconds = duration.count() / 1E6;
	auto fractSeconds = duration.count() % 1000000L / 1E6;
	auto secDuration = seconds + fractSeconds;

	string preparedJson = prepareJson(secDuration).dump();

	return preparedJson;
}

extern "C" jstring Java_com_grassnext_grassnextserver_jni_GrassNextJni_Echo(JNIEnv * env, jclass /* this */) {
	string stringOut = "JNI ECHO";
	return env->NewStringUTF(stringOut.c_str());
}

extern "C" jstring Java_com_grassnext_grassnextserver_jni_GrassNextJni_Count(JNIEnv * env, jclass /* this */, jstring jJsonIn) {
	char* cJsonIN = (char*)env->GetStringUTFChars(jJsonIn, NULL);

	GaussianPlume plume(cJsonIN);
	string stringOut = plume.gaussianPlume();
	jstring jJsonOut = env->NewStringUTF(stringOut.c_str());

	env->ReleaseStringUTFChars(jJsonIn, cJsonIN);
	return jJsonOut;
}

void GaussianPlume::calculateStdDev() {
	double sumSquaredDiff = 0.0;
	for (int i = 0; i < pp.matrixSize; ++i) {
		for (int j = 0; j < pp.matrixSize; ++j) {
			double diff = C1[i][j] - pp.mean;
			sumSquaredDiff += diff * diff;
		}
	}

	pp.stdDev = sqrt(sumSquaredDiff / (double)(pp.matrixSize * pp.matrixSize));
}

void GaussianPlume::findOptimalThresholds() {
	std::vector<double> thresholds;

	for (int i = 1; i <= pp.thresholds; ++i) {
		thresholds.push_back(pp.mean + i * pp.stdDev);
	}

	pp.thresholdList = thresholds;
}

void GaussianPlume::findIsolines(int contourIdx) {
	Mat binaryImage(pp.matrixSize, pp.matrixSize, CV_8U);
	for (int i = 0; i < pp.matrixSize; ++i) {
		for (int j = 0; j < pp.matrixSize; ++j) {
			binaryImage.at<uchar>(i, j) = (C1[i][j] >= contours.contourList[contourIdx].threshold) ? 255 : 0;
		}
	}

	vector<vector<Point>> contourPoints;
	findContours(binaryImage, contourPoints, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

	if (contourPoints.size() > 0) {
		int highestCntIdx = 0;
		int idx = 0;
		for (const auto& contour : contourPoints) {
			highestCntIdx = contour.size() > contourPoints[highestCntIdx].size() ? idx : highestCntIdx;
			idx++;
		}

		double new_latitude = 0.0;
		double new_longitude = 0.0;
		for (const auto& point : contourPoints[highestCntIdx]) {
			new_latitude = pp.roadStartLat + ((point.y + pp.matrixSizeStart) / 1000.0 / EARTH_RADIUS) * (180 / M_PI);
			new_longitude = pp.roadStartLon + ((point.x + pp.matrixSizeStart) / 1000.0 / EARTH_RADIUS) * (180 / M_PI) / cos(pp.roadStartLat * M_PI / 180);

			GpsPoint gps;
			gps.latitude = new_latitude;
			gps.longitude = new_longitude;
			contours.contourList[contourIdx].contourPoints.push_back(gps);
		}
	}
}

void GaussianPlume::createContours() {
	gaussianPlumeModel();

	double startT = 0.0000001;
	double endT = 0.00003;
	double stepT = 0.000001;

	string ptColor = "#4758b8";
	switch (pp.pollution) {
	case 1:
		ptColor = "#FF964F";
		break;
	case 2:
		ptColor = "#4758b8";
		break;
	case 3:
		ptColor = "#B32134";
		break;
	default:
		ptColor = "#4758b8";
		break;
	}

	vector<thread> threads;
	for (int t = 0; t < ( (endT - startT) / stepT ); t++) {
		char buffer[64 + 1] = { 0 };

		Contour c;
		c.threshold = startT + t * stepT;
		SPRINTF(buffer, "%.4f", (startT + t * stepT) * 1000);
		c.thresholdStr = string(buffer);
		c.color = ptColor;

		contours.contourList.push_back(c);

		threads.emplace_back([=]() {
			findIsolines(t);
		});
	}

	for (auto &th : threads) {
		th.join();
	}
}

int main() {
	GaussianPlume gp(jsonIn);
	auto j = gp.gaussianPlume();

	cout << endl << j << endl;
}