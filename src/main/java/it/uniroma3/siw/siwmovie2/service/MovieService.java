package it.uniroma3.siw.siwmovie2.service;

import it.uniroma3.siw.siwmovie2.model.Artist;
import it.uniroma3.siw.siwmovie2.model.Movie;
import it.uniroma3.siw.siwmovie2.model.Review;
import it.uniroma3.siw.siwmovie2.repository.ArtistRepository;
import it.uniroma3.siw.siwmovie2.repository.MovieRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ReviewService reviewService;

    @Transactional
    public void createNewMovie(@Valid Movie movie, @NotNull MultipartFile image) throws IOException {
        byte[] photoBytes = image.getBytes();
        movie.setImage(photoBytes);
        this.movieRepository.save(movie);
    }


    public void updateMovie(Long id, String title, Integer year, MultipartFile image) throws IOException{
        Movie movie = movieRepository.findById(id).get();
        if(title != null) {
            movie.setTitle(title);
        }
        if(year!= null && year >1888 && year <2023) {
            movie.setYear(year);
        }
        if (image != null && !image.isEmpty()) {
            byte[] photoBytes = image.getBytes();
            movie.setImage(photoBytes);
        }

        movieRepository.save(movie);
    }


    public Movie getMovieById(Long id) {
        return this.movieRepository.findById(id).get();
    }

    public Iterable<Movie> getMovies() {
        return this.movieRepository.findAll();
    }

    public List<Movie> getAllMoviesByAsc() {
        return this.movieRepository.findByOrderByYearAsc();
    }

    public List<Movie> getMoviesByTitle(String title) {
        return this.movieRepository.findByTitle(title);
    }

    @Transactional
    public Movie setDirectorToMovie(Long directorId, Long movieId) {
        Artist director = this.artistRepository.findById(directorId).get();
        Movie movie = this.movieRepository.findById(movieId).get();
        movie.setDirector(director);
        this.movieRepository.save(movie);
        return movie;
    }

    @Transactional
    public Movie addActorToMovie(Long movieId, Long actorId) {
        Movie movie = this.movieRepository.findById(movieId).get();
        Artist actor = this.artistRepository.findById(actorId).get();
        Set<Artist> actors = movie.getActors();
        Set<Movie> starredMovies = actor.getStarredMovies();
        actors.add(actor);
        starredMovies.add(movie);
        this.movieRepository.save(movie);
        return movie;
    }

    @Transactional
    public Movie removeActorFromMovie(Long movieId, Long actorId) {
        Movie movie = this.getMovieById(movieId);
        Artist actor = this.artistService.getActorById(actorId);
        Set<Artist> actors = movie.getActors();
        actors.remove(actor);
        this.movieRepository.save(movie);
        return movie;
    }

    public List<Artist> findActorsNotInMovie(Long movieId) {
        List<Artist> actorsToAdd = new ArrayList<>();

        for (Artist a : artistService.findActorsNotInMovie(movieId)) {
            actorsToAdd.add(a);
        }
        return actorsToAdd;
    }

    @Transactional
    public void deleteMovie(Long movieId) {
        Movie movie = this.getMovieById(movieId);
        Set<Artist> actors = movie.getActors();
        for (Artist actor : actors) {
            actor.getActorOf().remove(movie);
        }
        List<Review> reviews = movie.getReviews();
        for (Review review : reviews) {
            review.setMovie(null);
        }
        this.movieRepository.delete(movie);
    }

    @Transactional
    public Movie addReviewToMovie(Long movieId, Long reviewId) {
        Movie movie = this.getMovieById(movieId);
        Review review = this.reviewService.getReviewById(reviewId);
        List<Review> reviews = movie.getReviews();
        reviews.add(review);
        movie.setReviews(reviews);
        review.setMovie(movie);
        return this.movieRepository.save(movie);
    }


}