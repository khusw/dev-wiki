import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { LoginRequestPayload } from "./login-request.payload";
import { AuthService } from "../shared/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  loginRequestPayload: LoginRequestPayload;
  registerSuccessMessage: string;
  isError: boolean;

  constructor(private authService: AuthService, private activatedRoute: ActivatedRoute, private router: Router, private toastr: ToastrService) {
    this.loginRequestPayload = {
      email: '',
      password: ''
    };
  }

  ngOnInit() {
    this.loginForm = new FormGroup({
      email: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });

    this.activatedRoute.queryParams.subscribe(params => {
      if (params.registered !== undefined && params.registered === "true") {
        this.toastr.success("Signup Successful");
        this.registerSuccessMessage = "Please check your email for activation your account before login";
      }
    });
  }

  login() {
    this.loginRequestPayload.email = this.loginForm.get('email').value;
    this.loginRequestPayload.password = this.loginForm.get('password').value;

    this.authService.login(this.loginRequestPayload).subscribe(data => {
      if (data) {
        console.log("after login data : ", data);
        this.isError = false;
        this.router.navigateByUrl("/");
        this.toastr.success("Login Successful");
      } else {
        this.isError = true;
      }
    })
  }

}
