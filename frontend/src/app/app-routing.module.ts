import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {AddComponent} from "./add/add.component";
import {SearchComponent} from "./search/search.component";
import {AnalysisComponent} from "./analysis/analysis.component";
import {IndexComponent} from "./index/index.component";

const  routes: Routes = [
  {path: '', component: IndexComponent},
  {path: 'add', component: AddComponent},
  {path: 'search', component: SearchComponent},
  {path: 'analysis', component: AnalysisComponent}
];

@NgModule({
  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)]
})

export class AppRoutingModule {
}
