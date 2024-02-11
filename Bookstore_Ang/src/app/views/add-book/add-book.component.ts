import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BooksService } from '../../services/books.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-add-book',
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.css']
})
export class AddBookComponent implements OnInit {
  bookForm: FormGroup;
  bookId?: number;
  url: string = "http://localhost:8080/api/books";

  constructor(
    private fb: FormBuilder,
    private bookService: BooksService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.bookForm = this.fb.group({
      name: ['', Validators.required],
      status: [true, Validators.required],
      image: [null],
      description: ['', Validators.required],
      theme: ['', Validators.required],
    });

    const segments = this.route.snapshot.url.map(segment => segment.path);
    if (segments.includes("edit")) {
      this.route.params.subscribe(params => {
        this.bookId = +params['id'];

        this.bookService.getBookById(this.bookId).subscribe(
          (data: any) => {
            this.bookForm.patchValue({
              name: data.name,
              status: data.status,
              description: data.description,
              theme: data.theme,
            });
          },
          (error: any) => {
            console.log("Error fetching book details: " + error);
          }
        );
      });
    }
  }

  ngOnInit() {
    const segments = this.route.snapshot.url.map(segment => segment.path);
    if (segments.includes("edit")) {
      this.route.params.subscribe(params => {
        this.bookId = +params['id'];
      });
    }
  }

  onFileChange(event: any) {
    const file = (event.target as HTMLInputElement).files?.[0];
    this.bookForm.patchValue({
      image: file
    });
  }

  onSubmit(): void {
    if (this.bookForm.valid) {
      const formData = new FormData();
      Object.keys(this.bookForm.value).forEach(key => {
        formData.append(key, this.bookForm.value[key]);
      });

      if (this.bookId === undefined) {
        this.bookService.postNewBook(this.url, formData).subscribe(
          (data: any) => {
            console.log("Enviado: " + data);
            this.router.navigate(['/books/genre/TODOS']);
          },
          (error: any) => {
            console.log("Error: " + error);
          }
        );
      } else {
        this.bookService.putBook('http://localhost:8080/api/books/' + this.bookId, formData).subscribe(
          (data: any) => {
            console.log("Enviado: " + data);
            this.router.navigate(['/books/genre/TODOS']);
          },
          (error: any) => {
            console.log("Error: " + error);
          }
        );
      }
    }
  }
}
