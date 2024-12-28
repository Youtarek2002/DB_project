import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Environment } from '../../environment/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LotsserviceService {

  constructor(private http : HttpClient) { }



  getAllLots():Observable<any>
  {
    return this.http.get(Environment.url+'/authenticate/getAllLots')
  }
}
