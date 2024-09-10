package WizardTD.entities.monsters.movement;

import WizardTD.board.Tile;
import WizardTD.board.Tile.TileType;
import WizardTD.board.Board;

import java.util.Queue;
import java.util.HashMap;
import java.util.LinkedList;

import static WizardTD.utils.Dimensions.*;

public class Path {
    private Board board;
    private Queue<PathEdge> searchQueue = new LinkedList<>();
    private HashMap<Tile, Tile> tileMapping = new HashMap<>();
    private Queue<Tile> path = new LinkedList<>();

    private Tile initialTile;

    /**
     * Finds and creates optimal path to wizard house from the specified tile on the
     * current game board.
     * 
     * @param board       the current game board.
     * @param initialTile the tile from which to search for the optimal path.
     */
    public Path(Board board, Tile initialTile) {
        this.board = board;
        this.initialTile = initialTile;

        resetTraversalState();
        generatePath();
    }

    // public Path(Board board) {
    // this.board = board;

    // resetTraversalState();

    // getRandomInitialTile();
    // generatePath();

    // Queue<Tile> copyPathQueue = board.getRandomPath().getPathToCopy();
    // for (Tile tile : copyPathQueue) {
    // path.add(tile);
    // }
    // }

    /**
     * Returns a copy of an existing path as a new object such that it can be used
     * by another monster.
     * 
     * @param board        the current game board.
     * @param existingPath an existing path to copy.
     */
    public Path(Board board, Path existingPath) {
        this.board = board;

        Queue<Tile> existingPathQueue = existingPath.getPathToCopy();

        for (Tile tile : existingPathQueue) {
            path.add(tile);
        }
    }

    /**
     * Iterates through every tile in the game board matrix and sets the traversal
     * state to false in preparation for another path finding operation.
     */
    public void resetTraversalState() {
        for (int i = 0; i < BOARD_DIM; i++) {
            for (int j = 0; j < BOARD_DIM; j++) {
                board.getTile(i, j).setTraversed(false);
            }
        }

    }

    /**
     * Retrives the raw queue of tiles typically for the purpose of copying a path.
     * 
     * @return Exposes the queue of tiles that constitutes the path
     */
    public Queue<Tile> getPathToCopy() {
        return path;
    }

    /**
     * @param checkTile the tile to verify.
     * @return A boolean indicating whether to continue the search from this path.
     */
    private static boolean checkValidPathTile(Tile checkTile) {
        if (checkTile.isTraversed() == false) {
            if (checkTile.getTileType() == TileType.PATH) {
                return true;
            }
        }

        return false;
    }

    /**
     * Uses breadth-first search to find the optimal path from a given starting
     * location to the Wizard House.
     */
    public void generatePath() {
        Tile startTile = null;

        searchQueue.add(new PathEdge(board.getWizardHouse(), null));

        while (searchQueue.size() > 0) {
            PathEdge currentEdge = searchQueue.remove();

            Tile currentTile = currentEdge.getToTile();
            int xLocation = currentTile.getXLocation();
            int yLocation = currentTile.getYLocation();

            currentTile.setTraversed(true);

            if (!tileMapping.containsKey(currentTile)) {
                tileMapping.put(currentTile, currentEdge.getFromTile());
            }

            if (xLocation == initialTile.getXLocation() && yLocation == initialTile.getYLocation()) {
                startTile = initialTile;
                break;
            }

            if (xLocation < BOARD_DIM - 1) {
                if (checkValidPathTile(board.getTile(xLocation + 1, yLocation))) {
                    PathEdge tmpEdge = new PathEdge(board.getTile(xLocation + 1, yLocation), currentTile);
                    searchQueue.add(tmpEdge);
                }
            }

            if (xLocation != 0) {
                if (checkValidPathTile(board.getTile(xLocation - 1, yLocation))) {
                    PathEdge tmpEdge = new PathEdge(board.getTile(xLocation - 1, yLocation), currentTile);
                    searchQueue.add(tmpEdge);
                }
            }

            if (yLocation < BOARD_DIM - 1) {
                if (checkValidPathTile(board.getTile(xLocation, yLocation + 1))) {
                    PathEdge tmpEdge = new PathEdge(board.getTile(xLocation, yLocation + 1), currentTile);
                    searchQueue.add(tmpEdge);
                }
            }

            if (yLocation != 0) {
                if (checkValidPathTile(board.getTile(xLocation, yLocation - 1))) {
                    PathEdge tmpEdge = new PathEdge(board.getTile(xLocation, yLocation - 1), currentTile);
                    searchQueue.add(tmpEdge);
                }
            }

        }

        path.add(startTile);
        Tile goTo = tileMapping.get(startTile);

        while (goTo != null) {
            path.add(goTo);
            goTo = tileMapping.get(goTo);
        }
    }

    /**
     * Retrives without removing the next tile in the path queue.
     * 
     * @return The next tile in the path.
     */
    public Tile viewNextTile() {
        try {
            return path.element();
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Removes the tile at the front of the queue, thereby exposing the next tile.
     * 
     * @return The tile at the front of the path queue.
     */
    public Tile moveToNextTile() {
        try {
            return path.remove();
        } catch (Exception e) {
            return null;
        }

    }

}
