import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { CommonModule } from '@angular/common';
import {ButtonModule} from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { AuthenticationService } from '../../../service/authentication/authentication.service';
import { ActivatedRoute, Router, RouterLink, RouterModule } from '@angular/router';
import { CalendarModule } from 'primeng/calendar';
import { LucideAngularModule, File, Car, Menu, Calendar,Building2,BarChart3,Bell} from 'lucide-angular';

export interface NavItem {
  label: string;
  icon: any;
  route: string;
}
@Component({
  selector: 'app-user-navbar',
  imports: [ FormsModule, ReactiveFormsModule,FloatLabelModule,ButtonModule,RouterModule,RouterLink,InputTextModule,CommonModule,DropdownModule,CalendarModule,LucideAngularModule],
  templateUrl: './user-navbar.component.html',
  styleUrl: './user-navbar.component.scss'
})
export class UserNavbarComponent {

  constructor(private router:Router,private activatedRoute : ActivatedRoute){}

  readonly FileIcon = File;
  readonly CarIcon = Car;
  readonly MenuIcon = Menu;
  readonly CalendarIcon = Calendar;
  readonly BuildingIcon = Building2;
  readonly BarChartIcon = BarChart3
  readonly NotificationIcon = Bell
  navItems: NavItem[] = [
    { label: 'Reserve', icon: this.CarIcon, route: 'reserve' },
    { label: 'My Reservations', icon: this.CalendarIcon, route: 'reservations' },
    {label: 'Notifactions',icon:this.NotificationIcon, route:'notifications'}
  ];
  isMobileMenuOpen = false;

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }
  navigate(route: string): void {
    console.log(this.activatedRoute.parent)
    this.router.navigate([route], { relativeTo: this.router.routerState.root.firstChild });

  }
}
