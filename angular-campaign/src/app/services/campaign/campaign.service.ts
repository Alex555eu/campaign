import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, shareReplay } from 'rxjs';
import { apiUrl } from '../../app.config';
import { Campaign } from '../../model/campaign.model';
import { ApiPaths } from '../../api.paths';
import { PostCampaignRequest } from '../../dto/post-campaign-request.model';
import { PutCampaignRequest } from '../../dto/put-campaign-request.model';

@Injectable({
  providedIn: 'root'
})
export class CampaignService {

  constructor(
    private http: HttpClient
  ) { }

  getAllUserCampaigns(): Observable<Campaign[]> {
    return this.http.get<Campaign[]>(`${apiUrl + ApiPaths.CampaignPaths.GET_ALL_USER_CAMPAIGNS}`).pipe(
      shareReplay(1)
    );
  }

  postCampaign(body: PostCampaignRequest): Observable<Campaign> {
    return this.http.post<Campaign>(`${apiUrl + ApiPaths.CampaignPaths.POST_USER_CAMPAIGN}`, body)
  }

  putCampaign(body: PutCampaignRequest): Observable<Campaign> {
    return this.http.put<Campaign>(`${apiUrl + ApiPaths.CampaignPaths.PUT_USER_CAMPAIGN}`, body);
  }

  deleteCampaign(campaignId: string): Observable<any> {
    return this.http.delete<any>(`${apiUrl + ApiPaths.CampaignPaths.DELETE_USER_CAMPAIGN + campaignId}`);
  }

}
