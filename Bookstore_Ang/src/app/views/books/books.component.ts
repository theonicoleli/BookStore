import { Component, OnInit } from '@angular/core';

import { BooksService } from '../../services/books.service';
import { Book } from '../../services/models/Book';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';
import { SavebookService } from '../../services/savebook.service';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {

  books: Book[] = [];

  constructor(
    private bookService: BooksService,
    private router: Router,
    private session: AuthenticationService,
    private saveBookService: SavebookService
    ) 
    {}

  ngOnInit() {
    this.bookService.getAllBooks().subscribe(
      (data) => {
        this.books = data;
      },
      (error) => {
        console.error("Error fetching books:", error);
      }
    );
  }

  isBookSavedUser(book: Book): boolean {
    this.saveBookService.getAllSavedBookByUserId(this.session.getAuthenticatedUser()?.id).subscribe(
      (savedBooks: any) => {
        if (savedBooks && savedBooks.length > 0) {
          return savedBooks.some((savedBook: any) => savedBook.book === book.id);
        }
        return false;
      },
      (error) => {
        console.log("Erro ao procurar livro salvo pelo usuário.")
      }
    );
    return false;
  }

  isAdm(): boolean {
    if (this.session.getAuthenticatedUser()?.email.includes("@bookstore.com")) {
      return true;
    }
    return false;
  }
}
