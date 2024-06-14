package dk.dtu.compute.se.pisd.roborally.api.repository;

import dk.dtu.compute.se.pisd.roborally.api.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
