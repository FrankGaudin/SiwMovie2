package it.uniroma3.siw.siwmovie2.repository;

import it.uniroma3.siw.siwmovie2.model.Movie;
import it.uniroma3.siw.siwmovie2.model.Review;
import it.uniroma3.siw.siwmovie2.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    public boolean existsByReviewerAndMovie(User reviewer, Movie movie);

    public List<Review> findByOrderByRateAsc();
}