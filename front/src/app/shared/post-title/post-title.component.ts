import { Component, Input, OnInit } from '@angular/core';
import { PostModel } from "../../post/post-model";
import { faComments } from "@fortawesome/free-solid-svg-icons"
import {Router} from "@angular/router";
import {PostService} from "../../post/post.service";

@Component({
  selector: 'app-post-title',
  templateUrl: './post-title.component.html',
  styleUrls: ['./post-title.component.css']
})
export class PostTitleComponent implements OnInit {

  @Input()
  posts: PostModel[];
  faComments = faComments;

  constructor(private postService: PostService, private router: Router) {
    this.postService.getAllPosts().subscribe(post => {
      this.posts = post;
    });
  }

  ngOnInit(): void {
  }

  goToPost(id: number) {
    this.router.navigateByUrl("/view-post/" + id);
  }

}
