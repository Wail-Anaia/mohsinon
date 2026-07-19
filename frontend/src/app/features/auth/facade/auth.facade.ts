import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../services/auth.service';
import { AuthState } from '../../../core/state/auth/auth.state';

@Injectable({
  providedIn: 'root'
})
export class AuthFacade {

  private readonly authService = inject(AuthService);
  private readonly authState = inject(AuthState);
  private readonly router = inject(Router);

  login(usernameOrEmail: string, password: string) {

    return this.authService.login({
      usernameOrEmail,
      password
    });

  }

  logout(): void {

    this.authService.logout();

    this.authState.clear();

    this.router.navigate(['/login']);

  }

  get authenticated() {

    return this.authState.authenticated;

  }

  get currentUser() {

    return this.authState.currentUser;

  }

}
