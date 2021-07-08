import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CategoryModel} from "./category-model";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private httpClient: HttpClient) { }

  getAllCategories(): Observable<Array<CategoryModel>> {
    return this.httpClient.get<Array<CategoryModel>>("http://localhost:8080/api/categories");
  }
}
