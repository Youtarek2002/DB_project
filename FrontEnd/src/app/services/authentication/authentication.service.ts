import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http : HttpClient) { }

  login(info:any):Observable<any>
  {
    return this.http.post(Environment.url+'/authenticate/sigin',info)
  }
}
