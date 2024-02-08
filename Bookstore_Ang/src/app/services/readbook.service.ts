import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Book } from './models/Book';

@Injectable({
  providedIn: 'root'
})
export class ReadbookService {

  url: string = 'http://localhost:8080/api/read-books';

  constructor(private http: HttpClient) { }

  getReadBooksByUser(userId: number | undefined): Observable<Book[]> {
    return this.http.get<Book[]>(this.url + "/user/" + userId);
  }
}
