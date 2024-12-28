import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private websocket!: WebSocket;
  private messageSubject: Subject<string> = new Subject();

  connect(url: string): void {
    this.websocket = new WebSocket(url);

    this.websocket.onopen = () => {
      console.log('WebSocket connection established.');
    };

    this.websocket.onmessage = (event) => {
      // No JSON parsing, just emit the string message directly
      this.messageSubject.next(event.data);
    };

    this.websocket.onerror = (event) => {
      console.error('WebSocket error:', event);
    };

    this.websocket.onclose = () => {
      console.log('WebSocket connection closed.');
    };
  }

  getMessages(): Observable<string> {
    return this.messageSubject.asObservable();
  }

  disconnect(): void {
    if (this.websocket) {
      this.websocket.close();
    }
  }
}
