import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { apiUrl } from '../../app.config';
import { ApiPaths } from '../../api.paths';

@Injectable({
  providedIn: 'root'
})
export class RedirectService {

  constructor(
    private http: HttpClient
  ) { }


  bidRedirect(campaignId: string): Observable<any> {
    return this.http.post<any>(`${apiUrl + ApiPaths.BidPaths.BID_PRODUCT + campaignId}`, {},  {
      responseType: 'text' as 'json'
    });
  }

}
