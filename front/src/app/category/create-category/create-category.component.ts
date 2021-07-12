import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import { CategoryModel } from "../category-model";
import {Router} from "@angular/router";
import {CategoryService} from "../category.service";

@Component({
  selector: 'app-create-category',
  templateUrl: './create-category.component.html',
  styleUrls: ['./create-category.component.css']
})
export class CreateCategoryComponent implements OnInit {

  createCategoryForm: FormGroup;
  categoryModel: CategoryModel;
  title = new FormControl("");
  description = new FormControl("");

  constructor(private router: Router, private categoryService: CategoryService) {
    this.createCategoryForm = new FormGroup({
      title: new FormControl("", Validators.required),
      description: new FormControl("", Validators.required)
    });

    this.categoryModel = {
      name: "",
      description: ""
    }
  }

  ngOnInit(): void {
  }

  discard() {
    this.router.navigateByUrl("/");
  }

  createCategory() {
    this.categoryModel.name = this.createCategoryForm.get("title").value;
    this.categoryModel.description = this.createCategoryForm.get("description").value;
    this.categoryService.createCategory(this.categoryModel).subscribe(data => {
      this.router.navigateByUrl("/list-categories");
    }, error => {
      console.log("Error occured : " + error);
    });
  }
}
