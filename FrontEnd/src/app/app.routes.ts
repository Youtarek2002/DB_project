import { Routes } from '@angular/router';
import { LoginComponent } from './components/authentication/login/login.component';
import { SignupComponent } from './components/authentication/signup/signup.component';
import { LotsComponent } from './components/general/lots/lots.component';
import { LotComponent } from './components/general/lot/lot.component';

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
    {
        path:'lots',
        component:LotsComponent
    },
    {
        path:'reserve',
        component:LotsComponent
    },
    {
        path:'reserve/lot',
        component:LotComponent
    }

];
