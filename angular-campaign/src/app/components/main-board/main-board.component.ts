import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CampaignService } from '../../services/campaign/campaign.service';
import { UserDetailsService } from '../../services/user-details/user-details.service';
import { UserDetails } from '../../model/user-details.model';
import { Campaign } from '../../model/campaign.model';
import {MatCardModule} from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { TokenService } from '../../services/token/token.service';
import { Router } from '@angular/router';
import { catchError, of, tap, throwError } from 'rxjs';
import { AuthService } from '../../services/authorization/auth.service';
import { MatDialog, MatDialogConfig, MatDialogModule } from '@angular/material/dialog';
import { AddFundsToAccountComponent } from './add-funds-to-account/add-funds-to-account.component';
import { NewCampaignComponent } from './new-campaign/new-campaign.component';
import { EditCampaignComponent } from './edit-campaign/edit-campaign.component';

@Component({
  selector: 'app-main-board',
  standalone: true,
  imports: [
    MatCardModule,
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatDialogModule
  ],
  templateUrl: './main-board.component.html',
  styleUrl: './main-board.component.css'
})
export class MainBoardComponent implements OnInit {

  userDetails: UserDetails | null = null;
  campaigns: Campaign[] = [];
  origin: string = ""

  constructor(
    private snackBar: MatSnackBar,
    private campaignService: CampaignService,
    private userDetailsService: UserDetailsService,
    private tokenService: TokenService,
    private router: Router,
    private authService: AuthService,
    private dialog: MatDialog
  ) {}


  ngOnInit(): void {
    // get user data
    this.userDetailsService.getUserDetails().subscribe({
      next: (data: UserDetails) => {
        this.userDetails = data;
      },
      error: (error: any) => {
        this.snackBar.open(error.message, 'Close', {
          duration: 5000, // millis
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }
    })
    // get campaigns
    this.campaignService.getAllUserCampaigns().subscribe({
      next: (data: Campaign[]) => {
        this.campaigns = data;
      },
      error: (error: any) => {
        this.snackBar.open(error.message, 'Close', {
          duration: 5000, // millis
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }
    })
    this.origin = window.location.href;
  }

  addToWallet() {
    const ref = this.dialog.open(AddFundsToAccountComponent);
    ref.afterClosed().subscribe(res => {
      if(res) {
        this.ngOnInit();
      }
    })
  }

  newCampaign() {
    const ref = this.dialog.open(NewCampaignComponent);
    ref.afterClosed().subscribe(res => {
      if(res) {
        this.ngOnInit();
      }
    })
  }

  editCampaign(campaign: Campaign) {
    let config = new MatDialogConfig();
    config.data = {
      campaign: campaign
    }
    const ref = this.dialog.open(EditCampaignComponent, config);
    ref.afterClosed().subscribe(res => {
      if(res) {
        this.ngOnInit();
      }
    })
  }

  deleteCampaign(campaign: Campaign) {
    this.campaignService.deleteCampaign(campaign.id).subscribe({
      next: () => {
        this.ngOnInit();
      },
      error: (error: any) => {
        this.snackBar.open(error.message, 'Close', {
          duration: 5000, // millis
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }
    })
  }

  logout() {
    const authToken = this.tokenService.getAuthToken();
    if (!authToken) {
      this.router.navigate(['login']);
      window.location.reload();
      return of();
    }
    return this.authService.logout(authToken).pipe(
      tap(() => {
        this.tokenService.clearAll();
        this.router.navigate(['login']);
        window.location.reload();
      }),
      catchError(err => {
        this.tokenService.clearAll();
        this.router.navigate(['login']);
        window.location.reload();
        return throwError(() => err);
      })
    ).subscribe();
  }


  
}
