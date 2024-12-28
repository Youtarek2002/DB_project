import { Component } from '@angular/core';
import { UserNavbarComponent } from '../user-navbar/user-navbar.component';
import { LotsComponent } from '../../general/lots/lots.component';
import { RouterOutlet } from '@angular/router';
@Component({
  selector: 'app-dashboard',
  imports: [UserNavbarComponent,LotsComponent,RouterOutlet],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {

}
