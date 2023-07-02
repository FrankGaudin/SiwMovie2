package it.uniroma3.siw.siwmovie2.controller;

import it.uniroma3.siw.siwmovie2.controller.validator.ArtistValidator;
import it.uniroma3.siw.siwmovie2.model.Artist;
import it.uniroma3.siw.siwmovie2.repository.ArtistRepository;
import it.uniroma3.siw.siwmovie2.service.ArtistService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@Controller
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistValidator artistValidator;

    @GetMapping(value="/admin/formNewArtist")
    public String formNewArtist(Model model) {
        model.addAttribute("artist", new Artist());
        return "admin/formNewArtist.html";
    }

    @GetMapping(value="/admin/indexArtist")
    public String indexArtist() {
        return "admin/indexArtist.html";
    }

    @PostMapping("/admin/artist")
    public String newArtist(@Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult, Model model,
                            @RequestParam("fileImage") @NotNull MultipartFile image ) throws IOException{
        this.artistValidator.validate(artist, bindingResult);
        if (!bindingResult.hasErrors()) {
            this.artistService.addArtist(artist, image);
            model.addAttribute("artist", artist);
            return "artist.html";
        } else {
            model.addAttribute("messaggioErrore", "Questo artista esiste già");
            return "admin/formNewArtist.html";
        }
    }

    @GetMapping("/artist/{id}")
    public String getArtist(@PathVariable("id") Long id, Model model) {
        Artist artist = this.artistService.getActorById(id);
        model.addAttribute("artist", artist);
        return "artist.html";
    }

    @GetMapping("/artist")
    public String getArtists(Model model) {
        Iterable<Artist> artists = this.artistService.getAllArtists();
        model.addAttribute("artists", artists);
        return "artists.html";
    }

    @GetMapping("/admin/manageArtists")
    public String manageArtists(Model model) {
        model.addAttribute("artists", this.artistService.getAllArtists());
        return "admin/manageArtists.html";
    }

    @GetMapping("/admin/deleteArtist/{id}")
    public String deleteArtist(@PathVariable("id") Long id, Model model) {
        this.artistService.deleteArtist(id);
        model.addAttribute("artists", this.artistService.getAllArtists());
        return "admin/manageArtists.html";
    }

    @GetMapping("/admin/formUpdateArtist/{id}")
    public String updateArtist(@PathVariable("id") Long id, Model model){
        Artist artist = this.artistService.getActorById(id);
        model.addAttribute("artist", artist);
        return "admin/formUpdateArtist.html";
    }
	@PostMapping("/admin/updatedArtist/{id}")
	public String formUpdateArtist(@PathVariable("id") Long id,
                                   @RequestParam("name") String name,
                                   @RequestParam("surname") String surname,
                                   @RequestParam(value = "dateOfBirth", required = false) LocalDate dateOfBirth,
                                   @RequestParam(value ="dateOfDeath", required = false) LocalDate dateOfDeath,
                                   @RequestParam(value = "fileImage", required = false) MultipartFile imageFile,
                                   Model model) throws IOException {
		Artist artist = this.artistService.getActorById(id);
        artistService.updateArtist(id, name, surname, dateOfBirth,dateOfDeath, imageFile);
		model.addAttribute("artist", artist);
		return "admin/updatedArtist.html";
	}
}