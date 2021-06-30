import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { SignupRequestPayload } from "../signup/signup-request.payload";
import { Observable } from "rxjs";
import { LocalStorageService } from "ngx-webstorage";
import { LoginRequestPayload } from "../login/login-request.payload";
import { map } from "rxjs/operators";
import { LoginResponsePayload } from "../login/login-response.payload";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private localStorage: LocalStorageService) { }

  signup(signupRequestPayload: SignupRequestPayload): Observable<any> {
    return this.http.post("http://localhost:8080/api/auth/signup", signupRequestPayload, { responseType: 'text' });
  }

  login(loginRequestPayload: LoginRequestPayload): Observable<boolean> {
    return this.http.post<LoginResponsePayload>("http://localhost:8080/api/auth/login", loginRequestPayload)
      .pipe(map(data => {
        this.localStorage.store('authenticationToken', data.authenticationToken);
        this.localStorage.store('username', data.username);
        this.localStorage.store('refreshToken', data.refreshToken);
        this.localStorage.store('expiresAt', data.expiresAt);
        return true;
      }));
  }
}
