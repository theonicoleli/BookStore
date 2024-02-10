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
    protected session: AuthenticationService,
    private router: Router
    ) {}

  getSessionProfile() {
    return this.session.getAuthenticatedUser()?.userName;
  }

  sessionIs(): boolean {
    return this.session.getAuthenticatedUser() == undefined;
  }

  routerProfile() {
    const url = "/userprofile/" + this.getSessionProfile();
    console.log(url)
    this.router.navigate([url]);
  }

  outSession() {
    if (confirm("Realmente deseja sair da sua sessão atual?")) {
      this.session.logout();
      this.router.navigate(["/"]);
    }
  }

}
