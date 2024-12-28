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
    return this.http.post(Environment.url+'/authenticate/signin',info)
  }
  signup(info:any):Observable<any>
  {
    delete info.confirmpassword
    console.log(info)
    return this.http.post(Environment.url+'/authenticate/signup',info)
  }
  managersignup(info:any):Observable<any>
  {
    delete info.confirmpassword
    console.log(info)
    return this.http.post(Environment.url+'/authenticate/managerSignup',info)
  }
}
