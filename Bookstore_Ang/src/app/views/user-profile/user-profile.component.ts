import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UsersService } from '../../services/users.service';
import { User } from '../../services/models/User';
import { Book } from '../../services/models/Book';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  userName: string = '';
  userProfile: User | null = null;
  books: Book[] = []; // Added a property to store the books

  constructor(private route: ActivatedRoute, private userService: UsersService) {}

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
        
        if (data.bookList != null) {
          this.books = data.bookList;
        }
      },
      (error: any) => {
        console.log("Error trying to find person with userName: " + this.userName);
      }
    )
  }
}
