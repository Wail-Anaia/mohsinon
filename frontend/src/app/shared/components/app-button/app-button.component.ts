import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatIconModule
  ],
  templateUrl: './app-button.component.html',
  styleUrls: ['./app-button.component.scss']
})
export class AppButtonComponent {

  @Input() label = '';

  @Input() icon?: string;

  @Input() loading = false;

  @Input() disabled = false;

  @Input() type: 'button' | 'submit' | 'reset' = 'button';

  @Input()
  fullWidth = true;

  @Input()
  variant:
    | 'primary'
    | 'secondary'
    | 'success'
    | 'danger'
    | 'outlined'
    | 'text' = 'primary';

  @Input()
  size:
    | 'small'
    | 'medium'
    | 'large' = 'medium';
}
