import { Component, OnInit } from '@angular/core';
import {FormGroup, FormControl, Validators} from '@angular/forms'

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signupForm: FormGroup

  constructor() { }

  ngOnInit(): void {
    this.signupForm = new FormGroup({ // signupForm 멤버 변수 초기화
      username: new FormControl(null, Validators.required), // 검증을 위한 작업 추가
      email: new FormControl(null, [Validators.required, Validators.email]),
      password: new FormControl(null, Validators.required)
    })
  }

}
