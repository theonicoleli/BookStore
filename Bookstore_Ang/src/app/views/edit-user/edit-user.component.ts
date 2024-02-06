import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsersService } from '../../services/users.service';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrl: './edit-user.component.css'
})
export class EditUserComponent {

  userForm: FormGroup;
  hide: boolean = true;

  constructor(
    private fb: FormBuilder, 
    private usersService: UsersService, 
    private session: AuthenticationService,
    private router: Router
    ) {
      const authenticatedUser = this.session.getAuthenticatedUser();
      
      this.userForm = this.fb.group({
        userName: [authenticatedUser?.userName, Validators.required],
        name: [authenticatedUser?.name, Validators.required],
        password: [authenticatedUser?.password, Validators.required]
      });
  }

  get password() {
    return this.userForm.get('password');
  }

  get name() {
    return this.userForm.get('name');
  }

  get userName() {
    return this.userForm.get('userName');
  }
  
  ngOnInit(){
  }

  togglePasswordVisibility() {
    const passwordControl = this.userForm.get('password');
    if (passwordControl) {
      passwordControl.patchValue(passwordControl.value);
      this.hide = !this.hide;
    }
  }

  getVisibilityIcon() {
    return this.hide ? 'visibility' : 'visibility_off';
  }

  onSubmit() {
    if(confirm("Realmente deseja alterar seu cadastro?")) {
      const sessionConst = this.session.getAuthenticatedUser();

      this.usersService.putUser(
        this.userForm.controls['name'].value, 
        this.userForm.controls['userName'].value , 
        sessionConst?.email, 
        this.userForm.controls['password'].value, 
        sessionConst?.id
      ).subscribe(
        (data) => {
          console.log(this.userForm.controls['userName'].value);
          console.log("Alterado com sucesso!");
        },
        (error) => {
          console.error("Erro na alteração, erro: ", error);
        }
      )
    }
  }

}
