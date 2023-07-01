package it.uniroma3.siw.siwmovie2.repository;

import it.uniroma3.siw.siwmovie2.model.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long> {

    public List<Movie> findByTitle(String title);

    public boolean existsByTitleAndYear(String title, int year);

    public List<Movie> findByOrderByYearAsc();

}
