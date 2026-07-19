import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AppState {

  readonly sidebarOpened = signal(true);

  toggleSidebar(): void {

    this.sidebarOpened.update(value => !value);

  }

  openSidebar(): void {

    this.sidebarOpened.set(true);

  }

  closeSidebar(): void {

    this.sidebarOpened.set(false);

  }

}
