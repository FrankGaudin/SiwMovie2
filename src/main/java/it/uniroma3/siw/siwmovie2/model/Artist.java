package it.uniroma3.siw.siwmovie2.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String surname;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfDeath;

    @ManyToMany(mappedBy="actors")
    private Set<Movie> starredMovies;

    @OneToMany(mappedBy="director")
    private List<Movie> directedMovies;

    @Nullable
    private byte[] image;

    public Artist(){
        this.starredMovies = new HashSet<>();
        this.directedMovies = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public Set<Movie> getActorOf() {
        return starredMovies;
    }

    public Set<Movie> getStarredMovies() {
        return starredMovies;
    }

    public void setStarredMovies(Set<Movie> starredMovies) {
        this.starredMovies = starredMovies;
    }

    public List<Movie> getDirectedMovies() {
        return directedMovies;
    }

    public void setDirectedMovies(List<Movie> directedMovies) {
        this.directedMovies = directedMovies;
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
        return Objects.hash(name, surname);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Artist other = (Artist) obj;
        return Objects.equals(name, other.name) && Objects.equals(surname, other.surname);
    }
}
