import { Component,Input, ViewChild} from '@angular/core';
import { LucideAngularModule, Car,MapPin,Zap,ArrowRight,Accessibility} from 'lucide-angular';
import { CommonModule } from '@angular/common';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { DialogModule } from 'primeng/dialog';
import { GoogleMap, GoogleMapsModule, MapMarker } from '@angular/google-maps';

import { PaginatorModule } from 'primeng/paginator';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { LotsserviceService } from '../../../service/lotsservice/lotsservice.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
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
  imports: [GoogleMapsModule,FormsModule, ReactiveFormsModule,RouterOutlet,LucideAngularModule,CommonModule,IconFieldModule,InputIconModule,PaginatorModule,DialogModule,InputTextModule],
  templateUrl: './lots.component.html',
  styleUrl: './lots.component.scss'
})
export class LotsComponent {
  constructor(private router:Router,private route: ActivatedRoute, private lotsservice:LotsserviceService, private fb:FormBuilder){}
  @Input() lot!: ParkingLot;
  // @ViewChild(MapMarker) marker: MapMarker | undefined;
  @ViewChild(GoogleMap, { static: false }) map1!: GoogleMap;
  @ViewChild('map2', { static: false }) map2!: GoogleMap;

  marker1: google.maps.Marker | null = null;
  marker2: google.maps.Marker | null = null;

  center1: google.maps.LatLngLiteral = {
    lat: 51.206468647511493,
    lng: 29.924815893173218,
  };

  center2: google.maps.LatLngLiteral = {
    lat: 31.206468647511493,
    lng: 29.924815893173218,
  };

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
  showmodal:boolean=false
  showmap:boolean=false
  lotForm!:FormGroup

  directionsRenderer = new google.maps.DirectionsRenderer();
  directionsService = new google.maps.DirectionsService();
  display: any;
  adminid:any;
  ngOnInit()
  {
    this.lotForm = this.fb.group(
      {
        disabled_count: ['', [Validators.required]],
        disabled_price: ['', [Validators.required]],
        regular_count: ['', [Validators.required]],
        regular_price: ['', [Validators.required]],
        EV_count: ['', Validators.required],
        EV_price: ['', [Validators.required]],
        location: ['', [Validators.required]],
        longitude: ['',Validators.required],
        latitude: ['',[Validators.required]],
        parking_lot_manager:[localStorage.getItem("id"),[Validators.required]]
      }
    )


    this.managerid = history.state.managerid; 
    this.adminid = history.state.adminid;
    if(this.managerid)
    {
      this.fetchmanagerlots()
      
    }
    else if(this.adminid)
    {
      this.fetchpending()
     
    }

    else{
    this.fetchlots()
    
}
// this.initAutocomplete();

}
ngAfterViewInit() {

  // this.fetchlots()
//   setTimeout(() => {
//     this.initAutocomplete()
// }, 2000); 



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

    fetchpending()
    {
      this.lotsservice.getpendinglots().subscribe(
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
    deletelot(event:any)
    {
        const params = {
          id:event.id
        }
        this.lotsservice.deletelot(params).subscribe(
          response=>{
            alert(response)
          },
          error=>{
            console.log(error)
          }
          
        )
        this.fetchmanagerlots()


    }
    addlot(event:any)
    {
      this.lotForm.patchValue({longitude:this.center1.lng,latitude:this.center1.lat})
      if(this.lotForm.valid)
      {
        this.lotsservice.addlot(this.lotForm.value).subscribe(
          response=>{
            alert(response.message)
            this.lotForm.reset()
            this.showmodal=false
          },
          error=>{
            console.log(error)
          }

        )
      }
    }
    admitlot(event:any)
    {
      const params = {
        id:event.id
      }
      this.lotsservice.admitlot(params).subscribe(
        response=>{
          if(response.success)
          {
            alert("Lot admitted successfully")
            this.fetchpending()
          }
        },
        error=>{
          console.log(error)
        }
      )
    }
    denylot(event:any)
    {
      const params = {
        id:event.id
      }
      this.lotsservice.denylot(params).subscribe(
        response=>{
          if(response.success)
          {
            alert("Lot denied successfully")
            this.fetchpending()
          }
        },
        error=>{
          console.log(error)
        }
      )


    }

    togglemodal()
    {
      this.showmodal=!this.showmodal
      setTimeout(() => {
        this.initAutocomplete1()
    }, 2000);     }
    togglemap()
    {
      
      this.showmap=!this.showmap
      setTimeout(() => {
        this.initAutocomplete2()
    }, 2000);
    }


    moveMap(event: google.maps.MapMouseEvent) {
      if (event.latLng != null) this.center1 = (event.latLng.toJSON());
      console.log(this.center1)
  }
  move(event: google.maps.MapMouseEvent) {
    if (event.latLng != null) this.display = event.latLng.toJSON();
}


initAutocomplete1() {
  const input = document.getElementById('pac-input1') as HTMLInputElement;

  if (!input) {
    console.error('Search input for Map 1 not found!');
    return;
  }

  const searchBox = new google.maps.places.SearchBox(input);
  this.map1.googleMap?.addListener('bounds_changed', () => {
    const bounds = this.map1.googleMap?.getBounds();
    if (bounds) searchBox.setBounds(bounds);
  });

  searchBox.addListener('places_changed', () => {
    const places = searchBox.getPlaces();
    if (!places || places.length === 0) return;

    const place = places[0];
    if (place.geometry?.location) {
      const location = {
        lat: place.geometry.location.lat(),
        lng: place.geometry.location.lng(),
      };
      this.addMarker1(location);
    }
  });
}
initAutocomplete2() {
  const input = document.getElementById('pac-input2') as HTMLInputElement;

  if (!input) {
    console.error('Search input for Map 2 not found!');
    return;
  }

  const searchBox = new google.maps.places.SearchBox(input);
  this.map2.googleMap?.addListener('bounds_changed', () => {
    const bounds = this.map2.googleMap?.getBounds();
    if (bounds) searchBox.setBounds(bounds);
  });

  searchBox.addListener('places_changed', () => {
    const places = searchBox.getPlaces();
    if (!places || places.length === 0) return;

    const place = places[0];
    if (place.geometry?.location) {
      const location = {
        lat: place.geometry.location.lat(),
        lng: place.geometry.location.lng(),
      };
      this.addMarker2(location);
    }
  });
}

addMarker1(position: { lat: number; lng: number }) {
  if (this.marker1) this.marker1.setMap(null);
  this.marker1 = new google.maps.Marker({
    map: this.map1.googleMap!,
    position: position,
  });
  this.center1 = position;
}

addMarker2(position: { lat: number; lng: number }) {
  if (this.marker2) this.marker2.setMap(null);
  this.marker2 = new google.maps.Marker({
    map: this.map2.googleMap!,
    position: position,
  });
  this.center2 = position;
}
preventEnter(event: KeyboardEvent) {
  if (event.key === 'Enter') {
    event.preventDefault(); // Prevent form submission
  }
}

search()
{
  const params = {
    userLat:this.center2.lat,
    userLng:this.center2.lng
  }
  this.lotsservice.getnearest(params).subscribe(
    response=>{
      this.lots=response;
      setTimeout(() => {
        this.toview = this.lots.slice(0, this.rows);
    }, 200);    
    this.showmap=false
  },
    error=>{
      console.log(error)
    }
  )
}
}



