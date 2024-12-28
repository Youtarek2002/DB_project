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
  getmanagerlots(params:any):Observable<any>
  {
    return this.http.get(Environment.url+'/authenticate/manager/lots',{params})
  }
  getpendinglots():Observable<any>
  {
    return this.http.get(Environment.url+'/authenticate/all/pending/lots')

  }
  getnearest(params:any):Observable<any>
  {
    return this.http.get(Environment.url+"/authenticate/nearest/lots",{params})
  }
  deletelot(params:any)
  {
    return this.http.delete(Environment.url+'/authenticate/delete/lot',{params})
  }

  addlot(event:any):Observable<any>
  {
    return this.http.post(Environment.url+'/authenticate/request/adding/lot',event)
  }
  admitlot(params:any):Observable<any>
  {
    return this.http.get(Environment.url+'/authenticate/accept/adding/lot',{params})
  }
  denylot(params:any):Observable<any>
  {
    return this.http.get(Environment.url+'/authenticate/deny/adding/lot/request',{params})
  }

}
