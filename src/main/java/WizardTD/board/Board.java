package WizardTD.board;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import processing.core.PApplet;
import processing.core.PImage;
import WizardTD.utils.Drawable;
import WizardTD.utils.ImageLoader;
import WizardTD.utils.ImageManipulation;
import static WizardTD.utils.Dimensions.*;

import WizardTD.board.Tile.PathType;
import WizardTD.board.Tile.TileType;
import WizardTD.board.towers.Tower;

import WizardTD.entities.monsters.movement.Path;

public class Board implements Drawable {
    private Tile[][] board;

    private Path[] paths;
    ArrayList<Tile> spawnTiles = new ArrayList<>();
    private Tile wizardHouse;

    private File levelFile;

    /**
     * Initalises the level file and board matrix.
     * 
     * @param layoutPath a String representing the path to the relevant level file.
     */
    public Board(String layoutPath) {
        this.levelFile = new File(layoutPath);
        board = new Tile[BOARD_DIM][BOARD_DIM];
    }

    /**
     * Creates the initial multi-dimensional array of trials that represents the
     * game board and loads the relevant tile
     * data into this array from the level file.
     */
    public void createTileMatrix() {
        String[] tileLocations = new String[20];

        try {
            Scanner scanner = new Scanner(levelFile);

            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                tileLocations[lineNumber] = scanner.nextLine();
                lineNumber++;
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Yas queen");
        }

        for (int i = 0; i < BOARD_DIM; i++) {
            for (int j = 0; j < BOARD_DIM; j++) {
                if (j < tileLocations[i].length() - 1) {
                    board[i][j] = new Tile(tileLocations[i].charAt(j), i, j);
                } else {
                    board[i][j] = board[i][j] = new Tile(' ', i, j);
                }
            }
        }
    }

    /**
     * Determines the appropriate rotation of every image (particularly path images)
     * and assigns the rotated images to
     * the tiles such that they can be accessed and drawn.
     * 
     * @param app the PApplet of the current game.
     */
    public void assignTileImages(PApplet app) {
        // Load images
        PImage grassImage = ImageLoader.getImage("src/main/resources/WizardTD/grass.png"); // app.loadImage("src/main/resources/WizardTD/grass.png");
        PImage shrubImage = ImageLoader.getImage("src/main/resources/WizardTD/shrub.png"); // app.loadImage("src/main/resources/WizardTD/shrub.png");
        PImage houseImage = ImageLoader.getImage("src/main/resources/WizardTD/wizard_house.png"); // app.loadImage("src/main/resources/WizardTD/wizard_house.png");
        PImage pathLinearImage = ImageLoader.getImage("src/main/resources/WizardTD/path0.png"); // app.loadImage("src/main/resources/WizardTD/path0.png");
        PImage pathCornerImage = ImageLoader.getImage("src/main/resources/WizardTD/path1.png"); // app.loadImage("src/main/resources/WizardTD/path1.png");
        PImage pathJunctionImage = ImageLoader.getImage("src/main/resources/WizardTD/path2.png"); // app.loadImage("src/main/resources/WizardTD/path2.png");
        PImage pathIntersectionImage = ImageLoader.getImage("src/main/resources/WizardTD/path3.png"); // app.loadImage("src/main/resources/WizardTD/path3.png");
        // PImage testImage =
        // ImageLoader.getImage("src/main/resources/WizardTD/tower0.png"); //
        // app.loadImage("src/main/resources/WizardTD/tower0.png");

        // Assign images
        for (int i = 0; i < BOARD_DIM; i++) {
            for (int j = 0; j < BOARD_DIM; j++) {
                switch (board[i][j].getTileType()) {
                    case GRASS:
                        board[i][j].setTileImage(grassImage);
                        break;
                    case SHRUB:
                        board[i][j].setTileImage(shrubImage);
                        break;
                    case WIZARDHOUSE:
                        // board[i][j].setWizardGrassImage(grassImage);
                        board[i][j].setTileImage(houseImage);
                        break;
                    case PATH:
                        boolean[] proximatePathPresence = { false, false, false, false };
                        int pathConnections = 0;
                        int sequentialConnections = 0;

                        if (i != 0) {
                            if (board[i - 1][j].isWalkable() == true) {
                                proximatePathPresence[3] = true;
                            }
                        } else if (i == 0) {
                            proximatePathPresence[3] = true;
                        }

                        if (i < BOARD_DIM - 1) {
                            if (board[i + 1][j].isWalkable() == true) {
                                proximatePathPresence[1] = true;
                            }
                        } else if (i == BOARD_DIM - 1) {
                            proximatePathPresence[1] = true;
                        }

                        if (j < BOARD_DIM - 1) {
                            if (board[i][j + 1].isWalkable() == true) {
                                proximatePathPresence[2] = true;
                            }
                        } else if (j == BOARD_DIM - 1) {
                            proximatePathPresence[2] = true;
                        }

                        if (j != 0) {
                            if (board[i][j - 1].isWalkable() == true) {
                                proximatePathPresence[0] = true;
                            }
                        } else if (j == 0) {
                            proximatePathPresence[0] = true;
                        }

                        int firstWalkable = 0;
                        int firstUnwalkable = -1;
                        for (int k = 0; k < proximatePathPresence.length; k++) {

                            if (proximatePathPresence[k] == true) {
                                firstWalkable = k;
                            } else {
                                if (firstUnwalkable == -1) {
                                    firstUnwalkable = k;
                                }
                            }

                            if (k != proximatePathPresence.length - 1) {
                                if (proximatePathPresence[k + 1] == true) {
                                    pathConnections++;
                                    if (proximatePathPresence[k] == true) {
                                        sequentialConnections = sequentialConnections + 2;
                                    }
                                }
                            } else {
                                if (proximatePathPresence[0] == true) {
                                    pathConnections++;
                                    if (proximatePathPresence[k] == true) {
                                        sequentialConnections = sequentialConnections + 2;
                                    }
                                }
                            }
                        }

                        if (pathConnections == 4) {
                            board[i][j].setPathType(PathType.INTERSECTION);
                            board[i][j].setTileImage(pathIntersectionImage);

                        } else if (pathConnections == 3) {
                            board[i][j].setPathType(PathType.JUNCTION);
                            PImage rotatedImage = ImageManipulation.rotateImageByDegrees(app,
                                    pathJunctionImage, 90 * (firstUnwalkable));

                            board[i][j].setTileImage(rotatedImage);
                        } else {
                            if (sequentialConnections == 2) {
                                board[i][j].setPathType(PathType.CORNER);
                                int rotateAngle = 0;

                                if (proximatePathPresence[0] == false && proximatePathPresence[1] == false) {
                                    rotateAngle = 0;
                                } else if (proximatePathPresence[1] == false && proximatePathPresence[2] == false) {
                                    rotateAngle = 90;
                                } else if (proximatePathPresence[2] == false && proximatePathPresence[3] == false) {
                                    rotateAngle = 180;
                                } else if (proximatePathPresence[3] == false && proximatePathPresence[0] == false) {
                                    rotateAngle = 270;
                                }

                                PImage rotatedImage = ImageManipulation.rotateImageByDegrees(app, pathCornerImage,
                                        rotateAngle);

                                board[i][j].setTileImage(rotatedImage);
                            } else {
                                board[i][j].setPathType(PathType.LINEAR);
                                PImage rotatedImage = ImageManipulation.rotateImageByDegrees(app, pathLinearImage,
                                        90 * (firstWalkable + 1));

                                board[i][j].setTileImage(rotatedImage);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Iteratres through every tile on the board and stores them in the appropriate
     * object if they are of
     * such significance such that they likeley need to be retriveed frequently at a
     * later time.
     */
    public void findSignificantTiles() {
        for (int i = 0; i < BOARD_DIM; i++) {
            for (int j = 0; j < BOARD_DIM; j++) {
                if (board[i][j].getTileType() == TileType.PATH) {
                    if (i == 0 || i == BOARD_DIM - 1) {
                        spawnTiles.add(board[i][j]);
                    } else if (j == 0 || j == BOARD_DIM - 1) {
                        spawnTiles.add(board[i][j]);
                    }
                }

                if (board[i][j].getTileType() == TileType.WIZARDHOUSE) {
                    wizardHouse = board[i][j];
                }
            }
        }
    }

    /**
     * Finds the shortest path from every possible starting point and stores the
     * paths in an array.
     */
    public void getAllPaths() {
        paths = new Path[spawnTiles.size()];
        for (int i = 0; i < spawnTiles.size(); i++) {
            paths[i] = new Path(this, spawnTiles.get(i));
        }
    }

    /**
     * Retrives a copy of a path from the array of all optimal paths.
     * 
     * @param pathIndex an identifier of the desired path.
     * @return a path from the edge of the board to the wizard house.
     */
    public Path getPath(int pathIndex) {
        if (paths.length != 0) {
            if (pathIndex >= 0 && pathIndex < paths.length) {
                return new Path(this, paths[pathIndex]);
            }
        }
        return null;
    }

    public Path getRandomPath() {
        if (paths.length != 0) {
            Random random = new Random();
            int n = random.nextInt(paths.length);

            return new Path(this, paths[n]);
        } else {
            return null;
        }
    }

    /**
     * @return The tile that contains the wizard house.
     */
    public Tile getWizardHouse() {
        return wizardHouse;
    }

    /**
     * The number of path tiles around the edge of the board.
     * 
     * @return An integer representing the number of tiles a monster can spawn on.
     */
    public int getNumberSpawnTiles() {
        return spawnTiles.size();
    }

    /**
     * @param id an identifer for the desired spawn tile in the spawn tiles array.
     * @return The selected tile upon which it is possible for monster's to spawn.
     */
    public Tile getSpawnTile(int id) {
        return spawnTiles.get(id);
    }

    /**
     * Set the file that will be used to determine and load the game board.
     * 
     * @param levelFile a File object containing the text file which describes the
     *                  level to be loaded.
     */
    public void setLevel(File levelFile) {
        this.levelFile = levelFile;
    }

    /**
     * Brings together all of the initial steps for generating a game board. This
     * method, creates the tile matrix,
     * assigns all tile images, finds the significant tiles and generates all
     * optimal paths.
     * 
     * @param app the PApplet of the current game window.
     */
    public void constructBoard(PApplet app) {
        createTileMatrix();
        assignTileImages(app);
        findSignificantTiles();
        getAllPaths();
    }

    /**
     * Constructs a tower on a specified tile.
     * 
     * @param tower the Tower to be built on the relevant tile.
     * @param xLoc  the x-axis location of the new tower.
     * @param yLoc  the y-axis location of the new tower.
     */
    public void buildTower(Tower tower, int xLoc, int yLoc) {
        board[yLoc][xLoc].buildTower(tower);
    }

    /**
     * Retrives a tile from the Tile matrix that represents the game board.
     * 
     * @param xLocation the number of tiles leftward.
     * @param yLocation the number of tiles downard.
     * @return the tile at the specified location.
     */
    public Tile getTile(int xLocation, int yLocation) {
        return board[xLocation][yLocation];
    }

    public void updateLogic() {

    }

    public void draw(PApplet app) {
        for (int i = 0; i < BOARD_DIM; i++) {
            for (int j = 0; j < BOARD_DIM; j++) {
                Tile currentTile = board[i][j];
                currentTile.draw(app);
            }
        }
    }

}
