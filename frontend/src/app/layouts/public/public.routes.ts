import { Routes } from '@angular/router';

import { PublicLayoutComponent } from './public-layout.component';

export const PUBLIC_ROUTES: Routes = [

  {
    path: '',
    component: PublicLayoutComponent,

    children: [

      {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
      },

      {
        path: 'login',
        loadComponent: () =>
          import('../../features/auth/pages/login/login.component')
            .then(m => m.LoginComponent)
      }

    ]

  }

];
