import { Component, OnInit } from '@angular/core';
import {CategoryService} from "../category.service";
import {throwError} from "rxjs";
import {CategoryModel} from "../category-model";

@Component({
  selector: 'app-list-categories',
  templateUrl: './list-categories.component.html',
  styleUrls: ['./list-categories.component.css']
})
export class ListCategoriesComponent implements OnInit {

  categories: Array<CategoryModel> = [];

  constructor(private categoryService: CategoryService) { }

  ngOnInit(): void {
    this.categoryService.getAllCategories().subscribe(data => {
      this.categories = data['data'];
    }, error => {
      throwError(error);
    });
  }

}
