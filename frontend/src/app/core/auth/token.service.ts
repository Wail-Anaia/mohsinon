import { Injectable } from '@angular/core';

import { StorageConstants } from '../../shared/constants/storage.constants';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  get(): string | null {
    return localStorage.getItem(StorageConstants.TOKEN);
  }

  set(token: string): void {
    localStorage.setItem(StorageConstants.TOKEN, token);
  }

  clear(): void {
    localStorage.removeItem(StorageConstants.TOKEN);
  }

  exists(): boolean {
    return this.get() !== null;
  }

}
