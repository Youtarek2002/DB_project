import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ManagerNavbarComponent } from './components/manager/manager-navbar/manager-navbar.component';
import { LotsComponent } from './components/general/lots/lots.component';
import { GoogleMapsModule } from '@angular/google-maps';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,ManagerNavbarComponent,LotsComponent,GoogleMapsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'Frontend';
}
