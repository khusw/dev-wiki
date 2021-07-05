import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import { Injectable } from "@angular/core";
import {BehaviorSubject, Observable, throwError} from "rxjs";
import { AuthService } from "./auth/shared/auth.service";
import {catchError, switchMap} from "rxjs/operators";
import {LoginResponsePayload} from "./auth/login/login-response.payload";

@Injectable({
  providedIn: "root"
})
export class TokenInterceptor implements HttpInterceptor {

    isTokenRefreshing: boolean = false;
    refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject(null);

    constructor(public authService: AuthService) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      if (this.authService.getJWTToken()) {
        this.addToken(req, this.authService.getJWTToken());
      }

      return next.handle(req).pipe(catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 403) {
          return this.handleAuthErrors(req, next);
        } else {
          return throwError(error);
        }
      }));
    }

    private handleAuthErrors(req: HttpRequest<any>, next: HttpHandler) {
      if (!this.isTokenRefreshing) {
        this.isTokenRefreshing = true;
        this.refreshTokenSubject.next(null);

        return this.authService.refreshToken().pipe(
          switchMap((refreshTokenResponse: LoginResponsePayload) => {
            this.isTokenRefreshing = false;
            this.refreshTokenSubject.next(refreshTokenResponse.authenticationToken);
            return next.handle(this.addToken(req, refreshTokenResponse.authenticationToken));
          })
        )
      }
    }

    private addToken(req: HttpRequest<any>, jwtToken: string) {
      return req.clone({
        headers: req.headers.set("Authorization", "Bearer " + jwtToken)
      });
    }
}
