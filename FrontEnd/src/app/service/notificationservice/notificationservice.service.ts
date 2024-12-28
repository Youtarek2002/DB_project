import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Environment } from '../../environment/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationserviceService {

  constructor(private http:HttpClient) { }



  getusernotification(params:any):Observable<any>
  {
    return this.http.get(Environment.url+'/authenticate/notification/userNotification',{params})
  }
}
