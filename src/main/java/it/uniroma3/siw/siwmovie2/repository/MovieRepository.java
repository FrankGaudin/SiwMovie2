package it.uniroma3.siw.siwmovie2.repository;

import it.uniroma3.siw.siwmovie2.model.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long> {

    List<Movie> findByYear(Integer year);

    boolean existsByTitleAndYear(String title, Integer year);

}
