package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.MouseEvent;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;

import static WizardTD.utils.Dimensions.*;

import WizardTD.infopanes.Button;
import WizardTD.infopanes.Player;
import WizardTD.utils.ImageLoader;

import WizardTD.board.Board;
import WizardTD.board.Tile;
import WizardTD.board.towers.Tower;
import WizardTD.entities.projectiles.Fireball;
import WizardTD.game.Game;
import WizardTD.game.GameLoader;

public class App extends PApplet {

    public static final int FPS = 60;

    public String configPath;

    Player player;
    Game game;
    Board board;

    public App() {
        this.configPath = "config.json";
    }

    public void setConfigPath(String path) {
        this.configPath = path;
    }

    /**
     * Initialise the setting of the window size.
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the
     * player, enemies and map elements.
     */

    @Override
    public void setup() {
        frameRate(FPS);

        ImageLoader images = new ImageLoader(this);
        images.reloadImages();

        GameLoader gameLoader = new GameLoader(loadJSONObject(configPath));
        Fireball.setFireballImage(ImageLoader.getImage("src/main/resources/WizardTD/fireball.png"));

        board = new Board(gameLoader.getLayoutPath());
        board.constructBoard(this);

        game = new Game(board);
        player = game.getPlayer();

        game.createEventSchedule(gameLoader.getEventSchedule());
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed() {
        if (key == 'r') {
            if (game.getGameState() != "ONGOING") {
                setup();
            }
        }

        if (!player.isAlive()) {
            return;
        }

        if (key == 'p') {
            game.getPlayer().togglePauseState();
        } else if (key == 'f') {
            game.getPlayer().toggleFastForwardState();
        } else if (key == 't') {
            game.getPlayer().toggleBuildModeState();
        } else if (key == '1') {
            game.getPlayer().toggleUpgradeRangeModeState();
        } else if (key == '2') {
            game.getPlayer().toggleUpgradeSpeedModeState();
        } else if (key == '3') {
            game.getPlayer().toggleUpgradeDamageModeState();
        } else if (key == 'm') {
            game.getPlayer().useManaPoolSpell();
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
    @Override
    public void keyReleased() {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!player.isAlive()) {
            return;
        }

        if (e.getY() > 40 && e.getX() < WIDTH - SIDEBAR) {
            int xBoardLocation = (int) Math.floor(e.getX() / 32);
            int yBoardLocation = (int) Math.floor((e.getY() - TOPBAR) / 32);

            Tile currentTile = board.getTile(xBoardLocation, yBoardLocation);

            if (game.getPlayer().isBuilding()) {
                game.buildTower(currentTile);
            }

            if (currentTile.hasTower()) {
                Tower currentTower = currentTile.getTower();
                if (player.isUpgradingDamage()) {
                    game.upgradeDamage(currentTower);
                }

                if (player.isUpgradingRange()) {
                    game.upgradeRange(currentTower);
                }

                if (player.isUpgradingSpeed()) {
                    game.upgradeFiringSpeed(currentTower);

                }

            }

        } else if (e.getX() > Button.getXPosition() && e.getX() < Button.getXPosition() + Button.getButtonWidth()) {
            Button[] buttons = player.getButtons();
            for (int i = 0; i < buttons.length; i++) {
                if (e.getY() > buttons[i].getYPosition()
                        && e.getY() < buttons[i].getYPosition() + Button.getButtonHeight()) {
                    buttons[i].action();
                }
            }
        }

    }

    private Tile prevHoverTile = null;
    private Button prevButton = null;

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getY() > 40 && e.getX() < WIDTH - SIDEBAR) {
            int xBoardLocation = (int) Math.floor(e.getX() / 32);
            int yBoardLocation = (int) Math.floor((e.getY() - TOPBAR) / 32);

            Tile currentTile = board.getTile(xBoardLocation, yBoardLocation);

            if (currentTile != prevHoverTile) {
                if (prevHoverTile != null) {
                    prevHoverTile.getTower().toggleDisplayRange();
                    game.getPlayer().hideUpgradeDisplay();
                    prevHoverTile = null;
                }

                if (currentTile.getTower() != null) {
                    prevHoverTile = currentTile;
                    currentTile.getTower().toggleDisplayRange();
                    game.getPlayer().showUpgradeDisplay(currentTile.getTower());
                }
            }
        } else if (e.getX() > Button.getXPosition() && e.getX() < Button.getXPosition() + Button.getButtonWidth()) {
            Button[] buttons = player.getButtons();
            for (int i = 0; i < buttons.length; i++) {
                if (e.getY() > buttons[i].getYPosition()
                        && e.getY() < buttons[i].getYPosition() + Button.getButtonHeight()) {
                    if (prevButton != buttons[i]) {
                        game.getPlayer().showToolTip(buttons[i]);
                    }
                    break;
                } else {
                    game.getPlayer().hideToolTip();
                }
            }
        } else {
            game.getPlayer().hideToolTip();
            game.getPlayer().hideUpgradeDisplay();
        }
    }

    public void updateLogic() {
        if (player.isFastforwarding()) {
            game.updateLogic();
        }
        game.updateLogic();

    }

    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        updateLogic();

        game.draw(this);

        if (game.getGameState() != "ONGOING") {
            if (game.getGameState() == "WIN") {
                textSize(60);
                text("You WIN!", 215, 200);
            } else {
                textSize(60);
                text("You LOSE!", 200, 200);
            }
            textSize(40);
            text("Press 'r' to restart.", 170, 250);
        }

    }

    public static void main(String[] args) {
        PApplet.main("WizardTD.App");
    }

}
