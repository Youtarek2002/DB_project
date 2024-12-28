import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Environment } from '../../environment/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  constructor(private http:HttpClient) { }


  reserve(body:any):Observable<any>
  {
    return this.http.post(Environment.url+'/authenticate/reservations/create',body)
  }
  getuserreservations(params:any):Observable<any>
  {
    // const params = {
    //   userId:localStorage.getItem("id")
    // }
    return this.http.get(Environment.url + '/authenticate/reservations/user-reservations',{params})
  }
}
