package WizardTD.entities.monsters.movement;

import WizardTD.board.Tile;

public class PathEdge {
    private Tile toTile;
    private Tile fromTile;

    /**
     * @param toTile   a tile that can be navigated to from the current tile.
     * @param fromTile the current tile.
     */
    public PathEdge(Tile toTile, Tile fromTile) {
        this.toTile = toTile;
        this.fromTile = fromTile;
    }

    /**
     * @return A tile that can be navigated to from the current tile.
     */
    public Tile getToTile() {
        return toTile;
    }

    /**
     * @return The current Tile.
     */
    public Tile getFromTile() {
        return fromTile;
    }
}
