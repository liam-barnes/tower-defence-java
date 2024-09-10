package WizardTD.game;

import WizardTD.utils.Drawable;
import processing.core.PApplet;

import java.util.ArrayList;

import WizardTD.board.Board;
import WizardTD.board.Tile;
import WizardTD.board.towers.Tower;
import WizardTD.entities.monsters.Monster;
import WizardTD.entities.projectiles.Fireball;
import WizardTD.game.events.Event;
import WizardTD.game.events.EventSchedule;
import WizardTD.infopanes.Player;

/**
 * The core class that handles all of the game logic and object interaction.
 */
public class Game implements Drawable {
    private Board board;
    private Player player;

    private int waveNumber;
    private int time;
    private int eventTime;

    private String gameState;

    PApplet window;

    private ArrayList<Tower> towers = new ArrayList<>();
    private ArrayList<Monster> monsters = new ArrayList<>();
    private EventSchedule schedule;

    /**
     * Initialise the game, setting defaults and creating a player for the game.
     * 
     * @param board the game board for this game.
     */
    public Game(Board board) {
        this.board = board;
        this.player = new Player(this);

        this.time = 0;
        this.eventTime = 0;
        this.waveNumber = 0;

        this.gameState = "ONGOING";

        Event.setGame(this);
        // this.schedule = schedule;
    }

    /**
     * Attach an event schedule to be following in this game.
     * 
     * @param schedule the event schedule for this game.
     */
    public void createEventSchedule(EventSchedule schedule) {
        this.schedule = schedule;
    }

    /**
     * Determine if the current player can afford and is able to build a tower on
     * the proposed tile, and if they are build the tower.
     * 
     * @param tile the tile upon which the new tower is proposed to be built.
     */
    public void buildTower(Tile tile) {
        if (player.getMana() > Tower.getBuildCost()) {
            if (player.isAlive()) {
                tile.buildTower(new Tower(tile.getXPixels(), tile.getYPixels()));
                if (tile.hasTower()) {
                    towers.add(tile.getTower());
                    player.purchaseUpgrade(Tower.getBuildCost());
                }
            }
        }

    }

    /**
     * @return An ArrayList of monsters that are currently attacking the player.
     */
    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    /**
     * Add a monster to the roster of monsters currently attacking the player.
     * 
     * @param monster the Monster to be added.
     */
    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    /**
     * Determine if the player can afford to upgrade the tower's range, and if so
     * upgrade the tower's range.
     * 
     * @param tower the tower to which the upgrade will be applied
     * @return A boolean indicating whether the upgrade was successful.
     */
    public boolean upgradeRange(Tower tower) {
        if (player.getMana() > tower.getRangeUpgradeCost()) {
            player.purchaseUpgrade(tower.getRangeUpgradeCost());
            tower.upgradeRange();
            return true;
        }
        return false;
    }

    /**
     * Determine if the player can afford to upgrade the tower's damage, and if so
     * upgrade the tower's damage.
     * 
     * @param tower the tower to which the upgrade will be applied
     * @return A boolean indicating whether the upgrade was successful.
     */
    public boolean upgradeDamage(Tower tower) {
        if (player.getMana() > tower.getDamageUpgradeCost()) {
            player.purchaseUpgrade(tower.getDamageUpgradeCost());
            tower.upgradeDamage();
            return true;
        }
        return false;
    }

    /**
     * Determine if the player can afford to upgrade the tower's firing speed, and
     * if so upgrade the tower's firing speed.
     * 
     * @param tower the tower to which the upgrade will be applied
     * @return A boolean indicating whether the upgrade was successful.
     */
    public boolean upgradeFiringSpeed(Tower tower) {
        if (player.getMana() > tower.getFiringSpeedUpgradeCost()) {
            player.purchaseUpgrade(tower.getFiringSpeedUpgradeCost());
            tower.upgradeFiringSpeed();
            return true;
        }
        return false;
    }

    /**
     * @return The count of the number of waves that have passed plus one.
     */
    public String getNextWaveNumber() {
        return "" + (waveNumber + 2);
    }

    /**
     * @return If there is a subsequent event, returns the number of seconds until
     *         that event begins.
     */
    public String getTimeUntilNextEvent() {
        if (waveNumber + 1 >= schedule.getNumberOfEvents()) {
            return "";
        } else {
            int displayTime = Math.round(((float) schedule.getEventDuration() - (float) eventTime) / 60);
            if (displayTime >= 0) {
                return "" + displayTime;
            } else {
                return "";
            }

        }
    }

    /**
     * @return The current game state.
     */
    public String getGameState() {
        return gameState;
    }

    /**
     * @return The number of ticks that have passed during the current event.
     */
    public int getEventTime() {
        return eventTime;
    }

    /**
     * @return The the boad object representing the board upon which this game is
     *         being played.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return The player object representing the player playing this game.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Increment internal timers.
     */
    public void tick() {
        eventTime++;
        time++;
    }

    /**
     * Modify the logic and position of drawable objects to simulate movement and
     * motion.
     */
    public void updateLogic() {
        if (!player.isAlive()) {
            gameState = "LOSE";
            return;
        } else if (player.isPaused()) {
            player.updateLogic();
            return;
        }

        if (waveNumber == schedule.getNumberOfEvents()) {
            if (monsters.size() == 0) {
                gameState = "WIN";
            }
        }

        tick();
        player.updateLogic();

        if (time % 60 == 0) {
            player.updateMana(player.getManaGainRate());
        }

        // System.out.println("Wav No: " + waveNumber + " Num Events: " +
        // schedule.getNumberOfEvents());
        if (eventTime > schedule.getPauseDuration() + schedule.getEventDuration()) {
            eventTime = 0;

            if (waveNumber < schedule.getNumberOfEvents()) {
                waveNumber++;
            }

            schedule.getNextEvent();
        }

        ArrayList<Monster> monstersToRemove = new ArrayList<>();
        for (Monster monster : monsters) {
            monster.updateLogic();

            if (monster.isAlive() == false) {
                monstersToRemove.add(monster);
            } else if (monster.attackedWizardHouse()) {
                player.updateMana(-1 * (int) monster.getHealth());

                monster.resetAttackedWizardHouse();
                monster.setPath(board.getRandomPath());
            }
        }

        for (Monster monster : monstersToRemove) {
            monsters.remove(monster);
            player.updateMana(monster.getManaGainOnKill());
        }

        for (Tower tower : towers) {
            tower.updateLogic();
            if (tower.getCurrentFireball() != null) {
                continue;
            }

            for (Monster monster : monsters) {
                double monsterTowerDistance = Math.abs(monster.getXPixels() - tower.getXPixels())
                        + Math.abs(monster.getYPixels() - tower.getYPixels());

                if (monsterTowerDistance <= tower.getRange()) {
                    tower.createFireball(new Fireball(tower, monster));
                    break;

                }
            }
        }
    }

    /**
     * Draw objects to the window.
     * 
     * @param app the PApplet representing the game window.
     */
    public void draw(PApplet app) {
        if (!player.isAlive()) {
            // gameState = "LOSE";
            player.draw(app);
            return;
        }

        board.draw(app);

        if (!player.isPaused()) {
            if (eventTime >= schedule.getNextPauseDuration()) {
                if (schedule.getEvent() != null) {
                    schedule.getEvent().action(app);
                }
            }
        }

        // Draw monstesr
        for (Monster monster : monsters) {
            monster.draw(app);
        }

        // Draw priority elements
        board.getWizardHouse().draw(app);

        for (Tower tower : towers) {
            tower.draw(app);
        }

        player.draw(app);
    }

}
