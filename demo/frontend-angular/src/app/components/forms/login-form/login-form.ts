import { Component, inject } from '@angular/core';
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

type LoginForm = {
  email: FormControl<string>;
  password: FormControl<string>;
};

@Component({
  selector: 'app-login-form',
  imports: [ReactiveFormsModule,MatButtonModule,MatInputModule],
  templateUrl: './login-form.html',
  styleUrl: './login-form.scss',
})
export class LoginComponent {
  private readonly _formBuilder = inject(NonNullableFormBuilder);

  private readonly _authService = inject(AuthService);

  protected readonly loginFormGroup = this._formBuilder.group<LoginForm>({
    email: this._formBuilder.control('', [Validators.required, Validators.email]),

    password: this._formBuilder.control('', [Validators.required, Validators.minLength(6)]),
  });

  onFormSubmit(): void {
    if (this.loginFormGroup.valid) {
      console.log('Form Submitted successfully!');
      console.log('Raw Values:', this.loginFormGroup.getRawValue());
    } else {
      console.log('Form is invalid');
    }
  }
}
