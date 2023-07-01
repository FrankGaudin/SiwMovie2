package it.uniroma3.siw.siwmovie2.controller.validator;

import it.uniroma3.siw.siwmovie2.model.Artist;
import it.uniroma3.siw.siwmovie2.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ArtistValidator implements Validator {

    @Autowired
    private ArtistService artistService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Artist.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Artist artist = (Artist) o;
        if (artist.getName() != null && artist.getName() != null
                && artistService.existsByNameAndSurname(artist.getName(), artist.getSurname())) {
            errors.reject("artist.duplicate");
        }

    }

}
