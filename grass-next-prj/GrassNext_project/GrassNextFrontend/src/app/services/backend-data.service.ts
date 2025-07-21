import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { VehicleGroup } from "../model/vehicle-group";
import { WeatherStability } from "../model/weather-stability";
import { PollutionType } from "../model/pollution-type";
import { Observable } from "rxjs";
import { Detector } from "../model/detector";
import { PlumeData } from "../model/plume-data";
import { Contours } from "../model/contours";
import { CalendarDate } from "../model/calendar-date";

/**
 * Service to interact with the backend APIs for fetching and posting various types of data.
 */
@Injectable({ providedIn: 'root' })
export class BackendDataService {
  /**
   * The base URL for the Green-Tronic API.
   *
   */
  BASE_URL: string = 'http://localhost:8787/api/';

  /**
   * Constructor for the class that sets up an HttpClient instance for making HTTP requests.
   *
   * @param {HttpClient} http An instance of the Angular HttpClient used for handling HTTP operations.
   */
  constructor(private http: HttpClient) {}

  /**
   * Fetches vehicle group data based on the specified parameters.
   *
   * @param {number} detectorId - The ID of the detector to retrieve the vehicle groups for.
   * @param {string} date - The date for which to retrieve vehicle group data, formatted as a string.
   * @param {string} time - The time for which to retrieve vehicle group data, formatted as a string.
   * @return {VehicleGroup[]} An array of vehicle groups.
   */
  getVehicleGroupsData(detectorId, date, time): Observable<VehicleGroup[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append('detectorId', detectorId.toString());
    queryParams = queryParams.append('date', date.toString());
    queryParams = queryParams.append('time', time.toString());

    return this.http.get<VehicleGroup[]>(this.BASE_URL + 'vehicle-groups', {params: queryParams});
  }

  /**
   * Fetches stability data related to weather from the API endpoint.
   *
   * @return {WeatherStability[]} An array of WeatherStability objects obtained from the API response.
   */
  getStabilityData() {
    return this.http.get<WeatherStability[]>(
      this.BASE_URL + 'weather-stabilities'
    );
  }

  /**
   * Fetches the pollution data from the server.
   *
   * @return {PollutionType[]} An array of pollution types.
   */
  getPollutionData() {
    return this.http.get<PollutionType[]>(this.BASE_URL + 'pollution-types');
  }

  /**
   * Retrieves a list of country from the server.
   *
   * @return {String[]} An array of strings representing country names.
   */
  getCountryData() {
    return this.http.get<String[]>(this.BASE_URL + 'location/countries');
  }

  /**
   * Fetches the list of state for a given country.
   *
   * @param {String} country - The name of the country for which the state is to be retrieved.
   * @return {String[]} An observable emitting an array of state names.
   */
  getStateData(country: String) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append('country', country.toString());
    return this.http.get<String[]>(this.BASE_URL + 'location/states', {
      params: queryParams,
    });
  }

  /**
   * Fetches the list of cities based on the provided country and state.
   *
   * @param {String} country The name of the country.
   * @param {String} state The name of the state.
   * @return {String[]} An array of city names.
   */
  getCityData(country: String, state: String) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append('country', country.toString());
    queryParams = queryParams.append('state', state.toString());
    return this.http.get<String[]>(this.BASE_URL + 'location/cities', {
      params: queryParams,
    });
  }

  /**
   * Retrieves detector data based on the specified location parameters.
   *
   * @param {String} country - The country to filter the detector data.
   * @param {String} state - The state to filter the detector data.
   * @param {String} city - The city to filter the detector data.
   * @return {Detector[]} An array of detectors that match the specified location parameters.
   */
  getDetectorData(country: String, state: String, city: String) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append('country', country.toString());
    queryParams = queryParams.append('state', state.toString());
    queryParams = queryParams.append('city', city.toString());

    return this.http.get<Detector[]>(this.BASE_URL + 'detectors/', {
      params: queryParams,
    });
  }

  /**
   * Sends the provided Gaussian plume data to the server for processing and retrieves contour information as a response.
   *
   * @param {PlumeData} plumeData - The data representing the Gaussian plume measurement to be posted to the server.
   * @return {Contours} The response with contour details.
   */
  postGaussianPlumeData(plumeData: PlumeData) {
    return this.http.post<Contours>(
      this.BASE_URL + 'gaussian-plume/measurement',
      plumeData
    );
  }

  /**
   * Fetches calendar dates based on the provided topology ID.
   *
   * @param {number} topoId - The ID of the topo detector for which dates are to be retrieved.
   * @return {Observable<CalendarDate[]>} An array of CalendarDate objects.
   */
  getDates(topoId: number) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append('topoId', topoId);

    return this.http.get<CalendarDate[]>(this.BASE_URL + 'data/dates', {
      params: queryParams,
    });
  }
}
