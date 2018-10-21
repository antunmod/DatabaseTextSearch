import {Component, OnInit, ViewChild} from '@angular/core';
import {DateAdapter, MatPaginator, MatSnackBar, MatTableDataSource} from "@angular/material";
import {FormControl} from "@angular/forms";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Analysis} from "../Analysis";

@Component({
  selector: 'app-analysis',
  templateUrl: './analysis.component.html',
  styleUrls: ['./analysis.component.css']
})
export class AnalysisComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private adapter: DateAdapter<any>,
              private snackbar: MatSnackBar,
              private http: HttpClient) {
  }

  selected = 'day';
  resultReturned = false;

  startPicker = new FormControl(new Date());
  endPicker = new FormControl(new Date());

  dataSource;
  columns = [];

  ngOnInit() {
    this.adapter.setLocale('fr');
  }

  private search() {
    var start: Date = this.startPicker.value;
    var end = this.endPicker.value;
    if (end < start) {
      this.snackbar.open("End date has to be after start date!")._dismissAfter(2000);
    }
    else {

      var startDate = start.getDate() + "-" + (start.getMonth() + 1) + "-" + start.getFullYear();
      var endDate = end.getDate() + "-" + (end.getMonth() + 1) + "-" + end.getFullYear();
      const params = new HttpParams().append('start', startDate).append('end', endDate);
      var url = "http://localhost:8080/analysis/";

      url += this.selected == 'day' ? 'day' : 'hour';

      this.http.get(url, {params}).toPromise()
        .then((data: Analysis) => {

          console.log(data);
          this.columns = ['Query'];
          for (var i = 0; i < data.columns.length; ++i) {
            this.columns.push(data.columns[i]);
          }

          var tableData = [];

          const rows = data.rows;
          for (var i = 0; i < rows.length; ++i) {
            var dataMap = new Map();
            const element = rows[i];
            dataMap.set('Query', element[0]);
            console.log(element[0], element[1]);
            for (var j = 0; j < this.columns.length; ++j) {
              dataMap.set(this.columns[j], element[j]);
            }
            tableData.push(dataMap);
          }


          console.log(tableData);

          // const columns = Array<string> (Object.keys(rows[0]));
          // rows =
          this.dataSource = new MatTableDataSource<Object>(tableData);
          // this.dataSource.data = data;
          this.dataSource.paginator = this.paginator;
          this.resultReturned = true;
        });
    }
  }

}
