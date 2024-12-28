import { Component,Input} from '@angular/core';
import { LucideAngularModule, Car,MapPin,Zap,ArrowRight,Accessibility, Clock} from 'lucide-angular';
import { CommonModule } from '@angular/common';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { PaginatorModule } from 'primeng/paginator';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { ReservationService } from '../../../service/reservation/reservation.service';


export interface Reservation {
  id: number;
  penalty: number;
  startTime: string; // ISO 8601 datetime format
  endTime: string; // ISO 8601 datetime format
  duration: number | null;
  userId: number;
  parkingSpotId: number;
  transactionId: number;
  cost: number;
}

export interface LocationReservations {
  location: string;
  longitude: number;
  latitude: number;
  reservations: Reservation[];
}




@Component({
  selector: 'app-reservations',
  imports: [RouterOutlet,LucideAngularModule,CommonModule,IconFieldModule,InputIconModule,PaginatorModule],
  templateUrl: './reservations.component.html',
  styleUrl: './reservations.component.scss'
})
export class ReservationsComponent {
  readonly mapPinIcon = MapPin;
  readonly carIcon = Car;
  readonly zapIcon = Zap;
  readonly arrowIcon = ArrowRight;
  readonly wheelChairIcon = Accessibility;
  readonly clockIcon = Clock;

  first: number = 0;
  toview!:any
  rows: number = 5;
  reservations!:LocationReservations[]
  constructor(private service: ReservationService){}
  ngOnInit()
  {
    this.getreservations()
    setTimeout(() => {
      this.toview = this.reservations.slice(0, this.rows);
  }, 200); 
  }
  
  onPageChange(event:any)
  {
    this.first = event.first;
    this.rows = event.rows;
    this.toview = this.reservations.slice(this.first,Math.min(this.first+this.rows,this.reservations.length))
  }
  navigate(event:any)
  {
    const lat = event.latitude;  // Replace with your latitude
    const lng = event.longitude; // Replace with your longitude
  
    // Construct the URL for Google Maps with the location
    const googleMapsUrl = `https://www.google.com/maps/search/?api=1&query=${lat},${lng}`;
  
    // Open in a new tab
    window.open(googleMapsUrl, '_blank');
  }

  getreservations()
  {
    const params = {
    
      userId: localStorage.getItem("id")

    };
    this.service.getuserreservations(params).subscribe(
      response=>{
          this.reservations = response
      },
      error=>{
          console.log(error)
      }
    )
  }

}
