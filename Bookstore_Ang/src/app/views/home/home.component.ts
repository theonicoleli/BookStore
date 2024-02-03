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
    console.log("Oi - ", this.session.getAuthenticatedUser());
    this.name = this.session.getAuthenticatedUser().name;
  }

}
