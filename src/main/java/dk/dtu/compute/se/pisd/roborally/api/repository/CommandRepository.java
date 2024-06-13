package dk.dtu.compute.se.pisd.roborally.api.repository;

import dk.dtu.compute.se.pisd.roborally.api.model.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {
}
