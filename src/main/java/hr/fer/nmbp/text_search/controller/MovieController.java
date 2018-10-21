package hr.fer.nmbp.text_search.controller;

import hr.fer.nmbp.text_search.model.Analysis;
import hr.fer.nmbp.text_search.model.Movie;
import hr.fer.nmbp.text_search.model.MovieDto;
import hr.fer.nmbp.text_search.model.QueryDto;
import hr.fer.nmbp.text_search.model.Search;
import hr.fer.nmbp.text_search.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class MovieController {

    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }


    @GetMapping("all")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping()
    public boolean addMovie(@RequestBody MovieDto movie) {
        return movieService.add(movie);
    }

    @GetMapping("search")
    public List<QueryDto> searchMovies(@RequestParam(name = "query") String query) {
        return movieService.searchMovie(query);
    }

    @GetMapping("addSearch")
    public void addTestSearch(@RequestParam(name = "search") String search) {
        movieService.addSearch(search);
    }

    @GetMapping("searches")
    public List<Search> getSearches() {
        return movieService.getSearches();
    }

    @GetMapping("analysis/day")
    public Analysis getAnalysisByDay(@RequestParam String start, @RequestParam String end) {
        return movieService.getAnalysisByDay(start, end);
    }

    @GetMapping("analysis/hour")
    public Analysis getAnalysisByHour(@RequestParam String start, @RequestParam String end) {
        return movieService.getAnalysisByHour(start, end);
    }

}
