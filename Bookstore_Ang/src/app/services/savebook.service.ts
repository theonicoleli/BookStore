import { EventEmitter, Injectable, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, finalize } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SavebookService {

  url: string = 'http://localhost:8080/api/saved-books';

  @Output() saveBookChange: EventEmitter<void> = new EventEmitter<void>();

  constructor(private http: HttpClient) { }

  getAllSavedBookByUserId(userId?: number): Observable<any> {
    return this.http.get<any>(this.url + "/user/" + userId);
  }

  deleteSavedBook(userId?: number, bookId?: number): Observable<any> {
    return this.http.delete(`${this.url}/delete?userId=${userId}&bookId=${bookId}`).pipe(
      finalize(() => {
        this.saveBookChange.emit();
      })
    );
  }

  addSavedBook(userId?: number, bookId?: number): Observable<any> {
    return this.http.post(`${this.url}/add?userId=${userId}&bookId=${bookId}`, {}).pipe(
      finalize(() => {
        this.saveBookChange.emit();
      })
    );
  }

  hasUserSavedBook(userId?:number, bookId?: number): Observable<any> {
    return this.http.get(`${this.url}/users/${userId}/books/${bookId}/isSaved`);
  }
}
