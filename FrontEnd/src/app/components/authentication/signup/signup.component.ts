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

type ErrorMessageFunction = (params: any) => string;

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ FormsModule, ReactiveFormsModule,FloatLabelModule,ButtonModule,RouterModule,RouterLink,InputTextModule,CommonModule,DropdownModule,CalendarModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {
  errorMessage: { [key: string]: ErrorMessageFunction }= {
    'required'  : (params:any)  => `This field is required`,
    'maxlength' : (params:any)  => `Maximum ${params.requiredLength} characters are allowed`,
    'minlength' : (params:any)  => `Minimum ${params.requiredLength} characters are required`,
    'email'     : (params:any)  => 'This field must be an email',
};
  constructor(private fb : FormBuilder,private auth : AuthenticationService,private router: Router, private route: ActivatedRoute){}
  signupForm!:FormGroup
  errorr!: string;

  isManagerSignup: boolean = false;
  isUserSignup: boolean = true;
  usergender:any[]=[{name:'Male',value:'MALE'},{name:'Femlae',value:'FEMALE'}]

  ngOnInit(){
    this.signupForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(15)]],
      fname: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(15)]],
      lname: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(15)]],
      email: ['', [Validators.required, Validators.email, Validators.minLength(15), Validators.maxLength(30)]],
      phone: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
      confirmpassword: ['', [Validators.required]],
      licencePlate: ['',Validators.required],
      paymentMethod: ['',[Validators.required]]
    },
    { validators: [this.passwordmatch, this.validpassword.bind(this)] });
    
      this.route.queryParams.subscribe((params) => {
        this.isManagerSignup = params['role'] === 'manager';
        this.isUserSignup = !this.isManagerSignup
        console.log(this.isManagerSignup)
        console.log(this.isUserSignup)
      });
  }

  signup()
  { 
    if(this.isManagerSignup)
    {
      console.log('here')
      this.router.navigate(['/managersignup'],
      {state:{data: this.signupForm.value}}
      )
    }
    else{
    if(this.signupForm.valid)
    {
    this.auth.signup(this.signupForm.value).subscribe(
      response=>{
        if(response.success)
        {
          alert(response.message)
          this.router.navigate(['/login'])

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


}
passwordmatch(signupform: FormGroup): { [key: string]: boolean } | null{
  const password = signupform.get('password')?.value;
  const confirmpassword = signupform.get('confirmpassword')?.value;
  return password === confirmpassword ? null : {mismatch: true};
}

returnerror():String|null{
  return this.errorr
}

validpassword(signupform: FormGroup): {[key : string]: boolean} | null{
  const password = signupform.get('password')?.value;
  if( password !== password.toLowerCase() && password === password.toUpperCase())
  {    
    this.errorr='Password must contain a lowercase letter'
    console.log(this.errorr)
    return {notvalid : true}
  }
  if( password === password.toLowerCase() && password !== password.toUpperCase())
  {   
    this.errorr='Password must contain an uppercase letter'
    console.log(this.errorr)
    return {notvalid : true}
  }
  if(!(/\d/.test(password)))
  {    
    this.errorr='Password must contain a number'

    return {notvalid : true}
  }
  this.errorr = ''
  return null;

}

getErrorMessage(control: any): String | null {

  if (control.errors) {
    const errorKey = Object.keys(control.errors)[0]; // Get the first error key
    const errorParams = control.errors[errorKey]; // Get the error parameters

    // Check if the error message function exists and return the message
    if (this.errorMessage[errorKey]) {
      return this.errorMessage[errorKey](errorParams);
    }
  }
  return null;
}

  managersignup()
  {
    if(!this.isManagerSignup){
    this.router.navigate(['/signup'], { queryParams: { role: 'manager' } })
    }
  }
  usersignup(){
    if(!this.isUserSignup){
      this.router.navigate(['/signup'])
      }
  }

}
