import { Component, OnInit } from '@angular/core';
import {CategoryService} from "../../category/category.service";
import {CategoryModel} from "../../category/category-model";

@Component({
  selector: 'app-category-side-bar',
  templateUrl: './category-side-bar.component.html',
  styleUrls: ['./category-side-bar.component.css']
})
export class CategorySideBarComponent implements OnInit {

  categories: Array<CategoryModel> = [];
  displayViewAll: boolean;

  constructor(private categoryService: CategoryService) {
    this.categoryService.getAllCategories().subscribe(data => {
      if (data['data'].length > 3) {
        this.categories = data['data'].splice(0, 3);
        this.displayViewAll = true;
      } else {
        this.categories = data['data'];
      }
    });
  }

  ngOnInit(): void {
  }

}
