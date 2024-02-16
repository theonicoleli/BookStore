import { Component } from '@angular/core';
import { AuthenticationService } from '../../../services/authentication.service';
import { Router } from '@angular/router';
import { MatSelectChange } from '@angular/material/select';
import { NotificationService } from '../../../services/notification.service';
import { Notification } from '../../../services/models/Notification';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  notifications: Notification[] = [];

  constructor(
    protected session: AuthenticationService,
    private router: Router,
    private notificationService: NotificationService
    ) {}

  onGenreChange(event: MatSelectChange) {
    const selectedGenre = event.value;
    if (selectedGenre === 'TODOS') {
      this.router.navigate(['/books']);
    } else {
      this.router.navigate(['/books/genre', selectedGenre]);
    }
  }

  getSessionProfile() {
    return this.session.getAuthenticatedUser()?.userName;
  }

  sessionIs(): boolean {
    return this.session.getAuthenticatedUser() == undefined;
  }

  routerProfile() {
    const url = "/userprofile/" + this.getSessionProfile();
    console.log(url)
    this.router.navigate([url]);
  }

  outSession() {
    if (confirm("Realmente deseja sair da sua sessão atual?")) {
      this.session.logout();
      this.router.navigate(["/"]);
    }
  }

  getNotifications() {
    if (this.session.isAuthenticated()) {
      const userId = this.session.getAuthenticatedUser()?.id;
      if (userId) {
        this.notificationService.getUserReceiverNotifications(userId).subscribe(
          (datas: any[]) => {
            if (datas && datas.length > 0) {
              for (const data of datas) {
                if (!this.notifications.some(notification => notification.id === data.id)) {
                  this.notifications.push(data);
                }
              }
            }
          },
          (error) => {
            console.error("Falha ao obter notificações do usuário.", error);
          }
        );
      }
    }
  }

}
