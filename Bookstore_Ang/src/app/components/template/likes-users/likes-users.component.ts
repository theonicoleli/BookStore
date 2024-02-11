import { Component, EventEmitter, Input, Output } from '@angular/core';
import { User } from '../../../services/models/User';
import { CommentService } from '../../../services/comment.service';
import { UsersService } from '../../../services/users.service';

@Component({
  selector: 'app-likes-users',
  templateUrl: './likes-users.component.html',
  styleUrl: './likes-users.component.css'
})
export class LikesUsersComponent {

  @Input() commentId: number = 0;
  @Output() close: EventEmitter<void> = new EventEmitter<void>();

  users: User[] = [];
  showLikesUsersModal: boolean = true;

  constructor(private commentService: CommentService, private userService: UsersService) {}

  ngOnInit() {
    this.getUserWhoLiked();
  }

  getUserWhoLiked() {
    this.commentService.getUserIdsCommentLikes(this.commentId).subscribe(
      (datas) => {
        if (datas) {
          for (const data of datas) {
            this.userService.getUserById(data).subscribe(
              (user: User) => {
                this.users.push(user);
              }
            );
          }
        }
      }, 
      (error) => {
        console.log("Erro ao encontrar usuários.")
      }
    );
  }

  closeModal() {
    this.close.emit();
  }

}
