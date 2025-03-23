import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TokenService } from '../../services/token/token.service';
import { AuthService } from '../../services/authorization/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthResponse } from '../../dto/auth-response';
import { RegisterRequest } from '../../dto/register-request';
import { MatError, MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatError,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  registerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private tokenService: TokenService,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {
    this.registerForm = this.fb.group({
      firstName: [''],
      emailAddress: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      repeatPassword: ['', Validators.required],  
    }, { validators: this.matchValidator('password', 'repeatPassword') });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      this.authService.register(this.makeRegisterRequestBody(this.registerForm)).subscribe({
        next: (data: AuthResponse) => {
          this.tokenService.set(data.token, data.refreshToken);
          this.router.navigate(['/']);
        },
        error: (error: any) => {
          this.snackBar.open('Incorrect register data.', 'Close', {
            duration: 5000, // millis
            horizontalPosition: 'center',
            verticalPosition: 'top'
          });
        }
      })

    }
  }


  private matchValidator(controlName: string, matchingControlName: string): ValidatorFn {
    return (abstractControl: AbstractControl) => {
        const control = abstractControl.get(controlName);
        const matchingControl = abstractControl.get(matchingControlName);
        if (matchingControl!.errors && !matchingControl!.errors?.['confirmedValidator']) {
            return null;
        }
        if (control!.value !== matchingControl!.value) {
          const error = { confirmedValidator: 'Passwords do not match.' };
          matchingControl!.setErrors(error);
          return error;
        } else {
          matchingControl!.setErrors(null);
          return null;
        }
    }
  }

  private makeRegisterRequestBody(form: FormGroup): RegisterRequest {
    let registerRequest: RegisterRequest = {
        emailAddress: form.get('emailAddress')?.value,
        firstName: form.get('firstName')?.value,
        password: form.get('password')?.value,
      };
    return registerRequest;
  }

}
