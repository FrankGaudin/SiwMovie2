package it.uniroma3.siw.siwmovie2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class Siwmovie2Application {

	public static void main(String[] args) {
		SpringApplication.run(Siwmovie2Application.class, args);
	}

}
