package hr.fer.nmbp.text_search.repository;

import hr.fer.nmbp.text_search.model.Search;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, Long> {
}
