import { MatSelectModule } from '@angular/material/select';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CampaignService } from '../../../services/campaign/campaign.service';
import { MatInputModule } from '@angular/material/input';
import { PostCampaignRequest } from '../../../dto/post-campaign-request.model';
import {MatCheckboxModule} from '@angular/material/checkbox';
import { TownService } from '../../../services/town/town.service';
import { Town } from '../../../model/town.model';
import { debounceTime, Observable, switchMap } from 'rxjs';
import { Keyword } from '../../../model/keyword.model';
import { KeywordService } from '../../../services/keyword/keyword.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatChipsModule } from '@angular/material/chips';

@Component({
  selector: 'app-new-campaign',
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
  templateUrl: './new-campaign.component.html',
  styleUrl: './new-campaign.component.css'
})
export class NewCampaignComponent implements OnInit {

  campaign: PostCampaignRequest = {
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
    private dialogRefParent: MatDialogRef<NewCampaignComponent>
  ) {}

  ngOnInit(): void {
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
    this.campaignService.postCampaign(this.campaign).subscribe({
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
