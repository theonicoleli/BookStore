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
  @Input() withoutColor?: boolean = false;

  constructor(
    private bookService: BooksService,
    private saveBookService: SavebookService, 
    private session: AuthenticationService,
    private router: Router
    ) 
    {}

  ngOnInit() {
    if (!this.imagePath.startsWith("assets/img/")) {
      this.imagePath = "assets/img/" + this.imagePath;
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
    if (this.withoutColor) {
      this.saveBookService.deleteSavedBook(this.session.getAuthenticatedUser()?.id, this.edit).subscribe(
        (data) => {
          alert("Este livro não está mais salvo.");
          this.withoutColor = false;
        }
      );
    } else {
      this.saveBookService.addSavedBook(this.session.getAuthenticatedUser()?.id, this.edit).subscribe(
        (data) => {
          alert("Livro salvo!");
          this.withoutColor = true;
        },
        (error) => {
          alert("Erro ao salvar livro.");
        }
      );
    }
  }

  lendBook(bookId?: number) {
    this.router.navigate(['/lend/' + bookId])
  }

}
  
