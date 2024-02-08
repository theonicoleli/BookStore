import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UsersService } from '../../services/users.service';
import { User } from '../../services/models/User';
import { Book } from '../../services/models/Book';
import { BooksService } from '../../services/books.service';
import { ReadbookService } from '../../services/readbook.service';
import { SavebookService } from '../../services/savebook.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  userName: string = '';
  userProfile: User | null = null;
  books: Book[] = [];
  readBooks: Book[] = [];
  savedBooks: Book[] = [];

  constructor(
    private route: ActivatedRoute, 
    private userService: UsersService, 
    private bookService: BooksService,
    private readBookService: ReadbookService,
    private savedBookService: SavebookService
    ) 
    {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['username']) {
        this.userName = params['username'];
        this.loadUserProfile();
      }
    });
  }

  loadUserProfile(): void {
    this.userService.getUserByUserName(this.userName).subscribe(
      (data: any) => {
        this.userProfile = data;

        this.getAllSavedBooksByUserId();
        this.getBooksAlreadyRead();
        
        if (data.bookList != null) {
          for (const bookList of data.bookList) {
            this.bookService.getBookById(bookList).subscribe(
              (otherDatas) => {
                this.books.push(otherDatas);
              },
              (error) => {
                console.log("Infelizmente não conseguimos achar os livros do usuário.")
              }
            )
          }
        }

        if (this.userProfile && !this.userProfile.imagePath?.startsWith("assets/img/")) {
          this.userProfile.imagePath = "assets/img/" + this.userProfile.imagePath;
        }        

      },
      (error: any) => {
        console.log("Error trying to find person with userName: " + this.userName);
      }
    )
  }

  getBooksAlreadyRead() {
    if (this.userProfile?.id !== undefined) {
      this.readBookService.getReadBooksByUser(this.userProfile.id).subscribe(
        (data: any) => {
          for(const book of data) {
            this.bookService.getBookById(book.bookId).subscribe(
              (readBook) => {
                this.readBooks.push(readBook);
              },
              (error) => {
                console.log("Erro ao tentar achar livros do usuário.");
              }
            )
          }
        },
        (error) => {
          console.log("Erro ao encontrar livros lidos do usuário.");
        }
      )
    } else {
      console.log("UserID está indefinido.");
    }
  }

  getAllSavedBooksByUserId() {
    if (this.userProfile?.id !== undefined) {
      this.savedBookService.getAllSavedBookByUserId(this.userProfile?.id).subscribe(
        (data: any) => {
          for(const book of data) {
            this.bookService.getBookById(book.bookId).subscribe(
              (readBook) => {
                this.savedBooks.push(readBook);
              },
              (error) => {
                console.log("Erro ao tentar achar livros salvos do usuário.");
              }
            );
          }
        },
        (error: any) =>  {
          console.log("Erro ao achar livros salvos.")
        }
      )
    }
  }

  saveBookContains(book: Book): boolean {
    return this.savedBooks.some(savedBook => savedBook.id === book.id);
  }

}

