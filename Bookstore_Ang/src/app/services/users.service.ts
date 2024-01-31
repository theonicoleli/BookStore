import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './models/User';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  url: string = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.url);
  }

  getUserByEmailAndPassword(userEmail: string, userPassword: string): Observable<User> {
    const linkGetUser = "/" + userEmail + "/" + userPassword;
    
    return this.http.get<User>(this.url + linkGetUser);
  }
}
