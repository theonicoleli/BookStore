import { Component } from '@angular/core';
import { Book } from '../../services/models/Book';
import { BooksService } from '../../services/books.service';
import { AuthenticationService } from '../../services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-borrowed',
  templateUrl: './borrowed.component.html',
  styleUrl: './borrowed.component.css'
})
export class BorrowedComponent {

  books: Book[] = [];

  constructor(
    private bookService: BooksService, 
    private session: AuthenticationService,
    private router: Router
    ) 
    {}

  ngOnInit() {
    const authenticatedUser = this.session.getAuthenticatedUser();
  
    if (typeof sessionStorage !== 'undefined' && authenticatedUser !== null) {
      this.bookService.getAllBooksByUserId(authenticatedUser.id).subscribe(
        (data) => {
          this.books = data;
        },
        (error) => {
          console.error("Erro ao encontrar livros com o usuário desta id:", error);
        }
      );
    }
  }

  lendBook(bookId?: number) {
    this.router.navigate(['/lend/' + bookId])
  }

}
