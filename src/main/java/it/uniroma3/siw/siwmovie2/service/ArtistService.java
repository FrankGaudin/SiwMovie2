package it.uniroma3.siw.siwmovie2.service;

import it.uniroma3.siw.siwmovie2.model.Artist;
import it.uniroma3.siw.siwmovie2.model.Movie;
import it.uniroma3.siw.siwmovie2.repository.ArtistRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Transactional
    public Iterable<Artist> getAllArtists() {
        return this.artistRepository.findAll();
    }

    @Transactional
    public Artist getActorById(Long actorId) {
        return artistRepository.findById(actorId).get();
    }

    @Transactional
    public Iterable<Artist> findActorsNotInMovie(Long movieId) {
        return this.artistRepository.findActorsNotInMovie(movieId);
    }

    @Transactional
    public boolean existsByNameAndSurname(String name, String surname) {
        return artistRepository.existsByNameAndSurname(name, surname);
    }

    @Transactional
    public void addArtist(@Valid Artist artist, @NotNull MultipartFile image) throws IOException {
        byte[] photoBytes = image.getBytes();
        artist.setImage(photoBytes);
        this.artistRepository.save(artist);
    }


    @Transactional
    public void deleteArtist(Long artistId) {
        Artist artist = this.getActorById(artistId);
        Set<Movie> movies = artist.getActorOf();
        for(Movie movie : movies) {
            movie.getActors().remove(artist);
            if(movie.getDirector() != null && movie.getDirector().equals(artist)){
                movie.setDirector(null);
            }
        }
        this.artistRepository.delete(artist);
    }

    @Transactional
    public void updateArtist(Long artistId, String name, String surname,
                             LocalDate dateOfBirth, LocalDate dateOfDeath, MultipartFile image) throws IOException{
        Artist artist = artistRepository.findById(artistId).get();
        if(name != null && name.length() != 0) {
            artist.setName(name);
        }
        if(surname != null && surname.length() != 0) {
            artist.setSurname(surname);
        }
        if(dateOfBirth != null){
            artist.setDateOfBirth(dateOfBirth);
        }
        if(dateOfDeath != null){
            artist.setDateOfDeath(dateOfDeath);
        }
        if (image != null && !image.isEmpty()) {
            byte[] photoBytes = image.getBytes();
            artist.setImage(photoBytes);
        }
        artistRepository.save(artist);
    }

}