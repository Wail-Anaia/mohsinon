import { Injectable, signal, computed } from '@angular/core';

import { CurrentUser } from '../models/current-user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthState {

  private readonly _token = signal<string | null>(null);

  private readonly _currentUser =
    signal<CurrentUser | null>(null);

  readonly token = this._token.asReadonly();

  readonly currentUser =
    this._currentUser.asReadonly();

  readonly authenticated = computed(() =>
    this._token() !== null
  );

  setToken(token: string): void {

    this._token.set(token);

  }

  clearToken(): void {

    this._token.set(null);

  }

  setCurrentUser(user: CurrentUser): void {

    this._currentUser.set(user);

  }

  clearCurrentUser(): void {

    this._currentUser.set(null);

  }

  clear(): void {

    this.clearToken();

    this.clearCurrentUser();

  }

}
