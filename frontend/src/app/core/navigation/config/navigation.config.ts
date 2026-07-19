import { NavigationItem } from '../models/navigation-item.model';
import { NavigationGroup } from '../enums/navigation-group.enum';

export const NAVIGATION: NavigationItem[] = [

  {
    id: 'dashboard',
    title: 'لوحة التحكم',
    icon: 'dashboard',
    route: '/dashboard',
    group: NavigationGroup.MAIN
  },

  {
    id: 'mosques',
    title: 'المساجد',
    icon: 'mosque',
    route: '/mosques',
    group: NavigationGroup.MAIN,
    permission: 'mosque.view'
  },

  {
    id: 'donations',
    title: 'التبرعات',
    icon: 'volunteer_activism',
    route: '/donations',
    group: NavigationGroup.MAIN,
    permission: 'donation.view'
  },

  {
    id: 'volunteers',
    title: 'المتطوعون',
    icon: 'groups',
    route: '/volunteers',
    group: NavigationGroup.MAIN,
    permission: 'volunteer.view'
  }

];
