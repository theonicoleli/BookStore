import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './views/home/home.component';
import { BooksComponent } from './views/books/books.component';
import { LoginComponent } from './views/login/login.component';
import { AddBookComponent } from './views/add-book/add-book.component';
import { BorrowedComponent } from './views/borrowed/borrowed.component';
import { LendBooksComponent } from './views/lend-books/lend-books.component';
import { SignUpComponent } from './views/sign-up/sign-up.component';
import { UserConfigComponent } from './views/user-config/user-config.component';
import { EditUserComponent } from './views/edit-user/edit-user.component';
import { UserProfileComponent } from './views/user-profile/user-profile.component';
import { ProfileComponent } from './views/profile/profile.component';
import { NotificationComponent } from './views/notification/notification.component';
import { ChatComponentComponent } from './views/chat-component/chat-component.component';

const routes: Routes = [
  { path: "", component: HomeComponent },
  { path: "books", component: BooksComponent },
  { path: "books/genre/:name", component: BooksComponent },
  { path: "login", component: LoginComponent },
  { path: "books/add", component: AddBookComponent },
  { path: "books/edit/:id", component: AddBookComponent },
  { path: "borrowed", component: BorrowedComponent },
  { path: "lend/:id", component: LendBooksComponent },
  { path: "signup", component: SignUpComponent },
  { path: "userconfig", component: UserConfigComponent },
  { path: "userconfig/edit", component: EditUserComponent },
  { path: "userprofile", component: ProfileComponent },
  { path: "userprofile/:username", component: UserProfileComponent },
  { path: "notification", component: NotificationComponent},
  { path: "chat/:username", component: ChatComponentComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
