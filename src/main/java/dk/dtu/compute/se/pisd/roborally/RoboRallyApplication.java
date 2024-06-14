package dk.dtu.compute.se.pisd.roborally;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "dk.dtu.compute.se.pisd.roborally.api.model")
@EnableJpaRepositories(basePackages = "dk.dtu.compute.se.pisd.roborally.api.repository")
@ComponentScan(basePackages = "dk.dtu.compute.se.pisd.roborally")
public class RoboRallyApplication {
	public static void main(String[] args) {
		SpringApplication.run(RoboRallyApplication.class, args);
	}
}
