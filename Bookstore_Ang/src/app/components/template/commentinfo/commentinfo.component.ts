import { Component, Input } from '@angular/core';
import { User } from '../../../services/models/User';
import { Comment } from '../../../services/models/Comment';
import { CommentService } from '../../../services/comment.service';
import { AuthenticationService } from '../../../services/authentication.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-commentinfo',
  templateUrl: './commentinfo.component.html',
  styleUrls: ['./commentinfo.component.css']
})
export class CommentinfoComponent {

  @Input() bookId: number = 0;
  @Input() showComment: boolean = true;
  @Input() isReply: boolean = true;
  @Input() commentId: number = 0;
  @Input() commentText: string = '';
  @Input() commentUser?: User = {} as User;
  @Input() commentListComments?: Comment[] = [];

  showAddCommentForm: boolean = false;
  newCommentForm: FormGroup;

  constructor(
    private commentService: CommentService,
    private session: AuthenticationService,
    private fb: FormBuilder
  ) {
    this.newCommentForm = this.fb.group({
      newCommentText: ['', Validators.required]
    });
  }

  ngOnInit() {
    if (this.commentListComments) {
      this.commentListComments.forEach(comment => {
        const userObservable = this.commentService.getUserByCommentId(comment.id);

        userObservable.subscribe(
          user => {
            if (user) {
              comment.user = user as User;
            }
          },
          error => {
            console.error("Error fetching user data:", error);
          }
        );
      });
    }

  }

  commentSessionEquals(): boolean {
    return this.session.getAuthenticatedUser()?.id === this.commentUser?.id;
  }

  toggleAddCommentForm() {
    this.showAddCommentForm = !this.showAddCommentForm;
  }

  deleteComment() {
    if (confirm("Do you want to remove this comment?")) {
      this.commentService.deleteComment(this.commentId).subscribe(
        (data) => {
          console.log("Comment deleted successfully!");
          window.location.reload();
        },
        (error) => {
          console.error("Could not delete this comment.")
        }
      )
    }
  }

  addComment() {
    if (this.newCommentForm.valid) {

      const newComment: any = {
        text: this.newCommentForm.get('newCommentText')?.value,
        bookId: this.bookId,
        userId: this.session.getAuthenticatedUser()?.id
      };
      
      this.commentService.postReplyToComment(this.commentId, newComment).subscribe(
        (data) => {
          console.log("Comment added successfully!");
          window.location.reload();
        },
        (error) => {
          console.error("Could not add this comment.")
        }
      );
    }
  }
}
