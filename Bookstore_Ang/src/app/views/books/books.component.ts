import { Component, OnInit } from '@angular/core';

import { BooksService } from '../../services/books.service';
import { Book } from '../../services/models/Book';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {

  books: Book[] = [];

  constructor(private bookService: BooksService) { 
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
}
