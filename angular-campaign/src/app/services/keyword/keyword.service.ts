import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, shareReplay } from 'rxjs';
import { Keyword } from '../../model/keyword.model';
import { apiUrl } from '../../app.config';
import { ApiPaths } from '../../api.paths';

@Injectable({
  providedIn: 'root'
})
export class KeywordService {

  constructor(
    private http: HttpClient
  ) { }

  getAllKeywordsMatchingQuery(query: string): Observable<Keyword[]> {
    return this.http.get<Keyword[]>(`${apiUrl + ApiPaths.KeywordPaths.GET_MATCHING_KEYWORDS + query}`).pipe(
      shareReplay(1)
    );
  }

}
