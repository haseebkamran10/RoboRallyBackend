package dk.dtu.compute.se.pisd.roborally.api.repository;

import dk.dtu.compute.se.pisd.roborally.api.model.Board;
import dk.dtu.compute.se.pisd.roborally.api.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;



public interface SpaceRepository extends JpaRepository<Space, Long> {
    Space findByXAndYAndBoard(int x, int y, Board board);  // Updated method to include board
}
