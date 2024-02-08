import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsersService } from '../../services/users.service';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
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
      image: [null],
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
  
  togglePasswordVisibility() {
    const passwordControl = this.userForm.get('password');
    if (passwordControl) {
      passwordControl.patchValue(passwordControl.value);
      this.hide = !this.hide;
    }
  }

  onFileChange(event: any) {
    const file = (event.target as HTMLInputElement).files?.[0];
    this.userForm.patchValue({
      image: file
    });
  }

  getVisibilityIcon() {
    return this.hide ? 'visibility' : 'visibility_off';
  }

  onSubmit() {
    if(confirm("Realmente deseja alterar seu cadastro?")) {
      const sessionConst = this.session.getAuthenticatedUser();
  
      const user = {
        name: this.userForm.controls['name'].value,
        userName: this.userForm.controls['userName'].value,
        email: sessionConst?.email ?? '',
        password: this.userForm.controls['password'].value,
      };

      const formData = new FormData();
      
      formData.append('user', JSON.stringify(user));
      formData.append('image', this.userForm.controls['image'].value);

      this.usersService.putUser(sessionConst?.id, formData).subscribe(
        (data) => {
          this.session.setAuthenticatedUser(data);
          console.log("Alterado com sucesso!");
          this.router.navigate(['/userconfig']);
        },
        (error) => {
          console.error("Erro na alteração, erro: ", error);
        }
      );
    }
  }
}
