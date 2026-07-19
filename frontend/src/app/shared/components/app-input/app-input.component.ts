import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormControl } from '@angular/forms';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-input',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './app-input.component.html',
  styleUrls: ['./app-input.component.scss']
})
export class AppInputComponent {

  @Input({ required: true })
  control!: FormControl;

  @Input()
  label = '';

  @Input()
  type: 'text' | 'email' | 'password' = 'text';

  @Input()
  icon?: string;

  hidePassword = true;

  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }

  get isPassword(): boolean {
    return this.type === 'password';
  }

  getErrorMessage(): string {

    if (this.control.hasError('required')) {
      return 'هذا الحقل مطلوب';
    }

    if (this.control.hasError('email')) {
      return 'البريد الإلكتروني غير صحيح';
    }

    if (this.control.hasError('minlength')) {
      return 'عدد الأحرف أقل من المطلوب';
    }

    return 'القيمة غير صحيحة';

  }

}
