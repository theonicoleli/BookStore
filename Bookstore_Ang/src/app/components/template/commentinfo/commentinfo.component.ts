import { Component, Input } from '@angular/core';
import { User } from '../../../services/models/User';
import { Comment } from '../../../services/models/Comment';
import { CommentService } from '../../../services/comment.service';
import { AuthenticationService } from '../../../services/authentication.service';

@Component({
  selector: 'app-commentinfo',
  templateUrl: './commentinfo.component.html',
  styleUrls: ['./commentinfo.component.css']
})
export class CommentinfoComponent {

  @Input() commentId: number = 0;
  @Input() commentText: string = '';
  @Input() commentUser?: User = {} as User;
  @Input() commentListComments?: Comment[] = [];

  constructor(
    private commentService: CommentService,
    private session: AuthenticationService
  ) {}

  ngOnInit() {
  }

  commentSessionEquals(): boolean {
    if (this.session.getAuthenticatedUser()?.id == this.commentUser?.id) {
      return true;
    }
    return false;
  }

  deleteComment() {
    if (confirm("Deseja remover este comentário?")) {
      this.commentService.deleteComment(this.commentId).subscribe(
        (data) => {
          console.log("Comentário deletado com sucesso!");
          window.location.reload();
        },
        (error) => {
          console.error("Não foi possível deletar este comentário.")
        }
      )
    }
  }
}
