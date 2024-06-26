import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './components/template/header/header.component';
import { FooterComponent } from './components/template/footer/footer.component';

import { MatToolbarModule} from '@angular/material/toolbar';
import { HomeComponent } from './views/home/home.component';

import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { BooksComponent } from './views/books/books.component';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

import { HttpClientModule } from '@angular/common/http';
import { BookinfoComponent } from './components/template/bookinfo/bookinfo.component';
import { LoginComponent } from './views/login/login.component';

import { ReactiveFormsModule } from '@angular/forms';
import { AddBookComponent } from './views/add-book/add-book.component';
import { BorrowedComponent } from './views/borrowed/borrowed.component';
import { LendBooksComponent } from './views/lend-books/lend-books.component';
import { SignUpComponent } from './views/sign-up/sign-up.component';
import { CommentinfoComponent } from './components/template/commentinfo/commentinfo.component';
import { UserConfigComponent } from './views/user-config/user-config.component';
import { EditUserComponent } from './views/edit-user/edit-user.component';
import { UserProfileComponent } from './views/user-profile/user-profile.component';

import { FormsModule } from '@angular/forms';
import { ProfileComponent } from './views/profile/profile.component';
import { UserinfoComponent } from './components/template/userinfo/userinfo.component';
import { LikesUsersComponent } from './components/template/likes-users/likes-users.component';
import { NotificationComponent } from './views/notification/notification.component';
import { NotificationInfoComponent } from './components/template/notification-info/notification-info.component';
import { ChatComponentComponent } from './views/chat-component/chat-component.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    BooksComponent,
    BookinfoComponent,
    LoginComponent,
    AddBookComponent,
    BorrowedComponent,
    LendBooksComponent,
    SignUpComponent,
    CommentinfoComponent,
    UserConfigComponent,
    EditUserComponent,
    UserProfileComponent,
    ProfileComponent,
    UserinfoComponent,
    LikesUsersComponent,
    NotificationComponent,
    NotificationInfoComponent,
    ChatComponentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatCardModule,
    HttpClientModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatCheckboxModule,
    MatSelectModule,
    MatButtonModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [
    provideClientHydration()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
