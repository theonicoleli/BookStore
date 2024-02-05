import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { UsersService } from '../../services/users.service';
import { User } from '../../services/models/User';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-config',
  templateUrl: './user-config.component.html',
  styleUrl: './user-config.component.css'
})
export class UserConfigComponent {

  constructor(
    private session: AuthenticationService,
    private userService: UsersService,
    private router: Router
    ) 
    {}

  ngOnInit() {
  }

  returnSessionUser() {
    return this.session.getAuthenticatedUser()?.name;
  }

  editUser(): void {
    if (confirm("Realmente deseja editar sua conta?")) {
      this.router.navigate(["/userconfig/edit"])
    }
  }

  deleteUser(): void {
    if (confirm("Realmente deseja excluir sua conta?")) {
      this.userService.deleteUser(this.session.getAuthenticatedUser()?.id).subscribe(
        (data) => {
          console.log("Usuário deletado com sucesso.");
          this.session.logout();
          this.router.navigate(["/"]);
        },
        (error) => {
          console.error("Falha ao excluir sua conta, erro: ", error);
        }
      );
    }
  }

}
