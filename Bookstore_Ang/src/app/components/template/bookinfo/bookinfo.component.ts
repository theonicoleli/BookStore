import { Component, Input } from '@angular/core';
import { BooksService } from '../../../services/books.service';
import { AuthenticationService } from '../../../services/authentication.service';
import { Router } from '@angular/router';
import { SavebookService } from '../../../services/savebook.service';

@Component({
  selector: 'app-bookinfo',
  templateUrl: './bookinfo.component.html',
  styleUrls: ['./bookinfo.component.css']
})
export class BookinfoComponent {
  @Input() bookName: string = '';
  @Input() status: boolean = false;
  @Input() imagePath: string = '';
  @Input() description: string = '';
  @Input() theme: string = '';
  @Input() edit?: number = 0;

  savedColor: String = "grey";

  constructor(
    private bookService: BooksService,
    private saveBookService: SavebookService, 
    private session: AuthenticationService,
    private router: Router
    ) 
    {}

  ngOnInit() {
    this.checkUserSavedBook();
    if (this.imagePath) {
      if (!this.imagePath.startsWith("assets/img/")) {
        this.imagePath = "assets/img/" + this.imagePath;
      }
    }
  }

  checkUserSavedBook() {
    if (this.session.isAuthenticated()) {
      this.saveBookService.hasUserSavedBook(this.session.getAuthenticatedUser()?.id, this.edit).subscribe(
        (data: boolean) => {
          if (data) {
            this.savedColor = "yellow";
          }
        },
        (error) => {
          console.error("Falha ao achar livro salvo.")
        }
      )
    }
  }

  deleteOnClick(): void {
    if (confirm("Realmente deseja excluir o livro: " + this.bookName)) {
      this.bookService.deleteBookById(this.edit).subscribe(
        (data: any) => {
          console.log("Book deletado: " + data);
          window.location.reload();
        },
        (error: any) => {
          console.log("Error: " + error);
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

  changeSavedBook() {
    if (this.savedColor === "yellow") {
      this.saveBookService.deleteSavedBook(this.session.getAuthenticatedUser()?.id, this.edit).subscribe(
        (data) => {
          console.log("Este livro não está mais salvo.");
          this.changeId();
        }
      );
    } else {
      this.saveBookService.addSavedBook(this.session.getAuthenticatedUser()?.id, this.edit).subscribe(
        (data) => {
          console.log("Livro salvo!");
          this.changeId();
        },
        (error) => {
          alert("Erro ao salvar livro.");
        }
      );
    }
  }

  changeId() {
    this.savedColor = (this.savedColor === "yellow") ? "grey" : "yellow";
  }

  lendBook(bookId?: number) {
    this.router.navigate(['/lend/' + bookId])
  }

}
