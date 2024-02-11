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
  linkUrlPerson: string = '';
  newCommentForm: FormGroup;
  color: string = "grey";
  likesNumber: number = 0;

  showContainer: boolean = false;
  showLikesUsersModal: boolean = false;

  constructor(
    private commentService: CommentService,
    protected session: AuthenticationService,
    private fb: FormBuilder
  ) {
    this.newCommentForm = this.fb.group({
      newCommentText: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.getCommentLikesNumber();
    this.colorHeartComment();
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

    this.linkUrlPerson = `/userprofile/${this.commentUser?.userName}`;

  }

  openContainerUsers() {
    this.showContainer = true;
    this.showLikesUsersModal = true;
  }

  closeLikesUsersModal() {
    this.showLikesUsersModal = false;
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
          this.commentService.commentChange.emit();
        },
        (error) => {
          console.error("Could not delete this comment.")
        }
      )
    }
  }

  getCommentLikesNumber() {
    this.commentService.getCommentLikes(this.commentId).subscribe(
      (data: number) => {
        this.likesNumber = data;
      },
      (error) => {
        console.error("Não foi encontrado o valor de curtidas");
      }
    )
  }

  colorHeartComment() {
    if (this.session.isAuthenticated()) {
      this.commentService.hasUserLikedComment(this.commentId, this.session.getAuthenticatedUser()?.id).subscribe(
        (data: boolean) => {
          if (data) {
            this.color = "red";
          }
        },
        (error) => {
          console.error("Não foi possível encontrar comentário.")
        }
      )
    }
  }

  likeComment() {
    if (this.session.isAuthenticated()) {
      this.commentService.patchComment(this.commentId, this.session.getAuthenticatedUser()?.id).subscribe(
        (data) => {
          console.log("Like do comentário alterado.");
          this.commentService.commentChange.emit();
          this.changeId();
        },
        (error) => {
          console.error("Comentário não recebeu seu like");
        }
      )
    }
  }

  changeId() {
    this.color = (this.color === "red") ? "grey" : "red";
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
          this.commentService.commentChange.emit();
        },
        (error) => {
          console.error("Could not add this comment.")
        }
      );
    }
  }
}
