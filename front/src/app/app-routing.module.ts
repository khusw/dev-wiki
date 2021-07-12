import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SignupComponent } from './auth/signup/signup.component';
import { LoginComponent } from "./auth/login/login.component";
import { HomeComponent } from "./home/home.component";
import { CreatePostComponent } from "./post/create-post/create-post.component";
import { CreateCategoryComponent } from "./category/create-category/create-category.component";
import { ListCategoriesComponent } from "./category/list-categories/list-categories.component";
import { ViewPostComponent } from "./post/view-post/view-post.component";
import {UserProfileComponent} from "./auth/user-profile/user-profile.component";
import {AuthGuard} from "./auth/auth.guard";

const routes: Routes = [
  { path: "", component: HomeComponent },
  { path: "signup", component: SignupComponent },
  { path: "login", component: LoginComponent },
  { path: "create-post", component: CreatePostComponent, canActivate: [AuthGuard] },
  { path: "create-category", component: CreateCategoryComponent, canActivate: [AuthGuard] },
  { path: "list-categories", component: ListCategoriesComponent },
  { path: "view-post/:id", component: ViewPostComponent },
  { path: "user-profile/:name", component: UserProfileComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
