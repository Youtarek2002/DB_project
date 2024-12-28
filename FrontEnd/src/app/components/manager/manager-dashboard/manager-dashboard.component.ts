import { Component } from '@angular/core';
import { ManagerNavbarComponent } from '../manager-navbar/manager-navbar.component';
import { RouterOutlet } from '@angular/router';
@Component({
  selector: 'app-manager-dashboard',
  imports: [ManagerNavbarComponent,RouterOutlet],
  templateUrl: './manager-dashboard.component.html',
  styleUrl: './manager-dashboard.component.scss'
})
export class ManagerDashboardComponent {

}
