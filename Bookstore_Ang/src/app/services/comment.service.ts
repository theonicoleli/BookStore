import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './models/User';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  url: string = 'http://localhost:8080/api/comments'

  constructor(private http: HttpClient) { }

  getBookComments(bookId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(this.url + "/book/" + bookId);
  }

  getUserComments(userId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(this.url + "/user/" + userId);
  }

  getUserBookComments(userId: number, bookId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(this.url + "/user/" + userId + "/book/" + bookId);
  }

  getCommentById(commentId: number): Observable<Comment> {
    return this.http.get<Comment>(this.url + "/" + commentId);
  }

  getUserByCommentId(commentId: number): Observable<User> {
    return this.http.get<User>(this.url + "/infouser/" + commentId);
  }

  postComment(comment: any): Observable<Comment> {
    return this.http.post<Comment>(this.url, comment);
  }

  postReplyToComment(parentCommentId: number, comment: any): Observable<Comment> {
    return this.http.post<Comment>(this.url + "/reply/" + parentCommentId, comment);
  }

  putComment(commentId: number, comment: any): Observable<any> {
    return this.http.put(this.url + "/" + commentId, comment);
  }

  deleteComment(commentId: number): Observable<any> {
    return this.http.delete(this.url + "/" + commentId);
  }
}
