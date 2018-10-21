package hr.fer.nmbp.text_search.repository;

import hr.fer.nmbp.text_search.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);
    Category findByName(String name);
}
