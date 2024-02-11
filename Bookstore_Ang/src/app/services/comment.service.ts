import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './models/User';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  url: string = 'http://localhost:8080/api/comments'
  @Output() commentChange: EventEmitter<void> = new EventEmitter<void>();

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

  patchComment(commentId?: number, userId?: number): Observable<any> {
    return this.http.patch<any>(`${this.url}/${commentId}/like/${userId}`, null);
  }  

  getCommentLikes(commentId?: number): Observable<any> {
    return this.http.get(`${this.url}/likes/${commentId}`);
  }

  getUserCommentLikes(commentId?:number): Observable<any> {
    return this.http.get(`${this.url}/liked-users/${commentId}`);
  }

  getUserIdsCommentLikes(commentId?: number): Observable<any> {
    return this.http.get(`${this.url}/liked-id/${commentId}`);
  }

  hasUserLikedComment(commentId?: number, userId?: number): Observable<any> {
    return this.http.get(`${this.url}/comments/${commentId}/likedBy/${userId}`)
  }
}
