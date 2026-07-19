import { Injectable } from '@angular/core';

import { NAVIGATION } from '../config/navigation.config';
import { NavigationItem } from '../models/navigation-item.model';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  getItems(): NavigationItem[] {

    return NAVIGATION;

  }

}
