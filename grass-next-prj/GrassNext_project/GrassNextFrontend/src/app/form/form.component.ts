import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormControl} from '@angular/forms';
import {BackendDataService} from '../services/backend-data.service';
import {VehicleGroup} from '../model/vehicle-group';
import {WeatherStability} from '../model/weather-stability';
import {PollutionType} from '../model/pollution-type';
import {Contours, Point} from '../model/contours';
import {ToggleService} from '../services/toggle.service';
import 'leaflet';
import 'leaflet-textpath';
import 'leaflet-polylinedecorator';
import {MatCalendarCellClassFunction} from '@angular/material/datepicker';
import {Detector} from '../model/detector';
import {MatSnackBar} from '@angular/material/snack-bar';
import {PlumeData} from '../model/plume-data';
import moment from 'moment';
import {LocalStorageService} from '../services/local-storage.service';
import {HttpErrorResponse} from '@angular/common/http';
import {CalendarDate} from '../model/calendar-date';
import {Polygon} from 'leaflet';

/**
 * Leaflet map
 *
 */
declare let L;

/**
 * FormComponent is an Angular component designed to manage a form interface for selecting geographical,
 * vehicle, detector, and environmental parameters. It leverages Leaflet for map-based interactions.
 */
@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrl: './form.component.css',
  encapsulation: ViewEncapsulation.None,
})
export class FormComponent implements OnInit {
  /**
   * Represents the average radius of the Earth in kilometers.
   *
   */
  EARTH_RADIUS = 6378;

  /**
   * Represents the default value for the area of the calculations.
   *
   * */
  resolutionValue: number = 2000;
  /**
   * Represents a form control for managing and validating vehicle type input in a form.
   * This control is initialized with an empty string as its default value.
   *
   */
  vehicleTypes: FormControl = new FormControl('');
  /**
   * A map representing the mapping of hour indices to corresponding hour ranges in a 24-hour format.
   *
   */
  times = new Map<number, String>([
    [1, '00:00-01:00'],
    [2, '01:00-02:00'],
    [3, '02:00-03:00'],
    [4, '03:00-04:00'],
    [5, '04:00-05:00'],
    [6, '05:00-06:00'],
    [7, '06:00-07:00'],
    [8, '07:00-08:00'],
    [9, '08:00-09:00'],
    [10, '09:00-10:00'],
    [11, '10:00-11:00'],
    [12, '11:00-12:00'],
    [13, '12:00-13:00'],
    [14, '13:00-14:00'],
    [15, '14:00-15:00'],
    [16, '15:00-16:00'],
    [17, '16:00-17:00'],
    [18, '17:00-18:00'],
    [19, '18:00-19:00'],
    [20, '19:00-20:00'],
    [21, '20:00-21:00'],
    [22, '21:00-22:00'],
    [23, '22:00-23:00'],
    [24, '23:00-24:00'],
  ]);

  /**
   * Represents the currently selected country.
   *
   */
  selectedCountry: String = '';
  /**
   * Represents the currently selected state.
   *
   */
  selectedState: String = '';
  /**
   * Represents the currently selected city.
   *
   */
  selectedCity: String = '';

  /**
   * Represents the currently selected detector.
   *
   */
  selectedDetector: Detector;
  /**
   * Represents the currently selected date.
   *
   */
  selectedDate = '';
  /**
   * Represents the currently selected time.
   *
   */
  selectedTime = null;

  /**
   * Represents the currently selected type of pollution.
   *
   * @type {PollutionType | null}
   */
  selectedPollution: PollutionType = null;
  /**
   * Represents the selected weather stability class.
   *
   */
  selectedWeatherStab: WeatherStability = null;

  /**
   * A boolean flag indicating whether the state field is disabled.
   *
   */
  isStateDisabled: boolean = true;
  /**
   * Indicates whether the city field is disabled.
   *
   */
  isCityDisabled: boolean = true;
  /**
   * A boolean variable that indicates whether the detector field is disabled.
   *
   */
  isDetectorsDisabled: boolean = true;
  /**
   * A boolean variable indicating whether the date-time field is disabled.
   *
   */
  isDateTimeDisabled: boolean = true;
  /**
   * Indicates whether the vehicle list field is disabled or not.
   *
   */
  isVehiclesListDisabled: boolean = true;
  /**
   * A boolean variable that indicates whether the calculate button is disabled.
   *
   */
  isButtonDisabled: boolean = true;

  /**
   * Represents a collection of `VehicleGroup` objects.
   *
   */
  vehicleGroups = new Array<VehicleGroup>();
  /**
   * Represents a collection of `WeatherStability` objects.
   *
   */
  weatherStabilities = new Array<WeatherStability>();
  /**
   * Represents a collection of `PollutionType` objects.
   *
   */
  pollutionTypes = new Array<PollutionType>();
  /**
   * Represents the data structure for storing information about contours displayed on the map.
   *
   */
  contoursData: Contours;

  /**
   * Represents a list of country names stored as strings.
   *
   */
  countries = new Array<String>();
  /**
   * Represents a list of state names stored as strings.
   *
   */
  states = new Array<String>();
  /**
   * Represents a list of city names stored as strings.
   *
   */
  cities = new Array<String>();

  /**
   * An array containing detector lists.
   *
   */
  detectors = new Array<Detector>();
  /**
   * Initialization value of the loop.
   *
   */
  i: number = 0;

  /**
   * Represents a custom Leaflet red pin icon using Leaflet's L.icon method..
   *
   */
  redIcon = L.icon({
    iconUrl: '../assets/pin-red-shadow.png',

    iconSize: [60, 47],
    iconAnchor: [14, 45],
  });

  /**
   * Represents a custom blue-colored map icon using Leaflet's L.icon method.
   *
   */
  blueIcon = L.icon({
    iconUrl: '../assets/pin-blue-flip.png',

    iconSize: [38, 36],
    iconAnchor: [8, 35],
  });

  /**
   * Represents a Leaflet marker that is used to visually indicate a specified location on a map.
   *
   */
  detectorMarker = L.marker([0.0, 0.0], { icon: this.redIcon });
  /**
   * Represents the starting point of a road using a geographical marker from the Leaflet library.
   * The marker is initialized with default latitude and longitude coordinates [0.0, 0.0]
   * and styled using a blue pin icon.
   *
   *
   * @type {L.Marker}
   */
  roadStartMarker = L.marker([0.0, 0.0], { icon: this.blueIcon });
  /**
   * Represents the ending point of a road using a geographical marker from the Leaflet library.
   * The marker is initialized with default latitude and longitude coordinates [0.0, 0.0]
   * and styled using a blue pin icon.
   *
   *
   * @type {L.Marker}
   */
  roadEndMarker = L.marker([0.0, 0.0], { icon: this.blueIcon });

  /**
   * Represents a polygonal area on a map using Leaflet's polygon feature.
   * The variable is used to define and manipulate a polygon by specifying
   * its geographical coordinates.
   *
   */
  areaPolygon = L.polygon([]);
  /**
   * An array of Leaflet Polygon objects representing contour polygons.
   * Each polygon in the array defines a closed shape that represents a specific contour on a map.
   *
   */
  contourPolygons: Array<L.Polygon> = new Array<L.Polygon>();
  /**
   * A list of map markers used by the detectors.
   *
   */
  detectorMarkerList: Array<L.Marker> = new Array<L.Marker>();

  /**
   * A Leaflet Polyline instance representing the road path from starting to ending point.
   *
   */
  roadPolyline = L.polyline([[0.0, 0.0]]);
  /**
   * Represents the data structure containing Gaussian Plume calculations.
   *
   */
  plumeData: PlumeData = null;

  /**
   * Represents the duration of the error bar display time in seconds.
   *
   */
  errorBarDuration = 5;

  /**
   * Indicates whether a request has been sent.
   *
   */
  isRequestSent: boolean = false;
  /**
   * Represents a list of calendar dates.
   *
   */
  dateList: CalendarDate[] = null;
  /**
   * Represents the starting date for a mat-datepicker component.
   *
   */
  startDate: Date = null;

  /**
   * A function used to determine the CSS class to apply for a given calendar cell
   * in a mat-datepicker component.
   *
   */
  dateClass: MatCalendarCellClassFunction<Date> = (cellDate, view) => {
    return '';
  };

  /**
   * Constructor for initializing the service dependencies.
   *
   * @param {BackendDataService} backendDataService - The service responsible for handling backend data operations.
   * @param {ToggleService} toggleService - The service used for toggling sidenav.
   * @param {LocalStorageService} localStorageService - The service for interacting with local storage features.
   * @param {MatSnackBar} snackBar - The service for displaying snack bar notifications.
   */
  constructor(
    public backendDataService: BackendDataService,
    public toggleService: ToggleService,
    public localStorageService: LocalStorageService,
    private snackBar: MatSnackBar
  ) {
  }

  /**
   * Initializes the component and retrieves necessary data by calling multiple backend services.
   *
   */
  ngOnInit(): void {
    this.backendDataService.getStabilityData().subscribe((response) => {
      this.weatherStabilities = response.map((item) => {
        return new WeatherStability(
          item.id,
          item.weatherStability,
          item.description
        );
      });
    });

    this.backendDataService.getPollutionData().subscribe((response) => {
      this.pollutionTypes = response.map((item) => {
        return new PollutionType(item.id, item.pollutionType, item.description);
      });
    });

    this.backendDataService.getCountryData().subscribe((response) => {
      this.countries = response.map((item) => {
        return item;
      });

      if (this.countries.length == 1) {
        this.selectedCountry = this.countries[0] as String;
        this.changeStateShowValue();
      }
    });
  }

  /**
   * Updates the selected state value and enables or disables state field based on the selected country.
   *
   */
  changeStateShowValue() {
    this.selectedState = '';
    this.changeCityShowValue();

    if (this.selectedCountry) {
      this.backendDataService
        .getStateData(this.selectedCountry)
        .subscribe((response) => {
          this.states = response.map((item) => {
            return item;
          });

          if (this.states.length == 1) {
            this.selectedState = this.states[0] as String;
            this.changeCityShowValue();
          }
        });

      this.isStateDisabled = false;
    } else {
      this.isStateDisabled = true;
    }
  }

  /**
   * Updates the selected city value and enables or disables the detector field based on the selected city.
   *
   */
  changeCityShowValue() {
    this.selectedCity = '';
    this.changeDetectorShowValue();

    if (this.selectedState) {
      this.backendDataService
        .getCityData(this.selectedCountry, this.selectedState)
        .subscribe((response) => {
          this.cities = response.map((item) => {
            return item;
          });

          if (this.cities.length == 1) {
            this.selectedCity = this.cities[0] as String;
            this.changeDetectorShowValue();
          }
        });

      this.isCityDisabled = false;
    } else {
      this.isCityDisabled = true;
    }
  }

  /**
   * Updates the selected detector value and enables or disables the date-time field based on the selected detector.
   *
   */
  changeDetectorShowValue() {
    this.detectorMarker.removeFrom(this.toggleService.map);
    this.roadStartMarker.removeFrom(this.toggleService.map);
    this.roadEndMarker.removeFrom(this.toggleService.map);
    this.areaPolygon.removeFrom(this.toggleService.map);
    this.roadPolyline.removeFrom(this.toggleService.map);

    this.removeContourPolygons();

    this.selectedDetector = null;
    this.changeDateTimeShowValue();

    if (this.selectedCity) {
      this.backendDataService
        .getDetectorData(
          this.selectedCountry,
          this.selectedState,
          this.selectedCity
        )
        .subscribe((response) => {
          this.detectors = response.map((item) => {
            return new Detector(
              item.id,
              item.locationName,
              item.topoDetectorCoordinates,
              item.roadStartCoordinates,
              item.roadEndCoordinates
            );
          });
        });

      this.isDetectorsDisabled = false;
    } else {
      this.isDetectorsDisabled = true;
    }
  }

  /**
   * Updates the selected date-time value and enables or disables the vehicle group field based on the selected date-time.
   *
   */
  changeDateTimeShowValue() {
    this.selectedDate = '';
    this.selectedTime = null;
    this.changeVehiclesShowValue();

    if (this.selectedDetector) {
      this.getDatesFromBackend();

      this.isDateTimeDisabled = false;
    } else {
      this.isDateTimeDisabled = true;
    }
  }

  /**
   * Updates the selected vehicle group value and enables or disables calculate button based on the selected vehicle groups.
   *
   */
  changeVehiclesShowValue() {
    this.vehicleGroups.length = 0;
    this.vehicleTypes.reset();
    this.enableButton();

    if (this.selectedDate && this.selectedTime) {
      this.isVehiclesListDisabled = false;

      this.backendDataService.getVehicleGroupsData(this.selectedDetector.id, moment(this.selectedDate).format('YYYY-MM-DD'), this.selectedTime.key).subscribe({
        next: (response) => {
          this.vehicleGroups = response.map((item) => {
            return new VehicleGroup(
              item.id,
              item.vehicleGroup,
              item.description
            );
          });
        },
        error: (error: HttpErrorResponse) => {
          this.isVehiclesListDisabled = true;
        },
      });
    } else {
      this.isVehiclesListDisabled = true;
    }
  }

  /**
   * Clears contour polygons and detector markers.
   *
   */
  removeContourPolygons() {
    this.contourPolygons.forEach((polygon) => {
      polygon.removeFrom(this.toggleService.map);
    });
  }

  /**
   * Fetches dates from the backend based on the selected detector and processes them into a list of `CalendarDate` objects.
   * Sets the start date and applies a custom date class logic for the calendar view.
   *
   */
  getDatesFromBackend() {
    this.backendDataService
      .getDates(this.selectedDetector.id)
      .subscribe((response) => {
        this.dateList = response.map((item) => {
          return new CalendarDate(item.day, item.month, item.year);
        });

        this.dateList.forEach(element => {
          element.isSelected = false;
        });

        var lastDate: CalendarDate = this.dateList[this.dateList.length - 1];
        this.startDate = new Date(
          lastDate.year,
          lastDate.month - 1,
          lastDate.day
        );

        this.dateClass = (cellDate, view) => {
          if (view === 'month') {
            const day = cellDate.getDate();
            const month = cellDate.getMonth();
            const year = cellDate.getFullYear();

            for (var i = 0; i < this.dateList.length; i += 1) {
              this.dateList[i].isSelected = false;
            }

            for (var i = 0; i < this.dateList.length; i += 1) {
              if (
                day === this.dateList[i].day &&
                month === this.dateList[i].month - 1 &&
                year === this.dateList[i].year &&
                this.dateList[i].isSelected === false
              ) {
                this.dateList[i].isSelected = true;
                return 'dateEnabled';
              }
            }
          }
          return '';
        };
      });
  }

  /**
   * Evaluates the selected properties and conditions to determine whether a button should be enabled.
   * The button is enabled when all required properties (selectedCountry, selectedState, selectedCity,
   * selectedDetector, selectedDate, selectedTime, selectedPollution, selectedWeatherStab, and valid vehicleTypes)
   * are set and valid.
   *
   */
  enableButton() {
    this.clearContours();

    if (
      this.selectedCountry &&
      this.selectedState &&
      this.selectedCity &&
      this.selectedDetector &&
      this.selectedDate &&
      this.selectedTime &&
      this.selectedPollution &&
      this.selectedWeatherStab &&
      this.vehicleTypes.value != null &&
      this.vehicleTypes.value.length
    ) {
      this.isButtonDisabled = false;
    } else {
      this.isButtonDisabled = true;
    }
  }

  /**
   * Calculates Gaussian Plume and displays it on the map after pressing the "Measure" button.
   *
   */
  runMeasureButton() {
    if (
      this.selectedCountry &&
      this.selectedState &&
      this.selectedCity &&
      this.selectedDetector &&
      this.selectedDate &&
      this.selectedTime &&
      this.selectedPollution &&
      this.selectedWeatherStab &&
      this.vehicleTypes.value.length
    ) {
      this.isRequestSent = true;

      this.openErrorSnackBar('System is currently processing data', '', 3000);

      var vehicleArray = new Array<number>();
      this.vehicleTypes.value.forEach((vehicle) => {
        vehicleArray.push(vehicle.id);
      });

      this.plumeData = new PlumeData(
        vehicleArray,
        this.selectedWeatherStab.id,
        this.selectedPollution.id,
        this.selectedDetector.id,
        moment(this.selectedDate).format('YYYY-MM-DD'),
        this.selectedTime.key,
        this.resolutionValue
      );
      this.clearContours();
      this.calculateAirPollution(this.plumeData);
    } else {
      this.openErrorSnackBar('Incorrect data', 'Close', 5000);
    }
  }

  /**
   * Clears contour data.
   *
   */
  clearContours() {
    if (this.contourPolygons.length != 0) {
      this.contourPolygons.forEach((polygon: Polygon) => {
        polygon.removeFrom(this.toggleService.map);
      });
    }
  }

  /**
   * Calculates air pollution based on data gathered through the form.
   *
   */
  calculateAirPollution(plumeData: PlumeData) {
    this.backendDataService.postGaussianPlumeData(this.plumeData).subscribe({
      next: (response) => {
        this.toggleService.opened = !this.toggleService.opened;
        this.isRequestSent = false;

        this.contoursData = response;
        this.placeContoursOnMap();

        this.openErrorSnackBar(
          'Simulation completed successfully!',
          'Close',
          5000
        );
      },
      error: (error: HttpErrorResponse) => {
        this.toggleService.opened = !this.toggleService.opened;
        this.isRequestSent = false;

        this.openErrorSnackBar(error.error.msg, 'Close', 5000);
      },
    });
  }

  /**
   * Places contour polygons on the map based on the calculated contour data, styles them, and attaches popups with relevant information.
   *
   */
  placeContoursOnMap() {
    for (var i = 0; i < this.contoursData.contours.length; i++) {
      this.contourPolygons.push(L.polygon([]));
    }

    for (var i = 0; i < this.contoursData.contours.length; i++) {
      var gpsPointList: [number, number][] = [];

      this.contoursData.contours[i].points.forEach((point: Point) => {
        gpsPointList.push([point.lat, point.lon]);
      });

      this.contourPolygons[i].removeFrom(this.toggleService.map);

      this.contourPolygons[i] = L.polygon(gpsPointList);
      var myStyle = {
        color: this.contoursData.contours[i].color,
        opacity: 1,
        fillOpacity: 0.07,
      };
      this.contourPolygons[i].setStyle(myStyle);
      this.contourPolygons[i].addTo(this.toggleService.map);

      this.addPopup(
        this.contourPolygons[i],
        this.contoursData.contours[i].threshold +
          '&nbsp<sup>µg</sup><span>&frasl;</span><sub>m<sup>3</sup></sub></span>'
      );
    }
  }

  /**
   * Adds a popup to the specified layer that displays the provided text when hovered over.
   *
   * @param {Object} layer - The layer to which the popup will be added.
   * @param {string} text - The text to display inside the popup.
   *
   */
  addPopup(layer, text: string) {
    layer.on('mouseover', function (e) {
      this.bindPopup(text, { className: 'my-popup' }).openPopup(e.latlng);
    });
    layer.on('mouseout', function (e) {
      this.closePopup();
    });
  }

  /**
   * Clears all form data.
   *
   */
  clearAllOptions() {
    this.selectedCountry = '';
    this.changeStateShowValue();

    this.selectedPollution = null;
    this.selectedWeatherStab = null;
    this.vehicleTypes.reset();
    this.resolutionValue = 1000;

    this.openErrorSnackBar('Data cleared', 'Close', 5000);
  }

  /**
   * Displays an error snackbar with the provided message, action, and duration.
   *
   * @param {string} message - The message to display in the snackbar.
   * @param {string} action - The label for the snackbar's action button.
   * @param {number} duration - The duration in milliseconds for which the snackbar should be visible.
   *
   */
  openErrorSnackBar(message: string, action: string, duration: number) {
    this.snackBar.open(message, action, { duration: duration });
  }

  /**
   * Centers the map on the topological detector's location and configures map elements
   * such as markers and polylines to represent the detector and associated road points.
   * Removes any previously added map elements related to the road and the detector before
   * setting up new ones. Adjusts the map view to fit the detector's location with an default zoom value.
   *
   */
  centerToTopoDetector() {
    this.roadPolyline.removeFrom(this.toggleService.map);
    this.areaPolygon.removeFrom(this.toggleService.map);
    this.roadStartMarker.removeFrom(this.toggleService.map);
    this.roadEndMarker.removeFrom(this.toggleService.map);
    this.detectorMarker.removeFrom(this.toggleService.map);

    if (this.selectedDetector != null) {
      this.detectorMarker
        .setLatLng(this.selectedDetector.getTopoDetectorPoint())
        .addTo(this.toggleService.map);
      this.roadStartMarker
        .setLatLng(this.selectedDetector.getRoadStartPoint())
        .addTo(this.toggleService.map);
      this.roadEndMarker
        .setLatLng(this.selectedDetector.getRoadEndPoint())
        .addTo(this.toggleService.map);

      var zoomValue = this.calculateAreaBox();

      this.toggleService.map.setView(
        this.selectedDetector.getTopoDetectorPoint(),
        zoomValue
      );

      this.roadPolyline = L.polyline(
        [
          this.selectedDetector.getRoadStartPoint(),
          this.selectedDetector.getRoadEndPoint(),
        ],
        { color: 'rgb(56,124,180)' }
      ).addTo(this.toggleService.map);
    }
  }

  /**
   * Calculates the area boundaries for a specified region on a map based on the provided detector's road start coordinates,
   * resolution and area size from the form. Determines the zoom level for rendering the map.
   *
   * @return {number} The zoom level based on the resolution and area boundaries. Defaults to 13 if no valid detector or start coordinates are available.
   */
  calculateAreaBox(): number {
    if (this.selectedDetector && this.selectedDetector.roadStartCoordinates) {
      this.areaPolygon.removeFrom(this.toggleService.map);

      var areaPolygonList: [number, number][] = [];

      var areaMarkerLat =
        this.selectedDetector.roadStartCoordinates.latitude +
        (-(this.resolutionValue / 2) / 1000 / this.EARTH_RADIUS) *
          (180 / Math.PI);
      var areaMarkerLon =
        this.selectedDetector.roadStartCoordinates.longitude +
        ((-(this.resolutionValue / 2) / 1000 / this.EARTH_RADIUS) *
          (180 / Math.PI)) /
          Math.cos(
            (this.selectedDetector.roadStartCoordinates.latitude * Math.PI) /
              180
          );
      areaPolygonList.push([areaMarkerLat, areaMarkerLon]);

      areaMarkerLat =
        this.selectedDetector.roadStartCoordinates.latitude +
        (-(this.resolutionValue / 2) / 1000 / this.EARTH_RADIUS) *
          (180 / Math.PI);
      areaMarkerLon =
        this.selectedDetector.roadStartCoordinates.longitude -
        ((-(this.resolutionValue / 2) / 1000 / this.EARTH_RADIUS) *
          (180 / Math.PI)) /
          Math.cos(
            (this.selectedDetector.roadStartCoordinates.latitude * Math.PI) /
              180
          );
      areaPolygonList.push([areaMarkerLat, areaMarkerLon]);

      areaMarkerLat =
        this.selectedDetector.roadStartCoordinates.latitude -
        (-(this.resolutionValue / 2) / 1000 / this.EARTH_RADIUS) *
          (180 / Math.PI);
      areaMarkerLon =
        this.selectedDetector.roadStartCoordinates.longitude -
        ((-(this.resolutionValue / 2) / 1000 / this.EARTH_RADIUS) *
          (180 / Math.PI)) /
          Math.cos(
            (this.selectedDetector.roadStartCoordinates.latitude * Math.PI) /
              180
          );
      areaPolygonList.push([areaMarkerLat, areaMarkerLon]);

      areaMarkerLat =
        this.selectedDetector.roadStartCoordinates.latitude -
        (-(this.resolutionValue / 2) / 1000 / this.EARTH_RADIUS) *
          (180 / Math.PI);
      areaMarkerLon =
        this.selectedDetector.roadStartCoordinates.longitude +
        ((-(this.resolutionValue / 2) / 1000 / this.EARTH_RADIUS) *
          (180 / Math.PI)) /
          Math.cos(
            (this.selectedDetector.roadStartCoordinates.latitude * Math.PI) /
              180
          );
      areaPolygonList.push([areaMarkerLat, areaMarkerLon]);

      this.areaPolygon = new L.Polygon(areaPolygonList);
      var myStyle = {
        color: 'rgb(56,124,180)',
        weight: 2,
        fillOpacity: 0,
      };
      this.areaPolygon.setStyle(myStyle);
      this.areaPolygon.addTo(this.toggleService.map);

      return 14;
    }

    return 13;
  }
}
