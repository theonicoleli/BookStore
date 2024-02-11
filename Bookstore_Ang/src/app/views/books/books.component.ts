import { Component, OnInit } from '@angular/core';

import { BooksService } from '../../services/books.service';
import { Book } from '../../services/models/Book';
import { ActivatedRoute, Router } from '@angular/router';
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
  genreName: string = '';

  constructor(
    private bookService: BooksService,
    protected session: AuthenticationService,
    private activatedRoute: ActivatedRoute
    ) 
    {}

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params => {
      this.genreName = params.get('name') || '';
      console.log(this.genreName);
      if (this.genreName !== '') {
        this.loadBooks();
      }
    });
    this.timePass = 0;
  }

  loadBooks() {
    if (this.genreName === "TODOS" || !this.genreName) {
      this.bookService.getAllBooksId().subscribe(
        (datas) => {
          if (datas) {
            for (const data of datas) {
              this.bookService.getBookById(data).subscribe(
                (book) => {
                  this.books.push(book);
                }
              );
            }
          }
        },
        (error) => {
          console.error("Error fetching books:", error);
        }
      );
    } else {
      this.bookService.getBookByTheme(this.genreName).subscribe(
        (data) => {
          this.books = data;
        },
        (error) => {
          console.error("Error fetching books:", error);
        }
      );
    }
  }
  

  isAdm(): boolean {
    if (this.session.getAuthenticatedUser()?.email.includes("@bookstore.com")) {
      return true;
    }
    return false;
  }
}
