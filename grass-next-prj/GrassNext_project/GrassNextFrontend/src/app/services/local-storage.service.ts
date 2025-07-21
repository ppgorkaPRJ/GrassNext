import { Injectable } from '@angular/core';

/**
 * Service to manage local storage operations in a consistent and reusable manner.
 *
 */
@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {
  /**
   * Stores a key-value pair in the local storage.
   *
   * @param {string} key - The key under which the value will be stored.
   * @param {any} value - The value to be stored.
   *
   */
  setItem(key: string, value: any) {
    localStorage.setItem(key, value);
  }

  /**
   * Retrieves an item from local storage based on the provided key.
   *
   * @param {string} key - The key corresponding to the item to retrieve from local storage.
   *
   */
  getItem(key: string): any {
    return localStorage.getItem(key);
  }

  /**
   * Stores a boolean value in localStorage after converting it to a string representation.
   *
   * @param {string} key - The key under which the value will be stored in localStorage.
   * @param {boolean} value - The boolean value to be stored.
   *
   */
  setBool(key: string, value: boolean) {
    localStorage.setItem(key, String(value));
  }

  /**
   * Retrieves a boolean value from localStorage based on the given key.
   *
   * @param {string} key - The key used to retrieve the stored boolean value.
   *
   */
  getBool(key: string): boolean {
    return localStorage.getItem(key) === 'true';
  }

  /**
   * Stores an object in the browser's local storage after converting it to a JSON string.
   *
   * @param {string} key - The key under which the object will be stored in local storage.
   * @param {object} value - The object to be stored in local storage.
   *
   */
  setObject(key: string, value: object) {
    localStorage.setItem(key, JSON.stringify(value));
  }

  /**
   * Retrieves an object from local storage by its key.
   *
   * @param {string} key - The key associated with the object in local storage.
   *
   */
  getObject(key: string): object {
    return JSON.parse(localStorage.getItem(key));
  }
}
