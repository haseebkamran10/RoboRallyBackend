package dk.dtu.compute.se.pisd.roborally.api.model;

public enum Heading {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public Heading next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    // Method to get the previous heading in counter-clockwise direction
    public Heading prev() {
        return values()[(this.ordinal() + values().length - 1) % values().length];
    }
}
