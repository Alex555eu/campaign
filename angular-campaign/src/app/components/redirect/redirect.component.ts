import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { RedirectService } from '../../services/redirect/redirect.service';

@Component({
  selector: 'app-redirect',
  standalone: true,
  imports: [],
  templateUrl: './redirect.component.html',
  styleUrl: './redirect.component.css'
})
export class RedirectComponent implements OnInit{

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private redirectService: RedirectService
  ){}

  ngOnInit(): void {
      this.route.queryParamMap.pipe(
        switchMap(params => {
          const query = params.get('key');
          if (query) {
            return this.redirectService.bidRedirect(query);
          }
          return [];
        })
      ).subscribe(url => {
        if(url){
          window.location.replace(url);
        }
      });
  }


}
