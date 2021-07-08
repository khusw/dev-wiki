import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SignupComponent } from './auth/signup/signup.component';
import { LoginComponent } from "./auth/login/login.component";
import { HomeComponent } from "./home/home.component";
import {CreatePostComponent} from "./post/create-post/create-post.component";
import {CreateCategoryComponent} from "./category/create-category/create-category.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'login', component: LoginComponent },
  { path: 'create-post', component: CreatePostComponent },
  { path: 'create-category', component: CreateCategoryComponent }
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
