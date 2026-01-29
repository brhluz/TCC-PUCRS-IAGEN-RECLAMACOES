import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: '/client/complaint-form', pathMatch: 'full' },
  {
    path: 'client',
    loadChildren: () => import('./modules/client/client.module').then(m => m.ClientModule)
  },
  { path: '**', redirectTo: '/client/complaint-form' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }