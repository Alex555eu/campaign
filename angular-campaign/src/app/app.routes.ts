import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { MainBoardComponent } from './components/main-board/main-board.component';

export const routes: Routes = [
    { path: '', component: MainBoardComponent},
    { path: 'login', component: LoginComponent},
    { path: 'register', component: RegisterComponent},
    { path: '**', redirectTo: 'home'}

];
