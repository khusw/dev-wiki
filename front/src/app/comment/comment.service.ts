import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CommentPayload} from "./comment-payload";

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private httpClient: HttpClient) { }

  getAllCommentsForPost(postId: number): Observable<any> {
    return this.httpClient.get("http://localhost:8080/api/comments/by-post/" + postId);
  }

  postComment(commentPayload: CommentPayload): Observable<any> {
    return this.httpClient.post("http://localhost:8080/api/comments/", commentPayload);
  }

  getAllCommentsByUser(username: string): Observable<any> {
    return this.httpClient.get("http://localhost:8080/api/comments/by-user/" + username);
  }
}
