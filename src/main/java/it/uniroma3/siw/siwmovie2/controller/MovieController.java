package it.uniroma3.siw.siwmovie2.controller;

import it.uniroma3.siw.siwmovie2.controller.validator.MovieValidator;
import it.uniroma3.siw.siwmovie2.model.*;
import it.uniroma3.siw.siwmovie2.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private MovieValidator movieValidator;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;


    @GetMapping(value="/admin/formNewMovie")
    public String formNewMovie(Model model) {
        model.addAttribute("movie", new Movie());
        return "admin/formNewMovie.html";
    }

    @GetMapping(value="/admin/formUpdateMovie/{id}")
    public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
        model.addAttribute("movie", movieRepository.findById(id).get());
        return "admin/formUpdateMovie.html";
    }

    @GetMapping(value="/admin/indexMovie")
    public String indexMovie() {
        return "admin/indexMovie.html";
    }

    @GetMapping(value="/admin/manageMovies")
    public String manageMovies(Model model) {
        model.addAttribute("movies", this.movieRepository.findAll());
        return "admin/manageMovies.html";
    }

    @GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
    public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {

        Artist director = this.artistRepository.findById(directorId).get();
        Movie movie = this.movieRepository.findById(movieId).get();
        movie.setDirector(director);
        this.movieRepository.save(movie);

        model.addAttribute("movie", movie);
        return "admin/formUpdateMovie.html";
    }


    @GetMapping(value="/admin/addDirector/{id}")
    public String addDirector(@PathVariable("id") Long id, Model model) {
        model.addAttribute("artists", artistRepository.findAll());
        model.addAttribute("movie", movieRepository.findById(id).get());
        return "admin/directorsToAdd.html";
    }

    @PostMapping("/admin/movie")
    public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model) {

        this.movieValidator.validate(movie, bindingResult);
        if (!bindingResult.hasErrors()) {
            this.movieRepository.save(movie);
            model.addAttribute("movie", movie);
            return "movie.html";
        } else {
            return "admin/formNewMovie.html";
        }
    }

    @GetMapping("/movie/{id}")
    public String getMovie(@PathVariable("id") Long id, Model model) {
        model.addAttribute("movie", this.movieRepository.findById(id).get());
        return "movie.html";
    }

    @GetMapping("/movie")
    public String getMovies(Model model) {
        model.addAttribute("movies", this.movieRepository.findAll());
        return "movies.html";
    }

    @GetMapping("/formSearchMovies")
    public String formSearchMovies() {
        return "formSearchMovies.html";
    }

    @PostMapping("/searchMovies")
    public String searchMovies(Model model, @RequestParam int year) {
        model.addAttribute("movies", this.movieRepository.findByYear(year));
        return "foundMovies.html";
    }

    @GetMapping("/admin/updateActors/{id}")
    public String updateActors(@PathVariable("id") Long id, Model model) {
        List<Artist> actorsToAdd = this.actorsToAdd(id);
        model.addAttribute("actorsToAdd", actorsToAdd);
        model.addAttribute("movie", this.movieRepository.findById(id).get());

        return "admin/actorsToAdd.html";
    }

    @GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
    public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
        Movie movie = this.movieRepository.findById(movieId).get();
        Artist actor = this.artistRepository.findById(actorId).get();
        Set<Artist> actors = movie.getActors();
        actors.add(actor);
        this.movieRepository.save(movie);

        List<Artist> actorsToAdd = actorsToAdd(movieId);

        model.addAttribute("movie", movie);
        model.addAttribute("actorsToAdd", actorsToAdd);

        return "admin/actorsToAdd.html";
    }

    @GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
    public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
        Movie movie = this.movieRepository.findById(movieId).get();
        Artist actor = this.artistRepository.findById(actorId).get();
        Set<Artist> actors = movie.getActors();
        actors.remove(actor);
        this.movieRepository.save(movie);

        List<Artist> actorsToAdd = actorsToAdd(movieId);

        model.addAttribute("movie", movie);
        model.addAttribute("actorsToAdd", actorsToAdd);

        return "admin/actorsToAdd.html";
    }

    private List<Artist> actorsToAdd(Long movieId) {
        List<Artist> actorsToAdd = new ArrayList<>();

        for (Artist a : artistRepository.findActorsNotInMovie(movieId)) {
            actorsToAdd.add(a);
        }
        return actorsToAdd;
    }




   /* @GetMapping(value="/user/addReviewToMovie/{reviewId}/{movieId}")
    public String addReviewToMovie(@PathVariable("reviewId") Long reviewId, @PathVariable("movieId") Long movieId, Model model) {
        Movie movie = this.movieRepository.findById(movieId).get();
        Review review = this.reviewRepository.findById(reviewId).get();
        List<Review> reviews = movie.getReviews();
        if(!reviewRepository.existsByUserAndMovieReviewed(review.getUser(), movie)) {
            reviews.add(review);
            review.setMovieReviewed(movie);
            this.movieRepository.save(movie);

            model.addAttribute("movie", movie);
            model.addAttribute("reviewsToAdd", reviews);
        }
        return "user/reviewToAdd.html";
    }*/

    @GetMapping(value="/admin/removeReviewFromMovie/{reviewId}/{movieId}")
    public String removeReviewFromMovie(@PathVariable("reviewId") Long reviewId, @PathVariable("movieId") Long movieId, Model model) {
        Movie movie = this.movieRepository.findById(movieId).get();
        Review review = this.reviewRepository.findById(reviewId).get();
        List<Review> reviews = movie.getReviews();
        reviews.remove(review);
        this.movieRepository.save(movie);

        model.addAttribute("movie", movie);
        model.addAttribute("actorsToAdd", reviews);

        return "user/reviewToAdd.html";
    }



   /* @GetMapping(value="/user/formNewReview/{id}")
    public String formNewReview(@PathVariable("id") Long id,Model model) {
        model.addAttribute("movie", this.movieRepository.findById(id).get());
        model.addAttribute("review", new Review());
        return "user/formNewReview.html";
    }*/

    @GetMapping(value="/user/indexReview")
    public String indexReview() {
        return "user/indexReview.html";
    }

    /*@PostMapping("/user/review/{id}/{id1}")
    public String newReview(@ModelAttribute("review") Review review,@PathVariable("id") Long movieId,@PathVariable("id1") Long userId, Model model) {
        Movie movie = this.movieRepository.findById(movieId).get();
        List<Review> reviews = movie.getReviews();
        if(!reviewRepository.existsByUserAndMovieReviewed(review.getUser(), movie)) {
            this.reviewRepository.save(review);
            review.setMovieReviewed(movie);
            reviews.add(review);
            this.movieRepository.save(movie);

            model.addAttribute("movie", movie);
            return "movie";
        } else {
            model.addAttribute("messaggioErrore", "Ha già recensito questo film");
            return "user/formNewReview.html";
        }
    }*/

    @GetMapping(value="/user/formNewReview/{id}")
    public String formNewReview(@PathVariable("id") Long id, Model model) {
        Movie movie = movieRepository.findById(id).orElse(null);
        model.addAttribute("movie", movie);
        model.addAttribute("review", new Review());
        return "user/formNewReview.html";
    }

    @PostMapping("/user/review/{id}")
    public String newReview(@ModelAttribute("review") Review review, @PathVariable("id") Long movieId, Model model, Principal principal) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        User user = credentialsRepository.findByUsername(principal.getName()).orElse(null).getUser();

        if (user != null) {
            if (!reviewRepository.existsByUserAndMovieReviewed(user, movie)) {
                review.setMovieReviewed(movie);
                review.setUser(user);
                reviewRepository.save(review);

                movie.getReviews().add(review);
                movieRepository.save(movie);

                model.addAttribute("movie", movie);
                return "movies.html";
            } else {
                model.addAttribute("messaggioErrore", "Hai già recensito questo film");
                return "user/formNewReview.html";
            }
        } else {
            // L'utente non è autenticato, gestisci di conseguenza
            return "login.html";
        }
    }

    @GetMapping("/review/{id}")
    public String getReview(@PathVariable("id") Long id, Model model) {
        model.addAttribute("review", this.reviewRepository.findById(id).get());
        return "review.html";
    }

    @GetMapping("/reviews")
    public String getReviews(Movie movie, Model model) {
        model.addAttribute("reviews", this.reviewRepository.findAllByMovieReviewed(movie));
        return "movie.html";
    }

}