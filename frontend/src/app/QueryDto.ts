export class QueryDto {

  private _title: string;
  private _rank: number;

  constructor(title: string, rank: number) {
    this._title = title;
    this._rank = rank;
  }


  get title(): string {
    return this._title;
  }

  get rank(): number {
    return this._rank;
  }
}
