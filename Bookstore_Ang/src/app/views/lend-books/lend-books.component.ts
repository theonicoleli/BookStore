import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { BooksService } from '../../services/books.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Book } from '../../services/models/Book';
import { User } from '../../services/models/User';
import { UsersService } from '../../services/users.service';

@Component({
  selector: 'app-lend-books',
  templateUrl: './lend-books.component.html',
  styleUrl: './lend-books.component.css'
})
export class LendBooksComponent {

  bookId: number = 0;
  book: Book = {} as Book;
  user: User = {} as User;

  constructor(
    private session: AuthenticationService, 
    private bookService: BooksService,
    private userService: UsersService,
    private route: ActivatedRoute,
    private router: Router
    ) 
    {}

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
            console.log("Error ao encontrar usuário utilizando livro.")
          }
        );
      }, 
      (error: any) => {
        console.log("Erro ao achar livro.")
      }
    );
    
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
        this.bookService.patchStatusBook(this.bookId, authenticatedUser.id, true).subscribe(
          (data) => {
            console.log("Alteração concluída com sucesso!");
            this.router.navigate(["/books"]);
          },
          (error) => {
            console.error("Ocorreu uma falha com a alteração do status do livro:", error);
          }
        );
      }
    }
  }

  getText(): string {
    if (this.user.id === this.session.getAuthenticatedUser()?.id) {
      return "Devolver Livro";
    }
    return "Livro já emprestado";
  }

  giveBack() {
    if (this.book.status === false && this.user == this.session.getAuthenticatedUser()) {
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
            this.router.navigate(["/books"]);
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

}
