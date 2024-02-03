import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './models/User';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private authenticatedUser?: User;

  constructor(private http: HttpClient) {}

  setAuthenticatedUser(user: User): void {
    this.authenticatedUser = user;
    sessionStorage.setItem('user', JSON.stringify(user));
  }

  getAuthenticatedUser(): User {
    const storedUser = sessionStorage.getItem('user');
    return storedUser ? JSON.parse(storedUser) : null;
  }

  logout(): void {
    this.authenticatedUser = undefined;
    sessionStorage.removeItem('user');
  }

  isAuthenticated(): boolean {
    if (this.getAuthenticatedUser() == null) {
      return false;
    }
    return true;
  }
}
