import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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

  getUserByUserName(userName?: string) {
    return this.http.get<User>(this.url + "/profile/" + userName);
  }

  getUserById(userId: number): Observable<User> {
    return this.http.get<User>(this.url + "/" + userId);
  }

  getUserByBookId(bookId?: number): Observable<User> {
    return this.http.get<User>(this.url + "/book/" + bookId);
  }

  countUserByEmail(userEmail: string): Observable<number> {
    return this.http.get<number>(`${this.url}/count/${userEmail}`);
  }

  postUser(formData: FormData): Observable<any> {
    const headers = new HttpHeaders();
    return this.http.post(this.url, formData, { headers });
  }

  putUser(userId?: number, user?: FormData): Observable<any> {
    return this.http.put<any>(`${this.url}/${userId}`, user)
  }
  
  deleteUser(userId?: number): Observable<number> {
    return this.http.delete<number>(this.url + "/" + userId);
  }
}
