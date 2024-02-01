import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from './models/Book';

@Injectable({
  providedIn: 'root'
})
export class BooksService {

  url: string = 'http://localhost:8080/api/books';

  constructor(private http: HttpClient) { }

  getAllBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(this.url);
  }

  postNewBook(link: string, book: any): Observable<any> {
    return this.http.post(link, book);
  }

  putBook(link: string, book: any): Observable<any> {
    return this.http.put(link, book);
  }
}
