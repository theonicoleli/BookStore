import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './views/home/home.component';
import { BooksComponent } from './views/books/books.component';
import { LoginComponent } from './views/login/login.component';
import { AddBookComponent } from './views/add-book/add-book.component';

const routes: Routes = [
  { path: "", component: HomeComponent },
  { path: "books", component: BooksComponent },
  { path: "login", component: LoginComponent },
  { path: "books/add", component: AddBookComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
