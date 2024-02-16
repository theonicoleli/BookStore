import { Component, Input } from '@angular/core';
import { NotificationService } from '../../../services/notification.service';
import { Notification } from '../../../services/models/Notification';
import { FriendshipService } from '../../../services/friendship.service';

@Component({
  selector: 'app-notification-info',
  templateUrl: './notification-info.component.html',
  styleUrls: ['./notification-info.component.css']
})
export class NotificationInfoComponent {

  @Input() notification!: Notification;

  constructor(private notificationService: NotificationService, private friendShipService: FriendshipService) { }

  acceptFriendship(): void {
    if (confirm(`Você realmente quer aceitar ser amigo de ${this.notification.sender.name}?`)) {
      this.friendShipService.acceptFriendShipRequest(this.notification.id, true).subscribe(
        (data) => {
          console.log("Amizade aceita com sucesso.");
        },
        (error) => {
          console.error("Falha ao aceitar amizade.", error);
        }
      );
    }
  }

  rejectFriendship(): void {
    this.friendShipService.rejectFriendship(this.notification.id).subscribe(
      (data) => {
        console.log("Amizade recusada com sucesso.");
      },
      (error) => {
        console.error("Falha ao recusar amizade.", error);
      }
    );
  }

}
