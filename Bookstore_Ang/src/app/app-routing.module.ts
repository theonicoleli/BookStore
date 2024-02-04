import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './views/home/home.component';
import { BooksComponent } from './views/books/books.component';
import { LoginComponent } from './views/login/login.component';
import { AddBookComponent } from './views/add-book/add-book.component';
import { BorrowedComponent } from './views/borrowed/borrowed.component';
import { LendBooksComponent } from './views/lend-books/lend-books.component';
import { SignUpComponent } from './views/sign-up/sign-up.component';

const routes: Routes = [
  { path: "", component: HomeComponent },
  { path: "books", component: BooksComponent },
  { path: "login", component: LoginComponent },
  { path: "books/add", component: AddBookComponent},
  { path: "books/edit/:id", component: AddBookComponent},
  { path: "borrowed", component: BorrowedComponent},
  { path: "lend/:id", component: LendBooksComponent},
  { path: "signup", component: SignUpComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
