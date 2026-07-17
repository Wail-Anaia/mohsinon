import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

import { TokenService } from '../auth/token.service';

export const authGuard: CanActivateFn = () => {

  const tokenService = inject(TokenService);

  const router = inject(Router);

  if (tokenService.exists()) {

    return true;

  }

  router.navigate(['/login']);

  return false;

};
