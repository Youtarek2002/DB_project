import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import {ButtonModule} from 'primeng/button'
import { Router, RouterLink, RouterModule, RouterOutlet } from '@angular/router';
import { jwtDecode } from "jwt-decode";
import { AuthenticationService } from '../../../service/authentication/authentication.service';
import { PasswordModule } from 'primeng/password';
// import { AuthenticationService } from '../../../services/authentication/authentication.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, ReactiveFormsModule, FloatLabelModule, ButtonModule, RouterModule, RouterLink, InputTextModule, DialogModule,PasswordModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  constructor(private fb : FormBuilder,private router:Router,private auth : AuthenticationService){}
  loginForm!: FormGroup
  ngOnInit(){  
  this.loginForm = this.fb.group({
    email:['',[Validators.required]],
    password:['',Validators.required]
  })
  }




  login()
  {
    this.auth.login(this.loginForm.value).subscribe(
      response=>{
        console.log(response)
        if(response.success)
        {
          localStorage.setItem("token",response.data)
          const decodedToken: any = jwtDecode(response.data);
          localStorage.setItem("id",decodedToken.id)
          console.log(decodedToken);
          if(decodedToken.role === "DRIVER")
          {
            console.log('here')
            this.router.navigate(['/userdashboard'])
          }
          else if(decodedToken.role === "SYSTEM_ADMIN")
          {
            this.router.navigate(['/admindashboard'],{state:{adminid:localStorage.getItem("id")}}) 
          }
          else if(decodedToken.role === "PARKING_LOT_MANAGER")
          {
            //navigate to manager dashboard
            this.router.navigate(['/managerdashboard'])

          }
        }
        else
        {
          alert(response.message)
        }

      },
      error=>{
        console.log(error)
      }

    )
  }
}
