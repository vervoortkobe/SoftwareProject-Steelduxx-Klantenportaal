import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UploadComponent } from './upload/upload.component';
import { RouterModule, Routes } from '@angular/router';
import { DocumentsComponent } from './documents/documents.component';
import { LocationComponent } from './location/location.component';
import { LoginComponent } from './login/login.component';
import { OrderpageComponent } from './orderpage/orderpage.component';
import { RegisterComponent } from './register/register.component';
import { NavbarComponent } from './navbar/navbar.component';
import { NavbarRoutingLayoutComponent } from './navbar-routing-layout/navbar-routing-layout.component';
import { OrderdetailsComponent } from './orderdetails/orderdetails.component';
import { NewrequestComponent } from './newrequest/newrequest.component';
import { OrderrequestsComponent } from './orderrequests/orderrequests.component';
import { AuthGuardService } from './services/auth-guard.service';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { OrderrequestdetailsComponent } from './orderrequestdetails/orderrequestdetails.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { AdminpageComponent } from './adminpage/adminpage.component';
import { AdmincompanycodesComponent } from './admincompanycodes/admincompanycodes.component';
import { AccountRequestsComponent } from './account-requests/account-requests.component';
import { Role } from './enums/role';
import { HistoryComponent } from './history/history.component';
import { HistorypageadminComponent } from './historypageadmin/historypageadmin.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'forgot', component: ForgotPasswordComponent },
  { path: 'reset/:token', component: ResetPasswordComponent },
  {
    path: '',
    canActivate: [AuthGuardService],
    component: NavbarRoutingLayoutComponent,
    children: [
      { path: 'upload', component: UploadComponent },
      { path: 'documents', component: DocumentsComponent },
      { path: 'location', component: LocationComponent },
      { path: 'home', component: OrderpageComponent },
      { path: 'navbar', component: NavbarComponent },
      { path: 'orders', redirectTo: 'home', pathMatch: 'full' },
      { path: 'orders/:id', component: OrderdetailsComponent },
      { path: 'orders/:key/:id', component: OrderdetailsComponent },
      { path: 'new', component: NewrequestComponent },
      { path: 'adminorders', component: AdminpageComponent },
      { path: 'history', component: HistoryComponent },
      { path: 'historyadmin', component: HistorypageadminComponent },
      { path: 'admincodes', component: AdmincompanycodesComponent },
      { path: 'requests', component: OrderrequestsComponent },
      { path: 'requests/:id', component: OrderrequestdetailsComponent },
      { path: 'account-requests', component: AccountRequestsComponent },

      //LEAVE THIS AS LAST!
      {
        path: '**',
        children: [],
        data: { role: Role.CUSTOMER, success: 'home', fail: 'adminorders' },
        canActivate: [AuthGuardService],
      },
    ],
  },

  //LEAVE THIS AS LAST!
  { path: '**', redirectTo: 'login' },
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes), CommonModule],
  exports: [RouterModule],
})
export class AppRoutingModule {}
