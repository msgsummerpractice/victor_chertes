import { Component } from '@angular/core';
import { LoginComponent } from '../forms/login-form/login-form';

@Component({
  selector: 'app-login',
  imports: [LoginComponent],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {}
