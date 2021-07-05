import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private httpClient: HttpClient) {}

  getAllPosts(): Observable<any> {
    return this.httpClient.get("http://localhost:8080/api/posts/");
  }
}
