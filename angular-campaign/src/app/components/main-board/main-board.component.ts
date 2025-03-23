import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-main-board',
  standalone: true,
  imports: [],
  templateUrl: './main-board.component.html',
  styleUrl: './main-board.component.css'
})
export class MainBoardComponent {

  constructor(
    private snackBar: MatSnackBar
  ) {}
}
