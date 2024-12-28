import { Component,Input} from '@angular/core';
import { LucideAngularModule, Car,MapPin,Zap,ArrowRight,Accessibility} from 'lucide-angular';
import { CommonModule } from '@angular/common';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { PaginatorModule } from 'primeng/paginator';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
// import {  } from '../../../service/lots/lotsservice.service';
import { LotsserviceService } from '../../../service/lotsservice/lotsservice.service';
export interface PageEvent {
  first: number;
  rows: number;
  page: number;
  pageCount: number;
}

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
  selector: 'app-lots',
  imports: [RouterOutlet,LucideAngularModule,CommonModule,IconFieldModule,InputIconModule,PaginatorModule],
  templateUrl: './lots.component.html',
  styleUrl: './lots.component.scss'
})
export class LotsComponent {
  constructor(private router:Router,private route: ActivatedRoute, private lotsservice:LotsserviceService){}
  @Input() lot!: ParkingLot;
  readonly mapPinIcon = MapPin;
  readonly carIcon = Car;
  readonly zapIcon = Zap;
  readonly arrowIcon = ArrowRight;
  readonly wheelChairIcon = Accessibility;
  // lots = [
  //   {
  //     id:1,
  //     name: 'Downtown Parking',
  //     location: '123 Main St, Downtown',
  //     spots: {
  //       ev: 5,
  //       disabled: 2,
  //       regular: 10,
  //     },
  //   },
  //   {
  //     id:2,

  //     name: 'Downtown Parking',
  //     location: '123 Main St, Downtown',
  //     spots: {
  //       ev: 5,
  //       disabled: 2,
  //       regular: 10,
  //     },
  //   },{
  //     id:3,

  //     name: 'Downtown Parking',
  //     location: '123 Main St, Downtown',
  //     spots: {
  //       ev: 5,
  //       disabled: 2,
  //       regular: 10,
  //     },
  //   },{
  //     id:4,

  //     name: 'Downtown Parking',
  //     location: '123 Main St, Downtown',
  //     spots: {
  //       ev: 5,
  //       disabled: 2,
  //       regular: 10,
  //     },
  //   },
  //   {
  //     id:5,

  //     name: 'Airport Parking',
  //     location: '456 Terminal Rd, Airport',
  //     spots: {
  //       ev: 8,
  //       disabled: 4,
  //       regular: 20,
  //     },
  //   },
  //   {
  //     id:6,

  //     name: 'Mall Parking',
  //     location: '789 Shopping Blvd, Mall Area',
  //     spots: {
  //       ev: 3,
  //       disabled: 1,
  //       regular: 15,
  //     },
  //   },
  // ];
  lots!:ParkingLot[]
  toview!:ParkingLot[]
  first: number = 0;

  rows: number = 5;

  ngOnInit()
  {
    this.fetchlots()
    setTimeout(() => {
      this.toview = this.lots.slice(0, this.rows);
  }, 200); 

}

  onPageChange(event: any) {
        this.first = event.first;
        this.rows = event.rows;
        this.toview = this.lots.slice(this.first,Math.min(this.first+this.rows,this.lots.length))
    }


    routetolot(event:any)
    {
      console.log(event)
      this.router.navigate(['reserve_lot'], { relativeTo: this.router.routerState.root.firstChild, state: { data: event }});
    }


    fetchlots()
    {
      console.log('here')
      this.lotsservice.getAllLots().subscribe(
        response=>{
            this.lots=response;
        },
        error=>
        {

        }
      )
    }
}



