package dk.dtu.compute.se.pisd.roborally.api.repository;

import dk.dtu.compute.se.pisd.roborally.api.model.CommandCardField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandCardFieldRepository extends JpaRepository<CommandCardField, Long> {
}
