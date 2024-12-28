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
  selector: 'app-admin-navbar',
  imports: [ FormsModule, ReactiveFormsModule,FloatLabelModule,ButtonModule,RouterModule,RouterLink,InputTextModule,CommonModule,DropdownModule,CalendarModule,LucideAngularModule],
  templateUrl: './admin-navbar.component.html',
  styleUrl: './admin-navbar.component.scss'
})
export class AdminNavbarComponent {
  constructor(private router:Router,private activatedRoute : ActivatedRoute){}

  readonly FileIcon = File;
  readonly CarIcon = Car;
  readonly MenuIcon = Menu;
  readonly CalendarIcon = Calendar;
  readonly BuildingIcon = Building2;
  readonly BarChartIcon = BarChart3
  readonly NotificationIcon = Bell
  navItems: NavItem[] = [
    { label: 'Pending Lots', icon: this.BuildingIcon, route: 'pending_lots' },
    { label: 'Reports', icon: this.BarChartIcon, route: 'reports' },
    {label: 'Notifactions',icon:this.NotificationIcon, route:'notifications'}
  ];
  isMobileMenuOpen = false;

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }


  navigate(route: string): void {
    if(route==='pending_lots'|| route === 'reports')
    this.router.navigate([route], { relativeTo: this.router.routerState.root.firstChild ,state:{adminid:localStorage.getItem("id")}});
    else
      this.router.navigate([route], { relativeTo: this.router.routerState.root.firstChild});


  }
}
