package hr.fer.nmbp.text_search.service;

import hr.fer.nmbp.text_search.model.Analysis;
import hr.fer.nmbp.text_search.model.Category;
import hr.fer.nmbp.text_search.model.Movie;
import hr.fer.nmbp.text_search.model.MovieDto;
import hr.fer.nmbp.text_search.model.QueryDto;
import hr.fer.nmbp.text_search.model.Search;
import hr.fer.nmbp.text_search.repository.CategoryRepository;
import hr.fer.nmbp.text_search.repository.MovieRepository;
import hr.fer.nmbp.text_search.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MovieService {

    private MovieRepository movieRepository;

    private final CategoryRepository categoryRepository;

    private final SearchRepository searchRepository;

    private final EntityManager entityManager;

    @Autowired
    public MovieService(MovieRepository movieRepository, CategoryRepository categoryRepository, EntityManager entityManager, SearchRepository searchRepository) {
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
        this.entityManager = entityManager;
        this.searchRepository = searchRepository;
    }


    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public boolean add(MovieDto movieDto) {
        Set<Category> categories = getCategories(movieDto.getCategories());
        Movie movie = new Movie(movieDto.getTitle(), categories, movieDto.getSummary(), movieDto.getDescription());

        while (true) {
            try {
                movieRepository.save(movie);
                break;
            } catch (Exception ex) {
                System.out.println("There was and SQL error, probably with id");
            }
        }
        return true;
    }

    private Set<Category> getCategories(String categoriesString) {
        String[] parts = categoriesString.split(",");
        Set<Category> categories = new HashSet<>();

        for (String category : parts) {
            category = capitalizeString(category);
            if (categoryRepository.existsByName(category)) {
                categories.add(categoryRepository.findByName(category));
            } else {
                categories.add(new Category(category));
            }
        }

        return categories;
    }

    private String capitalizeString(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return name.trim();
    }

    public List<QueryDto> searchMovie(String query) {
        List movies = entityManager.createNativeQuery(query).getResultList();
        QueryDto[] movieArray = new QueryDto[movies.size()];
        List<QueryDto> moviesList = new ArrayList<>();
        for (int i = 0; i < movies.size(); ++i) {
            Object[] params = (Object[]) movies.get(i);
            movieArray[i] = new QueryDto((String) params[0], (Float) params[1]);
            moviesList.add(movieArray[i]);
        }
        return moviesList;
    }

    public void addTestSearch() {
        Search search = new Search("SELECT * FROM movie", new Date(System.currentTimeMillis()));
        searchRepository.save(search);
    }

    public void addSearch(String query) {
        Search search = new Search(query, new Date(System.currentTimeMillis()));
        searchRepository.save(search);
    }

    public List<Search> getSearches() {
        return searchRepository.findAll();
    }

    public Analysis getAnalysisByDay(String start, String end) {
        String query = createDayAnalysisQuery(start, end);
        List analysisByDay = entityManager.createNativeQuery(query).getResultList();

        List<String> columns = getDates(start, end);
        Analysis analysis = new Analysis(columns, analysisByDay);
        return analysis;
    }

    public Analysis getAnalysisByHour(String start, String end) {
        String query = createHourAnalysisQuery(start, end);
        List analysisByHour = entityManager.createNativeQuery(query).getResultList();

        List<String> columns = getHours();
        Analysis analysis = new Analysis(columns, analysisByHour);
        return analysis;
    }

    private String createHourAnalysisQuery(String start, String end) {
        StringBuilder builder = new StringBuilder();
        builder.append("select * from crosstab('SELECT query, EXTRACT(HOUR from executed) hourExecuted, COUNT(*) FROM search \n")
                .append("where executed::DATE >= TO_DATE(''" + start + "'', ''dd-MM-yyyy'')" + "and executed::DATE <= TO_DATE(''" + end + "'', ''dd-MM-yyyy'') ")
                .append("group by query, hourExecuted', \n")
                .append("'select generate_series(0,23)') \n")
                .append("AS pivotTable (query VARCHAR(40), s00 INT, s01 INT, s02 INT, s03 INT, s04 INT, s05 INT, s06 INT, s07 INT, s08 INT, \n")
                .append("s09 INT, s10 INT, s11 INT, s12 INT, s13 INT, s14 INT, s15 INT, s16 INT, s17 INT, s18 INT, s19 INT, s20 INT, s21 INT, s22 INT, s23 INT )\n")
                .append("ORDER BY query");
        return builder.toString();
    }

    private String createDayAnalysisQuery(String start, String end) {
        List<String> dates = getDates(start, end);
        String query = "select * from crosstab('SELECT query, CAST(executed AS DATE) dayExecuted, COUNT(*) FROM search \n";
        query += "where executed::DATE >= TO_DATE(''" + start + "'', ''dd-MM-yyyy'')" + "and executed::DATE <= TO_DATE(''" + end + "'', ''dd-MM-yyyy'') \n";
        query += "group by query, dayExecuted', \n";
        query += "$$ values " + getDateValues(dates) + " $$) \n";
        query += "AS pivotTable (query VARCHAR(40), " + getDatePivots(dates) + ") \n";
        query += "ORDER BY query";

        return query;
    }

    private List<String> getDates(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        List<String> dates = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        try {
            startCalendar.setTime(sdf.parse(start));
            endCalendar.setTime(sdf.parse(end));
        } catch (ParseException e) {
            return null;
        }


        while (startCalendar.before(endCalendar)) {
            dates.add(sdf.format(startCalendar.getTime()));
            startCalendar.add(Calendar.DATE, 1);
        }

        dates.add(sdf.format(endCalendar.getTime()));

        return dates;
    }

    private String getDatePivots(List<String> dates) {
        String datePivots = "";
        for (String date : dates) {
            date = date.substring(0, 2) + date.substring(3, 5) + date.substring(6, 10);
            datePivots += "d" + date + " INT, ";
        }

        return datePivots.substring(0, datePivots.length() - 2);
    }

    private String getDateValues(List<String> dates) {
        String dateValues = "";
        for (String date : dates) {
            date = date.substring(6, 10) + "-" + date.substring(3, 5) + "-" + date.substring(0, 2);
            dateValues += "('" + date + "'), ";
        }

        return dateValues.substring(0, dateValues.length() - 2);
    }

    private List<String> getHours() {
        return Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
    }
}
