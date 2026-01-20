import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ClientRoutingModule } from './client-routing.module';
import { ComplaintFormComponent } from './components/complaint-form/complaint-form.component';
import { ComplaintSuccessComponent } from './components/complaint-success/complaint-success.component';
import { ComplaintStatusComponent } from './components/complaint-status/complaint-status.component';

@NgModule({
  declarations: [
    ComplaintFormComponent,
    ComplaintSuccessComponent,
    ComplaintStatusComponent
  ],
  imports: [
    CommonModule,
    ClientRoutingModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class ClientModule { }