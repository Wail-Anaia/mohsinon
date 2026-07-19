import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../../../features/auth/services/auth.service';
import { TokenService } from '../token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthFacadeService {

  private readonly authService = inject(AuthService);
  private readonly tokenService = inject(TokenService);
  private readonly router = inject(Router);

  login(usernameOrEmail: string, password: string) {
    return this.authService.login({ usernameOrEmail, password });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return this.tokenService.exists();
  }

}
