import { Component } from '@angular/core';
import { NotificationService } from '../../services/notification.service';
import { Notification } from '../../services/models/Notification';
import { Subscription } from 'rxjs';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.css'
})
export class NotificationComponent {

  notifications: Notification[] = [];
  pollingSubscription?: Subscription;

  constructor(private notificationService: NotificationService, private session: AuthenticationService) {}

  ngOnInit(): void {
    const userId = (this.session.getAuthenticatedUser()?.id ?? '').toString();
    this.pollingSubscription = this.notificationService.startPolling(userId, 5000).subscribe(
      (notifications: any[]) => {
        this.notifications = notifications;
      },
      (error) => {
        console.error("Falha ao obter notificações.", error);
      }
    );
  }

  ngOnDestroy(): void {
    if (this.pollingSubscription) {
      this.pollingSubscription.unsubscribe();
    }
  }

}
