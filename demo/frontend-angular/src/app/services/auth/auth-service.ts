import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { tap } from 'rxjs';

export interface LoginResponse {
  message: string;
  username: string;
}

export interface SignInResponseDTO {
  token: string;
  roles: string[];
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/auth';

  private readonly _isAuthenticated = signal<boolean>(false);
  readonly isAuthenticated = this._isAuthenticated.asReadonly();

  login(username: string, password: string) {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { username, password });
  }

  verifyMfa(username: string, code: string) {
    return this.http.post<SignInResponseDTO>(`${this.apiUrl}/verify-mfa`, { username, code }).pipe(
      tap((response) => {
        localStorage.setItem('jwt_token', response.token);
        this._isAuthenticated.set(true);
      }),
    );
  }

  logout() {
    localStorage.removeItem('jwt_token');
    this._isAuthenticated.set(false);
  }
}
