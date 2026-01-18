import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ComplaintFormComponent } from './components/complaint-form/complaint-form.component';
import { ComplaintSuccessComponent } from './components/complaint-success/complaint-success.component';
import { ComplaintStatusComponent } from './components/complaint-status/complaint-status.component';

const routes: Routes = [
  { path: '', redirectTo: 'complaint-form', pathMatch: 'full' },
  { path: 'complaint-form', component: ComplaintFormComponent },
  { path: 'success/:protocol', component: ComplaintSuccessComponent },
  { path: 'status/:protocol', component: ComplaintStatusComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientRoutingModule { }