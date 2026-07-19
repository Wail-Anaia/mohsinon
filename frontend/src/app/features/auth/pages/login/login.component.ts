import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';

import { AuthService } from '../../services/auth.service';

import { AppButtonComponent } from '../../../../shared/components/app-button/app-button.component';
import { AppInputComponent } from '../../../../shared/components/app-input/app-input.component';
import { AppCardComponent } from '../../../../shared/components/app-card/app-card.component';
import { AppLogoComponent } from '../../../../shared/components/app-logo/app-logo.component';
import { AuthFacade } from '../../facade/auth.facade';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AppInputComponent,
    AppButtonComponent,
    AppCardComponent,
    AppLogoComponent
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly authFacade = inject(AuthFacade);


  loading = signal(false);
  errorMessage = signal('');

  readonly loginForm = this.fb.nonNullable.group({
    usernameOrEmail: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  login(): void {

  if (this.loginForm.invalid) {
    this.loginForm.markAllAsTouched();
    return;
  }

  this.loading.set(true);
  this.errorMessage.set('');

  this.authFacade.login(
    this.loginForm.controls.usernameOrEmail.value,
    this.loginForm.controls.password.value
  )
  .pipe(
    finalize(() => this.loading.set(false))
  )
  .subscribe({
    next: () => {
      this.router.navigate(['/dashboard']);
    },
    error: (err) => {
      this.errorMessage.set(
        err.error?.message ??
        'البريد الإلكتروني أو كلمة المرور غير صحيحة'
      );
    }
  });

}

}
