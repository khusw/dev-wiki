import { Component, OnInit } from '@angular/core';
import { PostModel } from "../post/post-model";
import { PostService } from "../post/post.service";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  posts: Array<PostModel> = [];

  constructor(private postService: PostService) {
    this.postService.getAllPosts().subscribe(post => this.posts = post['data'] );
  }

  ngOnInit(): void {
  }

}
