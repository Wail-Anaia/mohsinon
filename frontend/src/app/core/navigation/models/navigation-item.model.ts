import { NavigationGroup } from '../enums/navigation-group.enum';

export interface NavigationItem {

  id: string;

  title: string;

  icon: string;

  route: string;

  group: NavigationGroup;

  permission?: string;

  children?: NavigationItem[];

}
