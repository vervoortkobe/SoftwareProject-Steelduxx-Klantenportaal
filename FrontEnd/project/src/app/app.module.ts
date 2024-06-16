import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatPaginatorModule } from '@angular/material/paginator';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import {
  HTTP_INTERCEPTORS,
  HttpClientModule,
  HttpClientXsrfModule,
} from '@angular/common/http';
import { LocationComponent } from './location/location.component';
import { OrderpageComponent } from './orderpage/orderpage.component';
import { UploadComponent } from './upload/upload.component';
import { AppRoutingModule } from './app-routing.module';
import { DocumentsComponent } from './documents/documents.component';
import { FileformatterPipe } from './pipes/fileformatter.pipe';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegisterComponent } from './register/register.component';
import { NavbarComponent } from './navbar/navbar.component';
import { NavbarRoutingLayoutComponent } from './navbar-routing-layout/navbar-routing-layout.component';
import { OrderdetailsComponent } from './orderdetails/orderdetails.component';
import { OrderrequestsComponent } from './orderrequests/orderrequests.component';
import { NewrequestComponent } from './newrequest/newrequest.component';
import { CustomInterceptor } from './utils/HttpInterceptor';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { OrderrequestdetailsComponent } from './orderrequestdetails/orderrequestdetails.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { AdminpageComponent } from './adminpage/adminpage.component';
import { AdmincompanycodesComponent } from './admincompanycodes/admincompanycodes.component';
import { AccountRequestsComponent } from './account-requests/account-requests.component';
import { HistoryComponent } from './history/history.component';
import { HistorypageadminComponent } from './historypageadmin/historypageadmin.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LocationComponent,
    OrderpageComponent,
    UploadComponent,
    DocumentsComponent,
    FileformatterPipe,
    RegisterComponent,
    NavbarComponent,
    NavbarRoutingLayoutComponent,
    OrderdetailsComponent,
    OrderrequestsComponent,
    NewrequestComponent,
    ForgotPasswordComponent,
    OrderrequestdetailsComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent,
    AdminpageComponent,
    AccountRequestsComponent,
    AdmincompanycodesComponent,
    HistoryComponent,
    HistorypageadminComponent,
  ],
  imports: [
    MatPaginatorModule,
    HttpClientModule,
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientXsrfModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CustomInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
