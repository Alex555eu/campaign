import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Town } from '../../model/town.model';
import { Observable, shareReplay } from 'rxjs';
import { apiUrl } from '../../app.config';
import { ApiPaths } from '../../api.paths';

@Injectable({
  providedIn: 'root'
})
export class TownService {

  constructor(
    private http: HttpClient
  ) { }

  getAllTowns(): Observable<Town[]> {
    return this.http.get<Town[]>(`${apiUrl + ApiPaths.TownPaths.GET_ALL_TOWNS}`).pipe(
      shareReplay(1)
    );
  }
}
