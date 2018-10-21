import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatPaginator, MatSnackBar, MatTableDataSource} from "@angular/material";
import {HttpClient, HttpClientModule, HttpParams} from "@angular/common/http";
import {QueryDto} from "../QueryDto";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})

export class SearchComponent implements OnInit {


  searchForm: FormGroup;
  numberOfDocuments: number;
  searched = false;
  tmpSearchString: string;

  operator: string = "&";
  data: QueryDto[] = [];
  displayedColumns: string[] = ['title', 'rank'];


  private ELEMENT_DATA: QueryDto[] = [];

  dataSource = new MatTableDataSource(this.ELEMENT_DATA);


  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private builder: FormBuilder,
              private snackbar: MatSnackBar,
              private http: HttpClient) {
  }

  ngOnInit() {
    this.searchForm = this.builder.group({
      text: ['', [Validators.required]],
      operator: [''],
      query: ['']
    });
    this.dataSource.paginator = this.paginator;
  }

  changeOperator() {
    this.operator = this.operator == "&" ? "|" : "&";
  }

  onSubmit() {
    var text = this.searchForm.get('text').value;

    if (!this.correctNumberOfApostrophes(text)) {
      this.snackbar.open("There has to be and even number of apostrophes in text!")._dismissAfter(2000);
      return;
    }

    var parts = text.split(" ");
    var strings: string[] = this.extractStrings(parts);
    var query: string = this.generateQuery(strings);
    this.searchForm.get('query').setValue(query);

    this.getQueryResults(query, text);
  }

  private correctNumberOfApostrophes(text: string): boolean {
    var count = 0;
    for (var i = 0; i < text.length; ++i) {
      if (text.charAt(i) == '"') {
        count++;
      }
    }
    return count % 2 == 0;
  }

  private extractStrings(parts: string[]): string[] {
    var strings: string[] = [];
    var tmp: string = "";

    var i = 0;
    var number = 0;

    while (i < parts.length) {
      if (!parts[i].startsWith("\"")) {
        strings[number++] = parts[i++];
        continue;
      }

      if (parts[i].endsWith("\"")) {
        strings[number++] = parts[0].substring(1, parts[0].length - 1);
        i++;
        continue;
      }

      tmp += parts[i++].substring(1);
      tmp += " & ";

      while (!parts[i].endsWith("\"")) {
        tmp += parts[i++];
        tmp += " & ";
      }

      tmp += parts[i].substring(0, parts[i].length - 1);
      i++;
      strings[number++] = tmp;
      tmp = "";
    }

    return strings;
  }

  private generateQuery(strings: string[]): string {
    this.tmpSearchString = this.generateSearchString(strings);
    var query: string = "SELECT ts_headline(title, to_tsquery('english', '"+ this.tmpSearchString + "')) title,\n";
    query += "\tts_rank(movieTSV, to_tsquery('english', '" + this.tmpSearchString + "')) rank\n";
    query += "\tFROM movie JOIN movieTSV on movie.id = movieTSV.id\n";
    query += "\tWHERE\n";

    for (var i = 0; i < strings.length; ++i) {
      query += "\tmovieTSV @@ to_tsquery('english', '" + strings[i] + "') ";

      if (i != strings.length - 1) {
        query += this.operator == "&" ? "AND" : "OR";
      }

      query += "\n";
    }
    query += "\tORDER BY rank DESC";
    return query;
  }

  private generateSearchString(strings: string[]): string {
    var search = "";
    for (var i = 0; i < strings.length; ++i) {
      if (strings[i].indexOf("&") >= 0) {
        search += "(" + strings[i] + ")";
      } else {
        search += strings[i];
      }

      if (i != strings.length - 1) {
        search += " " + this.operator + " "
      }

    }
    return search;
  }

  private getQueryResults(query: string, search: string) {
    const params = new HttpParams().append('query', query);
    this.http.get("http://localhost:8080/search", {params}).toPromise()
      .then((result: QueryDto[]) => {
        this.numberOfDocuments = result.length;
        this.dataSource.data = result;
        this.searched = true;
        this.saveSearch(this.tmpSearchString);
      })
  }

  private saveSearch(search: string) {
    const params = new HttpParams().append('search', search);
    this.http.get("http://localhost:8080/addSearch", {params}).toPromise()
      .then((()=> {}));
  }
}
