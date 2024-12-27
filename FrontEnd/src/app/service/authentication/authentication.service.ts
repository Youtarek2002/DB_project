import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Environment } from '../../environment/environment';
@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http : HttpClient) { }

  login(info:any):Observable<any>
  {
    return this.http.post(Environment.url+'/login',info)
  }
  signup(info:any):Observable<any>
  {
    delete info.confirmpassword
    return this.http.post(Environment.url+'/signup',info)
  }
}
