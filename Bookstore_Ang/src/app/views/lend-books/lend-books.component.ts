import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { BooksService } from '../../services/books.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Book } from '../../services/models/Book';
import { User } from '../../services/models/User';
import { Comment } from '../../services/models/Comment';
import { UsersService } from '../../services/users.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommentService } from '../../services/comment.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-lend-books',
  templateUrl: './lend-books.component.html',
  styleUrl: './lend-books.component.css'
})
export class LendBooksComponent {

  bookId: number = 0;
  book: Book = {} as Book;
  user: User = {} as User;
  comments: Comment[] = [];

  commentForm: FormGroup;
  newCommentText: string = '';
  getUserComplete: boolean = false;

  constructor(
    private session: AuthenticationService, 
    private bookService: BooksService,
    private userService: UsersService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private commentService: CommentService
    ) 
    {
      this.commentForm = this.formBuilder.group({
        comment: ['', Validators.required],
      });
    }

  ngOnInit() {
    const segments = this.route.snapshot.url.map(segment => segment.path);
    if (segments.includes("lend")) {
      this.route.params.subscribe(params => {
        this.bookId = +params['id'];
      });
    }

    this.bookService.getBookById(this.bookId).subscribe(
      (data: Book) => {
        this.book = data;

        this.userService.getUserByBookId(data.id).subscribe(
          (user: User) => {
            this.user = user;
          },
          (error: any) => {
          }
        );
      }, 
      (error: any) => {
        console.log("Erro ao achar livro.")
      }
    );
    
    this.getBookComments();

    this.commentService.commentChange.subscribe(() => {
      this.getBookComments();
    });
  }

  lend() {
    if (this.book.status) {
      const authenticatedUser = this.session.getAuthenticatedUser();
  
      if (!authenticatedUser) {
        console.log("Usuário não autenticado. Não é possível emprestar o livro.");
        return;
      }
    
      const confirmLend = confirm("Deseja realmente emprestar o livro?");
    
      if (confirmLend) {
        const bookId = this.bookId;
        const userId = authenticatedUser?.id;
        
        if (bookId !== undefined && userId !== undefined) {
          this.bookService.patchStatusBook(bookId, userId, true).subscribe(
            (data) => {
              console.log("Alteração concluída com sucesso!");
              this.router.navigate(["/books/genre/TODOS"]);
            },
            (error) => {
              console.error("Ocorreu uma falha com a alteração do status do livro:", error);
            }
          );
        } else {
          console.error("bookId ou userId não são números válidos.");
        }
      }
    }
  }

  getText(): string {
    if (this.user.id === this.session.getAuthenticatedUser()?.id) {
      return "Devolver Livro";
    }
    return "Livro já emprestado";
  }

  getBookComments() {
    this.commentService.getBookComments(this.bookId).subscribe(
      (data) => {
        this.comments = (data as any) as Comment[];
        
        for (const comment of this.comments) {
          this.commentService.getUserByCommentId(comment.id).subscribe(
            (user: User) => {
              comment.user = user;
              this.getUserComplete = true;
            },
            (error) => {
              console.log("Error ao achar usuário do comentário.")
            }
          );
        }
      },
      (error) => {
        console.error("Comentários do livro " + this.bookId + " não foram encontrados.");
      }
    );
  }

  testingComment(commentTrue: Comment): boolean {
    for (const comment of this.comments) {
      if (comment.replies?.some(reply => reply.id === commentTrue.id)) {
        return false;
      }
    }
    return true;
  }
  
  giveBack() {
    if (this.book.status === false && this.user.id == this.session.getAuthenticatedUser()?.id) {
      const authenticatedUser = this.session.getAuthenticatedUser();

      if (!authenticatedUser) {
        console.log("Usuário não autenticado. Não é possível devolver o livro.");
        return;
      }
    
      const confirmLend = confirm("Deseja realmente devolver o livro?");
    
      if (confirmLend) {
        this.bookService.patchStatusBook(this.bookId, authenticatedUser.id, false).subscribe(
          (data) => {
            console.log("Alteração concluída com sucesso!");
            this.router.navigate(["/books/genre/TODOS"]);
          },
          (error) => {
            console.error("Ocorreu uma falha com a alteração do status do livro:", error);
          }
        );
      }
    } else {
      alert("Você não pode emprestar um livro que já está emprestado.")
    }
  }

  onSubmit() {
    if (this.commentForm.valid) {
      const commentText = this.commentForm.get('comment')?.value;
  
      if (commentText) {
        const newComment: any = {
          text: commentText,
          bookId: this.book.id,
          userId: this.session.getAuthenticatedUser()?.id
        };
  
        console.log('New Comment:', newComment);
  
        this.commentService.postComment(newComment).subscribe(
          (createdComment) => {
            console.log('Comment added successfully', createdComment);
            this.getBookComments();
            this.commentForm.reset();
          },
          (error) => {
            console.error('Error adding comment:', error);
            if (error instanceof HttpErrorResponse) {
              console.error('Status:', error.status);
              console.error('Response body:', error.error);
            }
          }
        );
      }
    }
  }
  
}
