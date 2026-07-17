import { environment } from '../../../environments/environment.development';

import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';

import { provideRouter } from '@angular/router';

import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from '../../app.routes';

import { authInterceptor } from '../../core/interceptors/auth.interceptor';

export const ApiConfig = {
  baseUrl: environment.apiUrl,

  endpoints: {
    auth: '/auth',

    users: '/users',

    mosques: '/mosques',

    donations: '/donations',

    permissions: '/permissions',

    permissionGroups: '/permission-groups'
  }
};


export const appConfig: ApplicationConfig = {

  providers: [

    provideBrowserGlobalErrorListeners(),

    provideRouter(routes),

    provideHttpClient(
      withInterceptors([
        authInterceptor
      ])
    )

  ]

};
