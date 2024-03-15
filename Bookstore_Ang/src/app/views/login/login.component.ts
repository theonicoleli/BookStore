import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsersService } from '../../services/users.service';
import { AuthenticationService } from '../../services/authentication.service';
import { User } from '../../services/models/User';
import { Router } from '@angular/router';

@Component({ 
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  successfulLogin: boolean = false;
  hide: boolean = true;
  errorMessage: string = '';
  pageLogin: boolean = true;

  constructor(private fb: FormBuilder, private usersService: UsersService, private session: AuthenticationService,
    private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {
  }

  get email() {
    return this.loginForm.get('email') || this.fb.control('');
  }

  get password() {
    return this.loginForm.get("password") || this.fb.control('');
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const userEmail = this.loginForm.controls['email'].value;
      const userPassword = this.loginForm.controls['password'].value;
  
      this.usersService.getUserByEmailAndPassword(userEmail, userPassword).subscribe(
        (data: User) => {
          console.log(data);
          this.session.setAuthenticatedUser(data);
          this.errorMessage = '';
          this.router.navigate(['/']);
        },
        (error) => {
          console.error("Error fetching user:", error);
          this.errorMessage = "Seu email ou sua senha estão incorretas";
        }
      );
    } else {
      this.loginForm.controls['email'].markAsTouched();
      this.loginForm.controls['password'].markAsTouched();
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