import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CreatePostPayload} from "./create-post-payload";
import {Router} from "@angular/router";
import {PostService} from "../post.service";
import {CategoryService} from "../../category/category.service";
import {throwError} from "rxjs";

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {

  createPostForm: FormGroup;
  postPayload: CreatePostPayload;
  categories: Array<any>;

  constructor(private router: Router, private postService: PostService, private categoryService: CategoryService) {
    this.postPayload = {
      postName: "",
      url: "",
      description: "",
      categoryName: ""
    }
  }

  ngOnInit(): void {
    this.createPostForm = new FormGroup({
      postName: new FormControl("", Validators.required),
      categoryName: new FormControl("", Validators.required),
      url: new FormControl("", Validators.required),
      description: new FormControl("", Validators.required)
    });

    this.categoryService.getAllCategories().subscribe(data => {
      this.categories = data;
    }, error => {
      throwError(error);
    });
  }

  createPost() {
    this.postPayload.postName = this.createPostForm.get("postName").value;
    this.postPayload.categoryName = this.createPostForm.get("categoryName").value;
    this.postPayload.url = this.createPostForm.get("url").value;
    this.postPayload.description = this.createPostForm.get("description").value;

    this.postService.createPost(this.postPayload).subscribe(data => {
      this.router.navigateByUrl("/");
    }, error => {
      throwError(error);
    });
  }

  discardPost() {
    this.router.navigateByUrl("/");
  }
}
