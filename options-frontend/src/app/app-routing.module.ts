import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { OptionInfoComponent } from './option-info/option-info.component'
import { OptionModelComponent } from './option-model/option-model.component';

const routes: Routes = [
  {path: '', redirectTo: '/optionModel', pathMatch: 'full'},
  {path: 'optionModel', component: OptionModelComponent},
  {path: 'optionInfo', component: OptionInfoComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
