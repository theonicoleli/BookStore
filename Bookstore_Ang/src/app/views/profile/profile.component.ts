import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from '../../services/models/User';
import { UsersService } from '../../services/users.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  searchProfile: FormGroup;
  allUsers: User[] = [];
  searchUsers: User[] = [];

  constructor(private formBuilder: FormBuilder, private userService: UsersService, private router: Router) {
    this.searchProfile = this.formBuilder.group({
      userName: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.userService.getAllUsers().subscribe(
      (data: User[]) => {
        this.allUsers = data;
      },
      (error) => {
        console.error("Erro ao encontrar usuários.")
      }
    )
  }

  onSubmit() {
    const searchTerm = this.searchProfile.controls['userName'].value.toLowerCase();
    this.searchUsers = this.allUsers.filter(user => 
      user.userName && user.userName.toLowerCase().startsWith(searchTerm)
    );
  }
  
}
