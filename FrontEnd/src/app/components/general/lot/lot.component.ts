import { Component } from '@angular/core';
import { LucideAngularModule, Car,MapPin,Zap,ArrowRight,Accessibility} from 'lucide-angular';
import { GoogleMapsModule } from '@angular/google-maps';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { SpotsserviceService } from '../../../service/spots/spotsservice.service';
import { CalendarModule } from 'primeng/calendar';
import { FormsModule, FormControl } from '@angular/forms';
import { ReservationService } from '../../../service/reservation/reservation.service';


export interface Spot {
  id: number;
  status: 'AvAILABLE' | 'OCCUPIED';
  type: 'DISABLED' | 'REGULAR' | 'EV';
  price: number;
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
  selector: 'app-lot',
  imports: [LucideAngularModule,GoogleMapsModule,CommonModule,CalendarModule,FormsModule],
  templateUrl: './lot.component.html',
  styleUrl: './lot.component.scss'
})

export class LotComponent {
  lot!:ParkingLot
  spots!:Spot[]
  disabledSpots!:any
  regularSpots!:any
  evSpots!:any
  // startTimeControl = new FormControl();
  // endTimeControl = new FormControl();
  startTime!: string;
  endTime!: string;
  startdisplay!:any
  selectedSpot: any = null;

  enddisplay!:any
  readonly mapPinIcon = MapPin;
  readonly carIcon = Car;
  readonly zapIcon = Zap;
  readonly arrowIcon = ArrowRight;
  readonly wheelChairIcon = Accessibility;
  display: any;
  start:any;
  center: google.maps.LatLngLiteral = {
      lat: 22.2736308,
      lng: 70.7512555
  };

  directionsRenderer = new google.maps.DirectionsRenderer();
  directionsService = new google.maps.DirectionsService();

  constructor(private route:ActivatedRoute,private router:Router,private spotservice:SpotsserviceService,private reserveservice:ReservationService){
    const navigation = this.router.getCurrentNavigation();
    this.lot = navigation?.extras.state?.['data'] || null;
    if (this.lot) {
      sessionStorage.setItem('selectedLot', JSON.stringify(this.lot));
      this.center = {
        lat: this.lot.latitude,
        lng: this.lot.longitude,
      };
    } else {
      const storedLot = sessionStorage.getItem('selectedLot');
      if (storedLot) {
        this.lot = JSON.parse(storedLot);
        this.center = {
          lat: this.lot.latitude,
          lng: this.lot.longitude,
        };
      }
    }

    this.calculateTimes();

  }


  ngOnInit()
  {
    
    this.fetchspots()
    setTimeout(() => {
      this.disabledSpots = this.extractSpotsByType(this.spots, 'DISABLED');
      this.regularSpots = this.extractSpotsByType(this.spots, 'REGULAR');
      this.evSpots = this.extractSpotsByType(this.spots, 'EV');
      }, 200); 
  }


  calculateTimes() {
    const currentDate = new Date();
    const currentHour = currentDate.getHours();
    const currentMinutes = currentDate.getMinutes();
    
    // Calculate ceiling of current time
    let startHour = Math.ceil(currentHour + currentMinutes / 60);

    this.startdisplay  = new Date(
      currentDate.getFullYear(),
      currentDate.getMonth(),
      currentDate.getDate(),
      startHour,
      0,  // Minutes
      0,  // Seconds
      0   // Milliseconds
    );

    this.enddisplay = new Date(
      currentDate.getFullYear(),
      currentDate.getMonth(),
      currentDate.getDate(),
      startHour+1,
      0,  // Minutes
      0,  // Seconds
      0   // Milliseconds
    );

    

    // Format start and end time
    const formatTwoDigits = (num: number): string => num.toString().padStart(2, '0');

    this.startTime = `${currentDate.getFullYear()}-${(currentDate.getMonth() + 1)
      .toString()
      .padStart(2, '0')}-${currentDate.getDate()
      .toString()
      .padStart(2, '0')}T${formatTwoDigits(startHour % 24)}:00:00`;

    this.endTime = `${currentDate.getFullYear()}-${(currentDate.getMonth() + 1)
      .toString()
      .padStart(2, '0')}-${currentDate.getDate()
      .toString()
      .padStart(2, '0')}T${formatTwoDigits((startHour % 24) + 1)}:00:00`;
    console.log(this.startTime)
    console.log(this.endTime)
  }




    moveMap(event: google.maps.MapMouseEvent) {
      if (event.latLng != null) this.center = (event.latLng.toJSON());
  }

  move(event: google.maps.MapMouseEvent) {
      if (event.latLng != null) this.display = event.latLng.toJSON();
  }
  startNavigation() {
    const lat = this.lot.latitude;  // Replace with your latitude
  const lng = this.lot.longitude; // Replace with your longitude

  // Construct the URL for Google Maps with the location
  const googleMapsUrl = `https://www.google.com/maps/search/?api=1&query=${lat},${lng}`;

  // Open in a new tab
  window.open(googleMapsUrl, '_blank');
  }
  


  extractSpotsByType(spots: Spot[], type: 'DISABLED' | 'REGULAR' | 'EV'): Spot[] {
    return spots.filter(spot => spot.type === type);
  }

  fetchspots()
  {
    const params = {
      startTime: this.startTime,
      endTime: this.endTime,
      lotId: this.lot.id.toString()
    };
    console.log(params)
    this.spotservice.getspotsfromnow(params).subscribe(
      response=>{
        console.log(response)
        this.spots = response.spots;
        this.start = response.startIndex
      },
      error=>{
        console.log(error)
      }
    )
    
  }
  onTimeChange()
  {
    const year = this.startdisplay.getFullYear();
    const month = String(this.startdisplay.getMonth() + 1).padStart(2, '0');
    const day = String(this.startdisplay.getDate()).padStart(2, '0');
    const hours = String(this.startdisplay.getHours()).padStart(2, '0');
    const minutes = String(this.startdisplay.getMinutes()).padStart(2, '0');
    const seconds = String(this.startdisplay.getSeconds()).padStart(2, '0');
    let startHour = Math.ceil(this.startdisplay.getHours() + this.startdisplay.getMinutes() / 60);
    this.startdisplay = new Date(
      this.startdisplay.getFullYear(),
      this.startdisplay.getMonth(),
      this.startdisplay.getDate(),
      startHour,
      0,  // Minutes
      0,  // Seconds
      0   // Milliseconds
      );

      const formatTwoDigits = (num: number): string => num.toString().padStart(2, '0');

      this.startTime = `${year}-${(month)
        .toString()
        .padStart(2, '0')}-${day
        .toString()
        .padStart(2, '0')}T${formatTwoDigits(startHour % 24)}:00:00`;


      // this.startTime = `${year}-${month}-${day}T${hours}:00:00`;
      const yearr = this.enddisplay.getFullYear();
      const monthh = String(this.enddisplay.getMonth() + 1).padStart(2, '0');
      const dayy = String(this.enddisplay.getDate()).padStart(2, '0');
    const hourss = String(this.enddisplay.getHours()).padStart(2, '0');
    const minutess = String(this.enddisplay.getMinutes()).padStart(2, '0');
    const secondss = String(this.enddisplay.getSeconds()).padStart(2, '0');
    let startHourr = Math.ceil(this.enddisplay.getHours() + this.enddisplay.getMinutes() / 60);
    this.enddisplay = new Date(
      this.enddisplay.getFullYear(),
      this.enddisplay.getMonth(),
      this.enddisplay.getDate(),
      startHourr,
      0,  // Minutes
      0,  // Seconds
      0   // Milliseconds
      );
      this.endTime = `${yearr}-${(monthh)
        .toString()
        .padStart(2, '0')}-${dayy
        .toString()
        .padStart(2, '0')}T${formatTwoDigits(startHourr % 24)}:00:00`; 
        this.fetchspots()
  }
  
  selectspot(event:any)
  {
    this.selectedSpot = event
  }
  reserve()
  {
    if(this.selectedSpot)
    {


    const date1 = new Date(this.startTime);
    const date2 = new Date(this.endTime);

    // Calculate the difference in milliseconds
    const diffInMs = date2.getTime() - date1.getTime();

    // Convert milliseconds to hours
    const hoursDifference = diffInMs / (1000 * 60 * 60);
    // console.log(hoursDifference)
    // console.log(this.selectedSpot)
    let duration = "PT";
    if (hoursDifference > 0) {
      duration += `${hoursDifference}H`;
    }
    const body = {
    penalty: 0,
    startTime: this.startTime,
    endTime: this.endTime,
    duration: duration,
    userId: localStorage.getItem("id"),
    parkingSpotId: this.selectedSpot.id,
    transactionId: 1,
	  cost:hoursDifference * this.selectedSpot.price 
    }
    console.log(body)
    console.log(duration)
    this.reserveservice.reserve(body).subscribe(
      response=>{
        if(response.success)
        {
          alert(response.message)
        }
        else{
          alert(response.message)
        }
      },
      error=>{
        console.log(error)
      }
      
    )

  }


  else{
    alert("select a spot first")
  }
}
}


