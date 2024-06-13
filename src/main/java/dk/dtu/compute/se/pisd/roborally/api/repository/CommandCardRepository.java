package dk.dtu.compute.se.pisd.roborally.api.repository;

import dk.dtu.compute.se.pisd.roborally.api.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.api.model.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandCardRepository extends JpaRepository<CommandCard, Long> {
}
