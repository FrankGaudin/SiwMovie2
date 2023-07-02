package it.uniroma3.siw.siwmovie2.model;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.*;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    @Min(1900)
    @Max(2023)
    private Integer year;

    @Nullable
    private byte[] image;

    @ManyToOne
    private Artist director;

    @ManyToMany
    private Set<Artist> actors;

    @OneToMany(mappedBy="movie")
    private List<Review> reviews;


    public Movie() {
        this.actors = new HashSet<>();
        this.reviews = new ArrayList<>();
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Artist getDirector() {
        return director;
    }

    public void setDirector(Artist director) {
        this.director = director;
    }

    public Set<Artist> getActors() {
        return actors;
    }

    public void setActors(Set<Artist> actors) {
        this.actors = actors;
    }

    @Nullable
    public byte[] getImage() {
        return image;
    }

    public String getEncodedImage() {
        if (image != null) {
            return Base64.getEncoder().encodeToString(image);
        }
        return null;
    }

    public void setImage(@Nullable byte[] image) {
        this.image = image;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Movie other = (Movie) obj;
        return Objects.equals(title, other.title) && year.equals(other.year);
    }
}
