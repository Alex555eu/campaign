import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/authorization/auth.service';
import { TokenService } from '../../services/token/token.service';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { AuthResponse } from '../../dto/auth-response';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder, 
    private authService: AuthService,
    private tokenService: TokenService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]], 
      password: ['', [Validators.required, Validators.minLength(4)]] 
    });
  };

  onLogin() {
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value;
      this.authService.login(email, password).subscribe({
        next: (data: AuthResponse) => {
          this.tokenService.set(data.token, data.refreshToken);
          this.router.navigate(['']);
        },
        error: (error: any) => {
          this.snackBar.open('Incorrect login data', 'Close', {
            duration: 5000, // millis
            horizontalPosition: 'center',
            verticalPosition: 'top'
          });
        }
      })

    }
  };

  onRegister() {
    this.router.navigate(['/register']);
  };
}
