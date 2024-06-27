import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsersService } from '../../services/users.service';
import { Router } from '@angular/router';
import * as CryptoJS from 'crypto-js';


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
      password: ['', Validators.required],
      image: [null]
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
      const hashedPassword = CryptoJS.MD5(this.loginForm.controls['password'].value).toString();

      const formData = new FormData();
      formData.append('name', this.loginForm.controls['name'].value);
      formData.append('userName', this.loginForm.controls['userName'].value);
      formData.append('email', this.loginForm.controls['email'].value);
      formData.append('password', hashedPassword);
      const fileInput = (document.getElementById('image') as HTMLInputElement);
      if (fileInput.files?.length) {
        formData.append('image', fileInput.files[0]);
      }

      this.usersService.postUser(formData).subscribe(
        (response) => {
          alert('Cadastro realizado com sucesso!');
          this.router.navigate(['/login']);
        },
        (error) => {
          console.error('Erro ao cadastrar usuário:', error);
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

  onFileChange(event: any) {
    const file = (event.target as HTMLInputElement).files?.[0];
    this.loginForm.patchValue({
      image: file
    });
  }  

}
