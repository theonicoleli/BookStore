import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  name: string = '';

  constructor(private session: AuthenticationService) {}

  ngOnInit() {
    const sessionStart = this.session.getAuthenticatedUser();

    if (sessionStart) {
      this.name = sessionStart.name;
    }
  }

}
