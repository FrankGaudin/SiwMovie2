package it.uniroma3.siw.siwmovie2.service;

import it.uniroma3.siw.siwmovie2.model.Artist;
import it.uniroma3.siw.siwmovie2.model.Movie;
import it.uniroma3.siw.siwmovie2.repository.ArtistRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;
//
//    @Autowired
//    private ImageRepository imageRepository;

//    @Autowired
//    private ImageValidator imageValidator;

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
    public void addArtist(@Valid Artist artist) {
        this.artistRepository.save(artist);
    }

//    @Transactional
//	public void createNewArtist(Artist artist, MultipartFile image) throws IOException {
//		Image artistImg = new Image(image.getBytes());
//        this.imageRepository.save(artistImg);
//        artist.setProfilePicture(artistImg);
//        this.artistRepository.save(artist);
//	}

    @Transactional
    public void deleteArtist(Long artistId) {
        Artist artist = this.getActorById(artistId);
        Set<Movie> movies = artist.getActorOf();
        for(Movie movie : movies) {
            movie.getActors().remove(artist);
        }
        this.artistRepository.delete(artist);
    }

//    @Transactional
//	public void addProfilePicture(Artist artist, MultipartFile image) throws IOException{
//        if (this.imageValidator.isImage(image) || image.getSize() < ImageValidator.MAX_IMAGE_SIZE){
//            Image artistImg = new Image(image.getBytes());
//            this.imageRepository.save(artistImg);
//            artist.setProfilePicture(artistImg);
//            this.artistRepository.save(artist);
//        }
//    }
//
//	@Transactional
//	public void setProfilePicture(Artist artist, MultipartFile image) throws IOException{
//        if (this.imageValidator.isImage(image) || image.getSize() < ImageValidator.MAX_IMAGE_SIZE){
//        	Image oldImg = artist.getProfilePicture();
//        	Image newImg = new Image(image.getBytes());
//        	this.imageRepository.save(newImg);
//        	artist.setProfilePicture(newImg);
//            this.artistRepository.save(artist);
//            this.imageRepository.delete(oldImg);
//        }
//    }

}