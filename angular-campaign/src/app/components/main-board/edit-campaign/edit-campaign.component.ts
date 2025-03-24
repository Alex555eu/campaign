import { Component, Inject, OnInit } from '@angular/core';
import { PutCampaignRequest } from '../../../dto/put-campaign-request.model';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Observable, switchMap } from 'rxjs';
import { Keyword } from '../../../model/keyword.model';
import { Town } from '../../../model/town.model';
import { CampaignService } from '../../../services/campaign/campaign.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TownService } from '../../../services/town/town.service';
import { KeywordService } from '../../../services/keyword/keyword.service';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { NewCampaignComponent } from '../new-campaign/new-campaign.component';
import { Campaign } from '../../../model/campaign.model';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatChipsModule } from '@angular/material/chips';

@Component({
  selector: 'app-edit-campaign',
  standalone: true,
  imports: [
    MatDialogModule,
    MatCardModule,
    CommonModule,
    MatIconModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    FormsModule,
    MatCheckboxModule,
    MatAutocompleteModule,
    MatChipsModule
  ],
  templateUrl: './edit-campaign.component.html',
  styleUrl: './edit-campaign.component.css'
})
export class EditCampaignComponent implements OnInit {

  campaign: PutCampaignRequest = {
    campaignId: '',
    campaignName: '',
    keywordIds: [],
    bidAmount: 1,
    campaignFund: 0,
    productUrl: '',
    status: false,
    townId: '',
    radius: 0,
  };

  keywordCtrl = new FormControl();
  filteredKeywords: Observable<Keyword[]> | null = null;

  availableTowns: Town[] = [];

  selectedKeywords: Keyword[] = [];

  constructor(
    private campaignService: CampaignService,
    private snackBar: MatSnackBar,
    private townService: TownService,
    private keywordService: KeywordService,
    private dialogRefParent: MatDialogRef<NewCampaignComponent>,
    @Inject(MAT_DIALOG_DATA) private data: {
      campaign: Campaign
    },
  ) {}

  ngOnInit(): void {
    this.campaign.campaignId = this.data.campaign.id;
    this.campaign.campaignName = this.data.campaign.campaignName;
    this.campaign.bidAmount = this.data.campaign.bidAmount;
    this.campaign.campaignFund = this.data.campaign.campaignFund;
    this.campaign.productUrl = this.data.campaign.productUrl;
    this.campaign.status = this.data.campaign.status;
    this.campaign.townId = this.data.campaign.town.id;
    this.campaign.radius = this.data.campaign.radius;

    this.data.campaign.keywords.forEach(keyword => {
      this.selectedKeywords.push(keyword);
    })

    // fetch town data
    this.townService.getAllTowns().subscribe(response => {
      if (response) {
        this.availableTowns = response;
      }
    });
    // fetch matching keywords
    this.filteredKeywords = this.keywordCtrl.valueChanges.pipe(
      switchMap(value => this.keywordService.getAllKeywordsMatchingQuery(value))
    );
  }

  addKeyword(keyword: Keyword): void {
    if (!this.selectedKeywords.includes(keyword)) {
      this.selectedKeywords.push(keyword);
    }
    this.keywordCtrl.setValue('');
  }

  removeKeyword(keyword: Keyword): void {
    const index = this.selectedKeywords.indexOf(keyword);
    if (index >= 0) {
      this.selectedKeywords.splice(index, 1); 
    }
  }
  
  onSubmit() {
    this.selectedKeywords.forEach(keyword => {
      this.campaign.keywordIds.push(keyword.id)
    })
    this.campaignService.putCampaign(this.campaign).subscribe({
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

  onClose() {
    this.dialogRefParent.close(null);
  }

}
