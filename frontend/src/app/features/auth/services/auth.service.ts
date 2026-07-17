import { Injectable, inject } from '@angular/core';
import { Observable, tap } from 'rxjs';

import { HttpClient } from '@angular/common/http';

import { LoginRequest } from '../models/login-request.model';
import { LoginResponse } from '../models/login-response.model';

import { ApiConfig } from '../../../core/config/api.config';
import { TokenService } from '../../../core/auth/token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly http = inject(HttpClient);

  private readonly tokenService = inject(TokenService);

  login(request: LoginRequest): Observable<LoginResponse> {

    return this.http.post<LoginResponse>(
      ApiConfig.baseUrl + ApiConfig.endpoints.auth + '/login',
      request
    ).pipe(

      tap(response => {

        this.tokenService.set(response.token);

      })

    );

  }

  logout(): void {

    this.tokenService.clear();

  }

  isAuthenticated(): boolean {

    return this.tokenService.exists();

  }

}
