import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BooksService } from '../../services/books.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-book',
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.css'] 
})
export class AddBookComponent implements OnInit {
  bookForm: FormGroup;
  url: string = "http://localhost:8080/api/books";

  constructor(private fb: FormBuilder, private bookService: BooksService, private router: Router) { 
    this.bookForm = this.fb.group({
      name: ['', Validators.required],
      status: [true, Validators.required],
      image: [null],
      description: ['', Validators.required],
      theme: ['', Validators.required],
    });
  }

  ngOnInit() {
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

      this.bookService.postNewBook(this.url, formData).subscribe(
        (data: any) => { 
          console.log("Enviado: " + data);
          this.router.navigate(['/books']);
        },
        (error: any) => {
          console.log("Error: " + error)
        }
      );
    }
  }
}
