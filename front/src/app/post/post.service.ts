import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import {CreatePostPayload} from "./create-post/create-post-payload";

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private httpClient: HttpClient) {}

  getAllPosts(): Observable<any> {
    return this.httpClient.get("http://localhost:8080/api/posts");
  }

  createPost(postPayload: CreatePostPayload): Observable<any> {
    return this.httpClient.post("http://localhost:8080/api/posts", postPayload);
  }

  getPost(postId: number): Observable<any> {
    return this.httpClient.get("http://localhost:8080/api/posts/" + postId);
  }

  getAllPostsByUser(username: string): Observable<any> {
    return this.httpClient.get("http://localhost:8080/api/posts/by-user/" + username);
  }
}
