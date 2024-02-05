import { Component } from '@angular/core';
import { AuthenticationService } from '../../../services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  constructor(
    private session: AuthenticationService,
    private router: Router
    ) {}

  sessionIs(): boolean {
    return this.session.getAuthenticatedUser() == undefined;
  }

  outSession() {
    if (confirm("Realmente deseja sair da sua sessão atual?")) {
      this.session.logout();
      this.router.navigate(["/"]);
    }
  }

}
