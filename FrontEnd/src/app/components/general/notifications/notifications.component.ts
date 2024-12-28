import { Component,Input} from '@angular/core';
import { LucideAngularModule, Car,MapPin,Zap,ArrowRight,Accessibility, Clock,Bell} from 'lucide-angular';
import { CommonModule } from '@angular/common';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { PaginatorModule } from 'primeng/paginator';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { ReservationService } from '../../../service/reservation/reservation.service';
import { NotificationserviceService } from '../../../service/notificationservice/notificationservice.service';




@Component({
  selector: 'app-notifications',
  imports: [RouterOutlet,LucideAngularModule,CommonModule,IconFieldModule,InputIconModule,PaginatorModule],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.scss'
})
export class NotificationsComponent {

  notifications!:any[]
  nonotification:any[] = ['No notifications to view']
  toview!:any[]
  readonly bellIcon = Bell
  constructor(private service : NotificationserviceService){}
  first: number = 0;

  rows: number = 5;


  ngOnInit()
  {
    this.getusernotification()
    setTimeout(() => {
      if(this.notifications)
        this.toview = this.notifications.slice(0, this.rows);
      console.log(this.toview)
  }, 200); 
  }



  getusernotification()
  {
    const params = {
      userId : localStorage.getItem("id")
    }
    this.service.getusernotification(params).subscribe(
      response=>{
        if(response.success && response.data)
        {

          this.notifications=response.data
          this.notifications.reverse()
        }
        else
        {
          alert(response.message)
        }
      },
      error=>{

      }
    )
  }
  onPageChange(event:any)
  {
    this.first = event.first;
    this.rows = event.rows;
    this.toview = this.notifications.slice(this.first,Math.min(this.first+this.rows,this.notifications.length))
  }
}
