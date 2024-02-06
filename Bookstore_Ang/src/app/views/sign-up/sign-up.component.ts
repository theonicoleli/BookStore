import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsersService } from '../../services/users.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
  loginForm: FormGroup;
  hide: boolean = true;
  errorMessage: string = '';

  constructor(private fb: FormBuilder, private usersService: UsersService, private router: Router) {
    this.loginForm = this.fb.group({
      name: ['', Validators.required],
      userName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  get name() {
    return this.loginForm.get('name');
  }

  get userName() {
    return this.loginForm.get("userName");
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const name = this.loginForm.controls['name'].value;
      const userEmail = this.loginForm.controls['email'].value;
      const userPassword = this.loginForm.controls['password'].value;
      const userName = this.loginForm.controls['userName'].value;

      this.usersService.countUserByEmail(userEmail).subscribe(
        (data: number) => {

          if (data > 0) {
            alert('Email já cadastrado, faça seu login!');
            return;
          }

          this.usersService.postUser(name, userName, userEmail, userPassword).subscribe(
            (response) => {
              alert('Cadastro realizado com sucesso!');
              this.router.navigate(['/login']);
            },
            (error) => {
              console.error('Error posting user:', error);
            }
          );
        },
        (error: any) => {
          console.log('Error checking email:', error);
        }
      );
    }
  }

  togglePasswordVisibility() {
    const passwordControl = this.loginForm.get('password');
    if (passwordControl) {
      passwordControl.patchValue(passwordControl.value);
      this.hide = !this.hide;
    }
  }

  getVisibilityIcon() {
    return this.hide ? 'visibility' : 'visibility_off';
  }
}
