import { Component, Input } from '@angular/core';
import { User } from '../../../services/models/User';
import { Router } from '@angular/router';

@Component({
  selector: 'app-userinfo',
  templateUrl: './userinfo.component.html',
  styleUrl: './userinfo.component.css'
})
export class UserinfoComponent {
  @Input() user!: User;

  constructor(private router: Router) {}

  ngOnInit() {
  }

  openUserProfile() {
    this.router.navigate(['userprofile/' + this.user.userName]);
  }

}
