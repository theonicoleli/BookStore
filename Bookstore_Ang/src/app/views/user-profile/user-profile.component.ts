import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UsersService } from '../../services/users.service';
import { User } from '../../services/models/User';
import { Book } from '../../services/models/Book';
import { BooksService } from '../../services/books.service';
import { ReadbookService } from '../../services/readbook.service';
import { SavebookService } from '../../services/savebook.service';
import { AuthenticationService } from '../../services/authentication.service';
import { FriendshipService } from '../../services/friendship.service';

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

  friends: User[] = [];

  showFriendsModal: boolean = false;

  constructor(
    private route: ActivatedRoute, 
    private userService: UsersService, 
    private bookService: BooksService,
    private readBookService: ReadbookService,
    private savedBookService: SavebookService,
    protected session: AuthenticationService,
    private friendShipService: FriendshipService
    ) 
    {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['username']) {
        this.userName = params['username'];
        this.friends = [];
        this.loadUserProfile();
      }
    });

    this.savedBookService.saveBookChange.subscribe(() => {
      this.getAllSavedBooksByUserId();
    });
  }

  loadUserProfile(): void {
    this.userService.getUserByUserName(this.userName).subscribe(
      (data: any) => {
        this.userProfile = data;

        this.getUserFriends();
        this.getAllSavedBooksByUserId();
        
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

  getBooksAlreadyRead(): void {
    if (this.userProfile?.id !== undefined) {
      this.readBooks = [];
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

  getAllSavedBooksByUserId(): void {
    if (this.userProfile?.id !== undefined) {
      this.savedBookService.getAllSavedBookByUserId(this.userProfile?.id).subscribe(
        (data: any) => {
          const savedBookBefore = this.savedBooks;
          this.savedBooks = [];
          for(const book of data) {
            this.bookService.getBookById(book.bookId).subscribe(
              (readBook: Book) => {
                if (!this.savedBooks.some(savedBook => savedBook.id === readBook.id)) {
                  this.savedBooks.push(readBook);
                }
              },
              (error) => {
                console.log("Erro ao tentar achar livros salvos do usuário.");
              }
            );
          }
          if (savedBookBefore !== this.savedBooks) {
            this.getBooksAlreadyRead();
          }
        },
        (error: any) =>  {
          console.log("Erro ao achar livros salvos.")
        }
      );
    }
  }

  isUserSessionProfile(): boolean {
    if (this.session.getAuthenticatedUser()?.id === this.userProfile?.id) {
      return true;
    }
    return false;
  }

  sendFriendShipRequest(): void {
    if (confirm(`Tem certeza de que deseja adicionar ${this.userProfile?.name}?`)) {
      this.friendShipService.sendFriendShipRequest(this.session.getAuthenticatedUser(), this.userProfile).subscribe(
        (data) => {
          console.log("Foi enviado uma notificação ao usuário");
        },
        (error) => {
          console.error("Falha ao enviar notificação");
        }
      );
    }
  }

  getUserFriends() {
    if (this.userProfile?.id !== undefined) {
      this.friendShipService.getFriendShipsByUser(this.userProfile?.id).subscribe(
        (friendsId) => {
          if (friendsId) {
            for (const friendId of friendsId) {
              this.userService.getUserById(friendId).subscribe(
                (user) => {
                  if (!this.friends.some(friend => friend.id === user.id)) {
                    this.friends.push(user);
                  }
                },
                (error) => {
                  console.log("Falha ao encontrar userFriends");
                }
              );
            }
          }
        },
        (error) => {
          console.log("Falha ao encontrar userFriends");
        }
      )
    }
  }

  existsFriendShip(): boolean {
    return !this.friends.some(friend => friend?.id === this.session.getAuthenticatedUser()?.id);
  }

  openFriendsModal() {
    this.showFriendsModal = true;
  }

  closeFriendsModal() {
    this.showFriendsModal = false;
  }

  deleteFriendShip() {
    if (this.userProfile !== undefined && this.session.isAuthenticated()) {
      if (!this.existsFriendShip() && confirm(`Você realmente deseja excluir a amizade com ${this.userProfile?.name}`)) {
        this.friendShipService.deleteFriendShip(this.session.getAuthenticatedUser()?.id, this.userProfile?.id).subscribe(
          (data) => {
            console.log("Amizade excluida com sucesso.");
            this.friends = this.friends.filter(friend => friend?.id !== this.userProfile?.id);
          }
        );
      }
    }
  }
  
}

