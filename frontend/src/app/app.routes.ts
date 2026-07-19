import { Routes } from '@angular/router';

import { PUBLIC_ROUTES } from './layouts/public/public.routes';
import { AUTH_ROUTES } from './features/auth/auth.routes';
import { DASHBOARD_ROUTES } from './features/dashboard/dashboard.routes';

export const routes: Routes = [

  ...PUBLIC_ROUTES,

  ...AUTH_ROUTES,

  ...DASHBOARD_ROUTES,

  {
    path: '**',
    redirectTo: 'login'
  }

];
