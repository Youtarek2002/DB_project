import { Component } from '@angular/core';
import { LucideAngularModule, Car,MapPin,Zap,ArrowRight,Accessibility} from 'lucide-angular';
import { GoogleMapsModule } from '@angular/google-maps';
import { CommonModule } from '@angular/common';

export interface Spot {
  id: number;
  status: 'available' | 'occupied';
  type: 'disabled' | 'regular' | 'ev';
  price: number;
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
  long:any;
  lat:any;
}
@Component({
  selector: 'app-lot',
  imports: [LucideAngularModule,GoogleMapsModule,CommonModule],
  templateUrl: './lot.component.html',
  styleUrl: './lot.component.scss'
})

export class LotComponent {
  lot!:ParkingLot
  spots!:Spot[]
  disabledSpots!:any
  regularSpots!:any
  evSpots!:any
  readonly mapPinIcon = MapPin;
  readonly carIcon = Car;
  readonly zapIcon = Zap;
  readonly arrowIcon = ArrowRight;
  readonly wheelChairIcon = Accessibility;
  display: any;
  center: google.maps.LatLngLiteral = {
      lat: 22.2736308,
      lng: 70.7512555
  };

  directionsRenderer = new google.maps.DirectionsRenderer();
  directionsService = new google.maps.DirectionsService();
  ngOnInit()
  {
    this.lot = {
      id:1,
      name:'parking',
      location:'omar elmokhtar',
      spots:{
        ev:25,
        disabled:25,
        regular:50
      },
      long: 29.97525, 
      lat: 31.243861
    }
    this.center = {
      lat: this.lot.lat,
      lng: this.lot.long,
    };

    this.spots= [
      { id: 1, status: 'available', type: 'regular', price: 10 },
      { id: 2, status: 'occupied', type: 'disabled', price: 15 },
      { id: 3, status: 'available', type: 'ev', price: 20 },
      { id: 4, status: 'occupied', type: 'regular', price: 10 },
      { id: 5, status: 'available', type: 'disabled', price: 15 },
      { id: 6, status: 'occupied', type: 'ev', price: 25 },
      { id: 7, status: 'available', type: 'regular', price: 10 },
      { id: 8, status: 'available', type: 'ev', price: 20 },
      { id: 9, status: 'occupied', type: 'regular', price: 10 },
      { id: 10, status: 'available', type: 'disabled', price: 15 },
      { id: 11, status: 'occupied', type: 'ev', price: 25 },
      { id: 12, status: 'available', type: 'regular', price: 10 },
      { id: 13, status: 'occupied', type: 'disabled', price: 15 },
      { id: 14, status: 'available', type: 'regular', price: 10 },
      { id: 15, status: 'occupied', type: 'disabled', price: 15 },
    ]

    this.disabledSpots = this.extractSpotsByType(this.spots, 'disabled');
    this.regularSpots = this.extractSpotsByType(this.spots, 'regular');
    this.evSpots = this.extractSpotsByType(this.spots, 'ev');
    console.log(this.disabledSpots)
    console.log(this.evSpots)
    console.log(this.regularSpots)

  }

    moveMap(event: google.maps.MapMouseEvent) {
      if (event.latLng != null) this.center = (event.latLng.toJSON());
  }

  move(event: google.maps.MapMouseEvent) {
      if (event.latLng != null) this.display = event.latLng.toJSON();
  }
  startNavigation() {
    const lat = this.lot.lat;  // Replace with your latitude
  const lng = this.lot.long; // Replace with your longitude

  // Construct the URL for Google Maps with the location
  const googleMapsUrl = `https://www.google.com/maps/search/?api=1&query=${lat},${lng}`;

  // Open in a new tab
  window.open(googleMapsUrl, '_blank');
  }
  


  extractSpotsByType(spots: Spot[], type: 'disabled' | 'regular' | 'ev'): Spot[] {
    return spots.filter(spot => spot.type === type);
  }

}
  

