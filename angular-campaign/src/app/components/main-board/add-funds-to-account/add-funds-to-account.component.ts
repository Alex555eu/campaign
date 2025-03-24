import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { UserDetails } from '../../../model/user-details.model';
import { UserDetailsService } from '../../../services/user-details/user-details.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-add-funds-to-account',
  standalone: true,
  imports: [
    MatDialogModule,
    CommonModule,
    MatIconModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    FormsModule,
  ],
  templateUrl: './add-funds-to-account.component.html',
  styleUrl: './add-funds-to-account.component.css'
})
export class AddFundsToAccountComponent {

  availableFunds = [100, 200, 500];
  selectedFund: number = this.availableFunds[0];

  constructor(
    private snackBar: MatSnackBar,
    private userDetailsService: UserDetailsService,
    private dialogRefParent: MatDialogRef<AddFundsToAccountComponent>
  ) {}

  onSubmit() {
    this.userDetailsService.rechargeUserEmeraldAccount(this.selectedFund).subscribe({
      next: () => {
        this.dialogRefParent.close(true);
      },
      error: (error: any) => {
        this.snackBar.open('Http error status: ' + error.status, 'Close', {
          duration: 5000, // millis
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }
    });
  }

}
