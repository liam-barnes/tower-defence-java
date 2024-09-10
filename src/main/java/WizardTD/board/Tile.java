package WizardTD.board;

import processing.core.PApplet;
import processing.core.PImage;
import WizardTD.utils.Drawable;
import WizardTD.utils.ImageLoader;

import static WizardTD.utils.Dimensions.*;

import WizardTD.board.towers.Tower;

public class Tile implements Drawable {
    public static enum TileType {
        GRASS,
        SHRUB,
        PATH,
        WIZARDHOUSE,
        TOWER;
    }

    public static enum PathType {
        INTERSECTION,
        JUNCTION,
        CORNER,
        LINEAR;
    }

    private boolean walkable;

    private int xLocation;
    private int yLocation;

    private TileType tileType;
    private PathType pathType;
    private PImage tileImage;
    private PImage wizardGrassImage;

    private Tower tileTower = null;

    private boolean traversed;

    /**
     * Assigning the inital tile type and initialising variables.
     * 
     * @param type      a character from the level file indicating the tile type.
     * @param xLocation the x location of the tile in the board matrix.
     * @param yLocation the y location of the tile in the board matrix.
     */
    public Tile(char type, int xLocation, int yLocation) {
        this.xLocation = xLocation;
        this.yLocation = yLocation;

        this.wizardGrassImage = ImageLoader.getImage("src/main/resources/WizardTD/grass.png");

        if (type == ' ') {
            tileType = TileType.GRASS;
            this.walkable = false;
        } else if (type == 'S') {
            tileType = TileType.SHRUB;
            this.walkable = false;
        } else if (type == 'W') {
            tileType = TileType.WIZARDHOUSE;
            this.walkable = true;
        } else if (type == 'X') {
            tileType = TileType.PATH;
            this.walkable = true;
        } else if (type == 'T') {
            tileType = TileType.TOWER;
            this.walkable = false;
        }

        this.traversed = false;
    }

    public void setTraversed(boolean state) {
        if (state) {
            traversed = true;
        } else {
            traversed = false;
        }
    }

    protected void setPathType(PathType pathType) {
        if (tileType == TileType.PATH) {
            this.pathType = pathType;
        } else {
            return;
        }
    }

    public void buildTower(Tower tileTower) {
        if (tileType == TileType.GRASS) {
            this.tileTower = tileTower;
            tileType = TileType.TOWER;
        }

    }

    public void setTileImage(PImage image) {
        tileImage = image;
    }

    public boolean hasTower() {
        if (tileTower == null) {
            return false;
        } else {
            return true;
        }
    }

    public Tower getTower() {
        if (tileTower == null) {
            return null;
        } else {
            return tileTower;
        }
    }

    /**
     * Retrives the traversal status (used for path finding).
     * 
     * @return A boolean notating whether the tile has been traversed.
     */
    public boolean isTraversed() {
        return traversed;
    }

    // protected PImage getTileImage() {
    // return tileImage;
    // }

    /**
     * Gets pathtype which describes the directionality of the path
     * 
     * @return The type of path.
     */
    protected PathType getPathType() {
        return pathType;
    }

    /**
     * Gets the tiletype with describes some of the properties of the tile.
     * 
     * @return The type of tile.
     */
    public TileType getTileType() {
        return tileType;
    }

    /**
     * Gets the x coordinate of the top left corner of the tile in pixels.
     * 
     * @return int of pixels rightward
     */
    public int getXPixels() {
        return xLocation * CELLSIZE;
    }

    /**
     * Gets the y coordinate of the top left corner of the tile in pixels.
     * 
     * @return int of pixels downward
     */
    public int getYPixels() {
        return TOPBAR + (32 * yLocation);
    }

    /**
     * Get the x coordinate in the game board matrix.
     * 
     * @return Number of tiles rightward
     */
    public int getXLocation() {
        return xLocation;
    }

    /**
     * Gets the y coordinate in the game board matrix
     * 
     * @return Number of tiles downward
     */
    public int getYLocation() {
        return yLocation;
    }

    /**
     * Used predominantly for banishing monsters and path finding.
     * 
     * @return A boolean indicates whether the tile is a Wizard House.
     */
    public boolean isWizardHouse() {
        if (this.tileType == TileType.WIZARDHOUSE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Indicates whether monsters are able to walk on this tile.
     * 
     * @return a boolean indicating whether monsters can walk on this tile.
     */
    public boolean isWalkable() {
        return walkable;
    }

    public void updateLogic() {

    }

    /**
     * Draw objects to the window.
     * 
     * @param app the PApplet representing the game window.
     */
    public void draw(PApplet app) {
        app.image(wizardGrassImage, getXPixels(), getYPixels());
        app.image(tileImage, getXPixels(), getYPixels());

        if (getTower() != null) {
            // return;
            tileTower.draw(app);
        }

    }
}
