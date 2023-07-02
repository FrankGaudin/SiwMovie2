package it.uniroma3.siw.siwmovie2.service;

import it.uniroma3.siw.siwmovie2.model.Credentials;
import it.uniroma3.siw.siwmovie2.model.Movie;
import it.uniroma3.siw.siwmovie2.model.Review;
import it.uniroma3.siw.siwmovie2.model.User;
import it.uniroma3.siw.siwmovie2.repository.MovieRepository;
import it.uniroma3.siw.siwmovie2.repository.ReviewRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CredentialsService credentialsService;

    @Transactional
    public void addReview(Review review) {
        this.reviewRepository.save(review);
    }

    @Transactional
    public Review getReviewById(Long id) {
        return this.reviewRepository.findById(id).get();
    }

    @Transactional
    public List<Review> getReviewsByRateAsc() {
        return this.reviewRepository.findByOrderByRateAsc();
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = this.getReviewById(reviewId);
        Movie movie = review.getMovie();
        review.getReviewer().getReviews().remove(review);
        review.getMovie().getReviews().remove(review);
        this.movieRepository.save(movie);
        this.reviewRepository.delete(review);
    }

    public Review saveReviewToUser(Long userId, Long reviewId) {
        Review review = this.getReviewById(reviewId);
        User user = this.userService.getUser(userId);
        List<Review> reviews = user.getReviews();
        reviews.add(review);
        user.setReviews(reviews);
        review.setReviewer(user);
        return this.reviewRepository.save(review);
    }

    @Transactional
    public Review newReview(@Valid Review review, Long movieId) {
        Movie movie = this.movieRepository.findById(movieId).get();
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(user.getUsername());
        if(!this.reviewRepository.existsByReviewerAndMovie(credentials.getUser(), movie)) {
            review.setMovie(movie);
            review.setReviewer(credentials.getUser());
            this.reviewRepository.save(review);

            movie.getReviews().add(review);

            this.movieRepository.save(movie);
            return review;
        } else {
            return null;
        }

    }

}
