import { Component } from '@angular/core';
import { Book } from '../../services/models/Book';
import { BooksService } from '../../services/books.service';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-borrowed',
  templateUrl: './borrowed.component.html',
  styleUrl: './borrowed.component.css'
})
export class BorrowedComponent {

  books: Book[] = [];

  constructor(private bookService: BooksService, private session: AuthenticationService) {}

  ngOnInit() {
    this.bookService.getAllBooksByUserId(this.session.getAuthenticatedUser().id).subscribe(
      (data) => {
        this.books = data;
      },
      (error) => {
        console.error("Erro ao encontrar livros com o usuário desta id:", error);
      }
    );
  }

}
