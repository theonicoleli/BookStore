import { Component, OnInit } from '@angular/core';

import { BooksService } from '../../services/books.service';
import { Book } from '../../services/models/Book';
import { Router } from '@angular/router';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {

  books: Book[] = [];

  constructor(private bookService: BooksService, private router: Router) { 
  }

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

  lendBook(bookId?: number) {
    this.router.navigate(['/lend/' + bookId])
  }
}
