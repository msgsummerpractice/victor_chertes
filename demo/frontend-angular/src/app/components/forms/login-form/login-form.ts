import { Component, inject, signal } from '@angular/core';
import {
  FormBuilder,
  NonNullableFormBuilder,
  Validators,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';

import { AuthService } from '../../../services/auth/auth-service';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
type LoginForm = {
  username: FormControl<string>;
  password: FormControl<string>;
};

@Component({
  selector: 'app-login-form',
  imports: [ReactiveFormsModule, MatButtonModule, MatInputModule],
  templateUrl: './login-form.html',
  styleUrl: './login-form.scss',
})
export class LoginComponent {
  private readonly _router = inject(Router);

  private readonly _formBuilder = inject(NonNullableFormBuilder);

  private readonly _authService = inject(AuthService);

  isMfaStep = signal<boolean>(false);
  errorMessage = signal<string>('');
  currentUserUsername = '';

  protected readonly loginFormGroup = this._formBuilder.group<LoginForm>({
    username: this._formBuilder.control('', [Validators.required]),

    password: this._formBuilder.control('', [Validators.required, Validators.minLength(6)]),
  });

  protected readonly mfaCodeControl = this._formBuilder.control('', [
    Validators.required,
    Validators.minLength(6),
  ]);

  onFormSubmit(): void {
    if (this.loginFormGroup.valid) {
      this.errorMessage.set('');
      const { username, password } = this.loginFormGroup.getRawValue();

      this._authService.login(username, password).subscribe({
        next: (response) => {
          console.log('Login inițial cu succes, codul a fost generat în consolă');
          this.currentUserUsername = response.username;
          this.isMfaStep.set(true);
        },
        error: (error) => {
          console.error('Eroare la autentificare:', error.error);

          if(error.error?.error === 'Bad credentials') {
            this.errorMessage.set('Incorrect username or password');
          } else {
            this.errorMessage.set('An error occurred.Please try again.')
          }
        },
      });
    }
  }

  onMfaSubmit(): void {
    if (this.mfaCodeControl.valid) {
      this.errorMessage.set('');
      const code = this.mfaCodeControl.value;

      this._authService.verifyMfa(this.currentUserUsername, code).subscribe({
        next: (response) => {
          console.log('MFA verificat cu succes!', response);
          this._router.navigate(['/home']);
        },
        error: (error) => {
          console.error('Cod MFA invalid:', error);
          this.errorMessage.set('Invalid or expired MFA code.');
        },
      });
    }
  }
}
