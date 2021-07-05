import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms'
import { SignupRequestPayload } from "./signup-request.payload";
import { AuthService } from "../shared/auth.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signupRequestPayload: SignupRequestPayload;
  signupForm: FormGroup;

  constructor(private authService: AuthService, private router: Router, private toastr: ToastrService) {
    this.signupRequestPayload = {
      email: '',
      username: '',
      password: ''
    }
  }

  ngOnInit() {
    this.signupForm = new FormGroup({ // signupForm 멤버 변수 초기화
      username: new FormControl('', Validators.required), // 검증을 위한 작업 추가
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required)
    })
  }

  signup() {
    this.signupRequestPayload.email = this.signupForm.get("email").value;
    this.signupRequestPayload.username = this.signupForm.get("username").value;
    this.signupRequestPayload.password = this.signupForm.get("password").value;

    this.authService.signup(this.signupRequestPayload).subscribe(() => {
      this.router.navigate(["/login"], { queryParams: { registered: "true" } });
    }, () => {
      this.toastr.error("Registration Failed! Try Again");
    })
  }

}
