import { NgModule } from '@angular/core';
import { Router, RouterModule, Routes } from '@angular/router';
import { AboutComponent } from './about/about.component';
import { HomeComponent } from './home/home.component';

/**
 * Defines the routes for the application.
 *
 */
const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full' },
  {path: "home", component: HomeComponent},
  {path: "about", component: AboutComponent},
  {path: '**', redirectTo: '/home', pathMatch: 'full'},
]

/**
 * AppRoutingModule is responsible for defining and managing the routing configuration
 * for the Angular application. It sets up the navigation and URL paths for the application.
 *
 */
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
