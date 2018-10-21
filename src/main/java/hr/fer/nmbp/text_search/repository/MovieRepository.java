package hr.fer.nmbp.text_search.repository;

import hr.fer.nmbp.text_search.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

//    @Quer(value = "SELECT ts_headline(title, to_tsquery('english', '%?1%')), " +
//            "   ts_headline(description, to_tsquery('english', '%?1%')), " +
//            "   description, " +
//            "   ts_rank(movieTSV, to_tsquery('english', '%?1%')) rank " +
//            "   FROM movie JOIN movieTSV ON movie.id = movieTSV.id " +
//            "   WHERE " +
//            "   ", nativeQuery = true)
//    List<String> findByQuery(String query);
}
