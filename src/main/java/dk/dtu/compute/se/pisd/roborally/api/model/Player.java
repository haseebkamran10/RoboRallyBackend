package dk.dtu.compute.se.pisd.roborally.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dk.dtu.compute.se.pisd.roborally.api.model.GameSession;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String avatar;




    @ManyToOne
    @JsonBackReference
    private GameSession gameSession;


    private boolean ready = false;
}