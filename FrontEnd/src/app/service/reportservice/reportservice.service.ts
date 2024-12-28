import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Environment } from '../../environment/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportserviceService {

  constructor(private http:HttpClient) { }


  generatemanagerreport(params: any): Observable<Blob> {
    return this.http.get<Blob>(`${Environment.url}/authenticate/report/generate-manager-report`, {
      params: params,
      responseType: 'blob' as 'json'  
    });
  }
  generateadminreport(): Observable<Blob> {
    return this.http.get<Blob>(`${Environment.url}/authenticate/report/generate-admin-report`, {
      responseType: 'blob' as 'json'  
    });
  }
  
}
