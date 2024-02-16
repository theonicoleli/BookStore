import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { User } from './models/User';

@Injectable({
  providedIn: 'root'
})
export class FriendshipService {

  url: string = "http://localhost:8080/api/friendships";

  constructor(private http: HttpClient) { }

  getAllFriendShips(): Observable<any> {
    return this.http.get<any>(this.url);
  }

  getFriendShipsByUser(userId?: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${userId}`);
  }

  countUserFriends(userId?: number): Observable<any> {
    return this.http.get<any>(`${this.url}/countUserFriends/${userId}`);
  }

  existsFriendShip(user1Id?: number, user2Id?: number): Observable<any> {
    return this.http.get<any>(`${this.url}/existsFriendShip/${user1Id}/${user2Id}`);
  }

  sendFriendShipRequest(sender: User | null, receiver: User | null): Observable<any> {
    if(!sender || !receiver) {
      return throwError("Problema ao identificar usuários de solicitações");
    }

    const friendShipRequest = {
      senderId: sender?.id,
      receiverId: receiver?.id
    };
    return this.http.post<any>(`${this.url}/request`, friendShipRequest);
  }

  acceptFriendShipRequest(notificationId: number, response: boolean): Observable<any> {
    return this.http.post<any>(`${this.url}/accept/${notificationId}`, response);
  }

  rejectFriendship(notificationId: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${notificationId}`);
  }

  deleteFriendShip(user1Id?: number, user2Id?: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/deleteFriendShip/${user1Id}/${user2Id}`);
  }
  
}
