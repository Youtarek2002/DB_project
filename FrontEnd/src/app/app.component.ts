import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ManagerNavbarComponent } from './components/manager/manager-navbar/manager-navbar.component';
import { LotsComponent } from './components/general/lots/lots.component';
import { GoogleMapsModule } from '@angular/google-maps';
import { WebsocketService } from './service/websocket/websocket.service';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet,ManagerNavbarComponent,LotsComponent,GoogleMapsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'Frontend';
  wsUrl = 'ws://localhost:8080/ws'
  constructor(private websocketService: WebsocketService) {}
  ngOnInit(): void {
    this.websocketService.connect(this.wsUrl);

    this.websocketService.getMessages().subscribe((message) => {
      // Trigger an alert for each received message
      alert(`New message received: ${message}`);
    });
  }

  ngOnDestroy(): void {
    this.websocketService.disconnect();
  }
}
