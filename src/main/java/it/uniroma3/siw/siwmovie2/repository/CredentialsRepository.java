package it.uniroma3.siw.siwmovie2.repository;

import it.uniroma3.siw.siwmovie2.model.Credentials;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

    public Optional<Credentials> findByUsername(String username);

}
