package it.uniroma3.siw.siwmovie2.controller;

import it.uniroma3.siw.siwmovie2.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;


}
