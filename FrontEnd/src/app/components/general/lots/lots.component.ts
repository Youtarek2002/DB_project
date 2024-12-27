import { Component,Input} from '@angular/core';
import { LucideAngularModule, Car,MapPin,Zap,ArrowRight,Accessibility} from 'lucide-angular';
import { CommonModule } from '@angular/common';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { PaginatorModule } from 'primeng/paginator';

export interface PageEvent {
  first: number;
  rows: number;
  page: number;
  pageCount: number;
}

export interface ParkingLot {
  id: number;
  name: string;
  location: string;
  spots: {
    ev: number;
    disabled: number;
    regular: number;
  };
  // imageUrl: string;
}
@Component({
  selector: 'app-lots',
  imports: [LucideAngularModule,CommonModule,IconFieldModule,InputIconModule,PaginatorModule],
  templateUrl: './lots.component.html',
  styleUrl: './lots.component.scss'
})
export class LotsComponent {
  @Input() lot!: ParkingLot;
  readonly mapPinIcon = MapPin;
  readonly carIcon = Car;
  readonly zapIcon = Zap;
  readonly arrowIcon = ArrowRight;
  readonly wheelChairIcon = Accessibility;
  lots = [
    {
      id:1,
      name: 'Downtown Parking',
      location: '123 Main St, Downtown',
      spots: {
        ev: 5,
        disabled: 2,
        regular: 10,
      },
    },
    {
      id:2,

      name: 'Downtown Parking',
      location: '123 Main St, Downtown',
      spots: {
        ev: 5,
        disabled: 2,
        regular: 10,
      },
    },{
      id:3,

      name: 'Downtown Parking',
      location: '123 Main St, Downtown',
      spots: {
        ev: 5,
        disabled: 2,
        regular: 10,
      },
    },{
      id:4,

      name: 'Downtown Parking',
      location: '123 Main St, Downtown',
      spots: {
        ev: 5,
        disabled: 2,
        regular: 10,
      },
    },
    {
      id:5,

      name: 'Airport Parking',
      location: '456 Terminal Rd, Airport',
      spots: {
        ev: 8,
        disabled: 4,
        regular: 20,
      },
    },
    {
      id:6,

      name: 'Mall Parking',
      location: '789 Shopping Blvd, Mall Area',
      spots: {
        ev: 3,
        disabled: 1,
        regular: 15,
      },
    },
  ];
  toview!:ParkingLot[]
  first: number = 0;

  rows: number = 5;

  ngOnInit()
  {
    this.toview = this.lots.slice(0,this.rows);
  }

  onPageChange(event: any) {
        this.first = event.first;
        this.rows = event.rows;
        this.toview = this.lots.slice(this.first,Math.min(this.first+this.rows,this.lots.length))
    }
}



