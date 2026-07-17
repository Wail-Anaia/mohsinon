import { Injectable, signal } from '@angular/core';

import { CurrentUser } from '../../features/auth/models/current-user.model';

@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {

  readonly currentUser = signal<CurrentUser | null>(null);

  set(user: CurrentUser): void {

    this.currentUser.set(user);

  }

  clear(): void {

    this.currentUser.set(null);

  }

}
