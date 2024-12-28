import { Component,Input} from '@angular/core';
import { LucideAngularModule, Car,MapPin,Zap,ArrowRight,Accessibility} from 'lucide-angular';
import { CommonModule } from '@angular/common';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { PaginatorModule } from 'primeng/paginator';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { LotsserviceService } from '../../../service/lotsservice/lotsservice.service';
import { ReportserviceService } from '../../../service/reportservice/reportservice.service';

export interface ParkingLot {
  id: number;
  location: string;
  latitude:number;
  longitude:number;
  regular_count:number;
  disabled_count:number;
  EV_count:number;
  regular_price:number;
  disabled_price:number;
  EV_price:number;
  
}
@Component({
  selector: 'app-reports',
  imports: [RouterOutlet,LucideAngularModule,CommonModule,IconFieldModule,InputIconModule,PaginatorModule],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.scss'
})
export class ReportsComponent {
  constructor(private router:Router,private route: ActivatedRoute, private lotsservice:LotsserviceService,private reportservice:ReportserviceService){}
  readonly mapPinIcon = MapPin;
  readonly carIcon = Car;
  readonly zapIcon = Zap;
  readonly arrowIcon = ArrowRight;
  readonly wheelChairIcon = Accessibility;

  lots!:ParkingLot[]
  toview!:ParkingLot[]
  first: number = 0;
  rows: number = 5;
  managerid!:number
  adminid!:number

  ngOnInit()
  {
    this.managerid = history.state.managerid; 
    this.adminid = history.state.adminid
    if(this.managerid)
    {
      this.fetchmanagerlots()
      
    }

    else{
      this.fetchlots()
}

}

  onPageChange(event: any) {
    this.first = event.first;
    this.rows = event.rows;
    this.toview = this.lots.slice(this.first,Math.min(this.first+this.rows,this.lots.length))
  }
  fetchmanagerlots()
      {
        const params = {
          managerId:this.managerid
        }
        this.lotsservice.getmanagerlots(params).subscribe(
          response=>{
              this.lots=response;
              setTimeout(() => {
                this.toview = this.lots.slice(0, this.rows);
            }, 200); 
          },
          error=>
          {

          }
        )
      }
      fetchlots()
      {
        this.lotsservice.getAllLots().subscribe(
          response=>{
              this.lots=response;
              setTimeout(() => {
                this.toview = this.lots.slice(0, this.rows);
            }, 200); 
          },
          error=>
          {
  
          }
        )
      }

  generatereport(lot:any)
  {
    const params = {
      managerId:this.managerid
    }

    this.reportservice.generatemanagerreport(params).subscribe(
      (response:Blob)=>{
        // const blob = new Blob([response], { type: 'application/pdf' });
        // const link = document.createElement('a');
        // link.href = URL.createObjectURL(blob);
        // link.download = 'reportManager.pdf'; // This is the default filename from the backend
        // link.click();  
        const blob = new Blob([response], { type: 'application/pdf' });
        const blobUrl = URL.createObjectURL(blob);
        window.open(blobUrl, '_blank'); // This will open the PDF in a new tab
        setTimeout(() => {
          URL.revokeObjectURL(blobUrl);
        }, 1000); // Revoking the URL after 1 second   
          
          }

    )
  }
  generateadminreport()
  {
    

    this.reportservice.generateadminreport().subscribe(
      (response:Blob)=>{
        // const blob = new Blob([response], { type: 'application/pdf' });
        // const link = document.createElement('a');
        // link.href = URL.createObjectURL(blob);
        // link.download = 'reportManager.pdf'; // This is the default filename from the backend
        // link.click();  
        const blob = new Blob([response], { type: 'application/pdf' });
        const blobUrl = URL.createObjectURL(blob);
        window.open(blobUrl, '_blank'); // This will open the PDF in a new tab
        setTimeout(() => {
          URL.revokeObjectURL(blobUrl);
        }, 1000);   
          
          }

    )
  }
}
