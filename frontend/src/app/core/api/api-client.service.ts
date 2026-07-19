import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApiConfig } from '../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class ApiClient {

  protected readonly http = inject(HttpClient);

  protected get<T>(
    url: string,
    params?: Record<string, unknown>
  ): Observable<T> {

    return this.http.get<T>(
      ApiConfig.baseUrl + url,
      {
        params: this.buildParams(params)
      }
    );

  }

  protected post<T>(
    url: string,
    body?: unknown
  ): Observable<T> {

    return this.http.post<T>(
      ApiConfig.baseUrl + url,
      body
    );

  }

  protected put<T>(
    url: string,
    body?: unknown
  ): Observable<T> {

    return this.http.put<T>(
      ApiConfig.baseUrl + url,
      body
    );

  }

  protected delete<T>(
    url: string
  ): Observable<T> {

    return this.http.delete<T>(
      ApiConfig.baseUrl + url
    );

  }

  private buildParams(
    params?: Record<string, unknown>
  ): HttpParams {

    let httpParams = new HttpParams();

    if (!params) {
      return httpParams;
    }

    Object.entries(params).forEach(([key, value]) => {

      if (value !== null && value !== undefined) {

        httpParams = httpParams.set(
          key,
          String(value)
        );

      }

    });

    return httpParams;

  }

}
