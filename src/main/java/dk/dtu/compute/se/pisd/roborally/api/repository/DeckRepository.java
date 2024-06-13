package dk.dtu.compute.se.pisd.roborally.api.repository;

import dk.dtu.compute.se.pisd.roborally.api.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {
}
