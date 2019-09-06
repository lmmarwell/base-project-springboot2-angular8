import { ForbiddenComponent } from './pages/forbidden/forbidden.component';
import { UsersComponent } from './pages/users/users.component';
import { MainComponent } from './shared/layout/main/main.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { LoginComponent } from './pages/login/login.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProfilesComponent } from './pages/profiles/profiles.component';
import { AuthGuardService as AuthGuard } from './core/guards/auth-guard.service';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'page-not-found', component: PageNotFoundComponent },
  {
    path: 'main', component: MainComponent, children: [
      { path: 'acesso-negado', component: ForbiddenComponent },
      {
        path: 'users', component: UsersComponent, canActivate: [AuthGuard],
        data: {
          expectedRole: 'ROLE_USERS_LIST'
        }
      },
      {
        path: 'profiles', component: ProfilesComponent, canActivate: [AuthGuard],
        data: {
          expectedRole: 'ROLE_PROFILES_LIST'
        }
      }

]
  },
{ path: '**', redirectTo: 'page-not-found' }
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
