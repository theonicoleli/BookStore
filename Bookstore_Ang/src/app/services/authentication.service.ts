import { Injectable } from '@angular/core';
import { User } from './models/User';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private authenticatedUser?: User;

  constructor() {}

  setAuthenticatedUser(user: User): void {
    this.authenticatedUser = user;
    if (typeof sessionStorage !== 'undefined') {
      sessionStorage.setItem('user', JSON.stringify(user));
    }
  }

  getAuthenticatedUser(): User | null {
    if (typeof sessionStorage !== 'undefined') {
      const storedUser = sessionStorage.getItem('user');
      return storedUser ? JSON.parse(storedUser) : null;
    }
    return null;
  }

  logout(): void {
    this.authenticatedUser = undefined;
    if (typeof sessionStorage !== 'undefined') {
      sessionStorage.removeItem('user');
    }
  }

  isAuthenticated(): boolean {
    return this.getAuthenticatedUser() !== null;
  }

}
