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

  timePass: number = 0;
  books: Book[] = [];

  constructor(
    private bookService: BooksService,
    protected session: AuthenticationService,
    ) 
    {}

  ngOnInit() {
    this.timePass = 0;
    this.bookService.getAllBooks().subscribe(
      (data) => {
        this.books = data;
      },
      (error) => {
        console.error("Error fetching books:", error);
      }
    );
  }

  isAdm(): boolean {
    if (this.session.getAuthenticatedUser()?.email.includes("@bookstore.com")) {
      return true;
    }
    return false;
  }
}
