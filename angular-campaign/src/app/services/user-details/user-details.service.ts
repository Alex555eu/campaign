import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, shareReplay } from 'rxjs';
import { UserDetails } from '../../model/user-details.model';
import { apiUrl } from '../../app.config';
import { ApiPaths } from '../../api.paths';
import { EmeraldWallet } from '../../model/emerald-wallet.model';

@Injectable({
  providedIn: 'root'
})
export class UserDetailsService {

  constructor(
    private http: HttpClient
  ) { }

  getUserDetails(): Observable<UserDetails> {
    return this.http.get<UserDetails>(`${apiUrl + ApiPaths.UserPaths.GET_USER}`).pipe(
      shareReplay(1)
    );
  }

  rechargeUserEmeraldAccount(amount: number): Observable<EmeraldWallet> {
    return this.http.post<EmeraldWallet>(`${apiUrl + ApiPaths.UserPaths.RECHARGE_USER_EMERALD_ACCOUNT + amount}`, {});
  }
  
}
