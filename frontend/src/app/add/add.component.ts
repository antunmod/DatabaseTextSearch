import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Movie} from "../movie";
import {HttpClient} from "@angular/common/http";
import {MatSnackBar} from "@angular/material";

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddComponent implements OnInit {

  movieForm : FormGroup;
  movie: Movie;
  constructor(public builder: FormBuilder,
              private http: HttpClient,
              private snackbar: MatSnackBar) { }

  ngOnInit() {
    this.movieForm = this.builder.group({
      title: ['', [Validators.required]],
      categories: ['', [Validators.required]],
      summary: ['', [Validators.required]],
      description: ['', [Validators.required]]
    });
  }

  onSubmit() {
    let movie = new Movie(this.movieForm.get('title').value, this.movieForm.get('categories').value, this.movieForm.get('summary').value, this.movieForm.get('description').value);
    this.http.post("http://localhost:8080", movie).toPromise()
      .then( (response) => {
        if (response == true) {
          this.snackbar.open("Movie added")._dismissAfter(2000);
        }
        }
      )

  }
}
