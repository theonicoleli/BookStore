import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from '../../services/models/User';
import { Message } from '../../services/models/Message';
import { MessageService } from '../../services/message.service';
import { UsersService } from '../../services/users.service';
import { FriendshipService } from '../../services/friendship.service';
import { AuthenticationService } from '../../services/authentication.service';
import { interval, Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-chat-component',
  templateUrl: './chat-component.component.html',
  styleUrls: ['./chat-component.component.css']
})
export class ChatComponentComponent {

  userChat: User = {} as User;
  messages: Message[] = [];
  newMessageText: string = '';

  friendShipId: number = 0;

  timerSubscription: Subscription | undefined;

  constructor(
    private route: ActivatedRoute,
    private messageService: MessageService,
    private userService: UsersService,
    private friendShipService: FriendshipService,
    protected session: AuthenticationService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['username']) {
        this.userService.getUserByUserName(params['username']).subscribe(
          (user: User) => {
            this.userChat = user;
            this.getFriendShipId();
          }
        );
      }
    });

    this.timerSubscription = interval(2000).pipe(
      switchMap(() => this.messageService.getAllMessagesByFriendshipId(this.friendShipId))
    ).subscribe(
      (messages: Message[]) => {
        this.messages = messages;
      },
      (error) => {
        console.error('Erro ao buscar mensagens:', error);
      }
    );
  }

  ngOnDestroy(): void {
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
  }

  getFriendShipId() {
    this.friendShipService.getFriendshipId(this.userChat.userName, this.session.getAuthenticatedUser()?.userName).subscribe(
      (friendShipId) => {
        this.friendShipId = friendShipId;
      },
      () => {
        console.log("Falha ao buscar a id da friendship")
      },
      () => {
        this.messageService.getAllMessagesByFriendshipId(this.friendShipId).subscribe(
          (messages: Message[]) => {
            this.messages = messages;
          }
        );
      }
    );
  }

  sendMessage(): void {
    if (this.newMessageText.trim() === '') {
      return;
    }

    const newMessage: Message = {
      friendshipId: this.friendShipId,
      content: this.newMessageText,
      timestamp: new Date(),
      senderId: this.session.getAuthenticatedUser()?.id,
      receiverId: this.userChat.id,
    };

    this.messageService.createMessage(newMessage).subscribe(
      (message: Message) => {
        this.messages.push(message);
        this.newMessageText = '';
      }
    );
  }

  formatTimestamp(timestamp: any): string {
    const date = new Date(timestamp);
    return `${date.getHours()}:${date.getMinutes()} ${date.getHours() >= 12 ? 'PM' : 'AM'}`;
  }

  getUserMessage(message: Message) {
    if (message.senderId === this.userChat.id) {
      return this.userChat.name;
    }
    return this.session.getAuthenticatedUser()?.name;
  }

}
