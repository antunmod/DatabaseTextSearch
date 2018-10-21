export class Analysis {

  private _columns: string[];
  private _rows: Object[][];


  constructor(dates: string[], rows: Object[][]) {
    this._columns = dates;
    this._rows = rows;
  }


  get columns(): string[] {
    return this._columns;
  }

  get rows(): Object[][] {
    return this._rows;
  }
}
