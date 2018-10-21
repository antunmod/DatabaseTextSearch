export class Movie {
  
  constructor(title: string, categories: string, summary: string, description: string) {
    this.title = title;
    this.categories = categories;
    this.summary = summary;
    this.description = description;
  }

  title: string;
  categories: string;
  summary: string;
  description: string;
}
