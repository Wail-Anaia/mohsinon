import { Injectable, inject } from '@angular/core';
import { tap } from 'rxjs';

import { AuthApiService } from '../../../core/api/auth/auth-api.service';
import { TokenService } from '../../../core/auth/token.service';
import { AuthState } from '../../../core/state/auth/auth.state';

import { LoginRequest } from '../models/login-request.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly api = inject(AuthApiService);

  private readonly token = inject(TokenService);

  private readonly authState = inject(AuthState);

  login(request: LoginRequest) {

    return this.api.login(request).pipe(

      tap(response => {

          this.token.set(response.token);

          this.authState.setToken(response.token);

      }),

    );

  }

  logout(): void {

    this.token.clear();

    this.authState.clear();

}

  isAuthenticated(): boolean {

    return this.token.exists();

  }

}
