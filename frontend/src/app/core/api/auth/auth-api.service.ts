import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClient } from '../api-client.service';

import { LoginRequest } from '../../../features/auth/models/login-request.model';
import { LoginResponse } from '../../../features/auth/models/login-response.model';

import { ApiConfig } from '../../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class AuthApiService extends ApiClient {

  login(
    request: LoginRequest
  ): Observable<LoginResponse> {

    return this.post<LoginResponse>(
      ApiConfig.endpoints.auth + '/login',
      request
    );

  }

}
