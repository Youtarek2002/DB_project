import { Routes } from '@angular/router';
import { LoginComponent } from './components/authentication/login/login.component';
import { SignupComponent } from './components/authentication/signup/signup.component';
import { LotsComponent } from './components/general/lots/lots.component';
import { LotComponent } from './components/general/lot/lot.component';
import { DashboardComponent } from './components/user/dashboard/dashboard.component';
import { ReservationsComponent } from './components/general/reservations/reservations.component';
import { NotificationsComponent } from './components/general/notifications/notifications.component';
import { ManagerDashboardComponent } from './components/manager/manager-dashboard/manager-dashboard.component';
import { ReportsComponent } from './components/general/reports/reports.component';
import { AdminDashboardComponent } from './components/admin/admin-dashboard/admin-dashboard.component';
export const routes: Routes = [
    {
        path:'',
        redirectTo:'login',
        pathMatch:'full'
    },
    {
        path:'login',
        component:LoginComponent
    },
    {
        path:'signup',
        component:SignupComponent
    },
    // {
    //     path:'lots',
    //     component:LotsComponent
    // },
    // {
    //     path:'reserve',
    //     component:LotsComponent
    // },
    // {
    //     path:'reserve/lot',
    //     component:LotComponent
    // },
    {
        path: 'userdashboard',
        component: DashboardComponent,
        children: [
          { path: 'reserve', component: LotsComponent },
          { path: 'reserve_lot', component: LotComponent }, // Full redirect for lot
          { path: 'reservations', component: ReservationsComponent }, 
          { path: 'notifications', component:NotificationsComponent},
          { path: '', redirectTo: 'reserve', pathMatch: 'full' }, // Default child route
        ]
      },
      {
        path: 'managerdashboard',
        component: ManagerDashboardComponent,
        children: [
          { path: 'reserve', component: LotsComponent },
          { path: 'reserve_lot', component: LotComponent }, // Full redirect for lot
          { path: 'reservations', component: ReservationsComponent },
          { path: 'lots',component:LotsComponent},
          { path: 'reports',component:ReportsComponent},
          { path: 'notifications', component:NotificationsComponent},
          { path: '', redirectTo: 'reserve', pathMatch: 'full' }, // Default child route
        ]
      },
      {
        path: 'admindashboard',
        component: AdminDashboardComponent,
        children: [
          { path: 'pending_lots',component:LotsComponent,data: { state: { adminid: localStorage.getItem("id") } } },
          { path: 'reports',component:ReportsComponent},
          { path: 'notifications', component:NotificationsComponent},
          { path: '', redirectTo: 'pending_lots', pathMatch: 'full' }, 
        ]
      },
      
    
    


];
