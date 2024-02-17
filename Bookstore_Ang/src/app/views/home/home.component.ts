import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { User } from '../../services/models/User';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  userSession: User = {} as User;

  constructor(private session: AuthenticationService) {}

  ngOnInit() {
    const sessionStart = this.session.getAuthenticatedUser();

    if (sessionStart) {
      this.userSession = sessionStart;
    }
  }

}
