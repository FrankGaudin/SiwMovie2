package it.uniroma3.siw.siwmovie2.model;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Movie movieReviewed;


}
