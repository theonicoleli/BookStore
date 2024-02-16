import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY, Observable, interval, startWith, switchMap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  url: string = "http://localhost:8080/api/notifications";

  constructor(private http: HttpClient) { }

  getAllNotifications(): Observable<any[]> {
    return this.http.get<any[]>(this.url);
  }

  getNotificationById(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}`);
  }

  createNotification(notification: any): Observable<any> {
    return this.http.post<any>(this.url, notification);
  }

  getUserReceiverNotifications(userId?: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/receiver/${userId}`);
  }

  startPolling(userId: string, intervalTime: number): Observable<any[]> {
    const userIdNumber = Number(userId);
    if (isNaN(userIdNumber)) {
      console.error("userId não é um número válido.");
      return EMPTY;
    }
  
    return interval(intervalTime).pipe(
      startWith(0),
      switchMap(() => this.getUserReceiverNotifications(userIdNumber))
    );
  }
  
}
