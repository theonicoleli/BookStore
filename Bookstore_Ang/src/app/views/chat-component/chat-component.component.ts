import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from '../../services/models/User';
import { UsersService } from '../../services/users.service';

@Component({
  selector: 'app-chat-component',
  templateUrl: './chat-component.component.html',
  styleUrl: './chat-component.component.css'
})
export class ChatComponentComponent {

  userChat: User = {} as User;

  constructor(private route: ActivatedRoute, private userService: UsersService) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['username']) {
        this.userService.getUserByUserName(params['username']).subscribe(
          (user: User) => {
            this.userChat = user;
          },
          (error) => {
            console.error("Erro ao encontrar usuário.");
          }
        );
      }
    });
  }

}
