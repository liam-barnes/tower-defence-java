package WizardTD.entities.monsters;

import java.util.Queue;
import java.util.LinkedList;

import WizardTD.board.Tile;
import WizardTD.entities.monsters.movement.Path;
import WizardTD.utils.Drawable;
import WizardTD.utils.ImageLoader;
import processing.core.PImage;
import processing.core.PApplet;

public class Monster extends MonsterType implements Drawable {
    private float health;
    private Path path;

    private int time;
    private float xPos;
    private float yPos;

    private Tile currentTile;
    private Tile nextTile;

    private String imagePath;
    private PImage sprite;

    private boolean alive;
    private boolean dying;
    private boolean attackedWizardHouse;

    private Queue<PImage> deathSequence = new LinkedList<>();

    private boolean travellingOnY;
    int axisMovementDirection = -1;

    /**
     * Links a monster type to a given path on the board to create a monster to
     * attack the player. The constructor initiates all of the required variables
     * for both attacking, death and traversal.
     * 
     * @param monsterType the characters of the new monster.
     * @param path        the path upon which the new monster will travel.
     */
    public Monster(MonsterType monsterType, Path path) {
        // int maxHealth, int speed, float armour, int manaGainOnKill
        // super(monsterType.getMaxHealth(), monsterType.getSpeed(),
        // monsterType.getArmour(),
        // monsterType.getManaGainOnKill());
        super(monsterType);
        this.health = monsterType.getMaxHealth();
        this.path = path;
        this.alive = true;
        this.dying = false;
        this.attackedWizardHouse = false;

        currentTile = path.moveToNextTile();
        nextTile = path.viewNextTile();

        xPos = currentTile.getXPixels();
        yPos = currentTile.getYPixels();

        checkDirection();
        determineMonsterImage();
        generateDeathSequence();

    }

    // public Monster(String monsterRace, int quantity, int maxHealth, int speed,
    // float armour, int manaGainOnKill,
    // Path path) {
    // super(monsterRace, quantity, maxHealth, speed, armour, manaGainOnKill);
    // this.health = super.getMaxHealth();
    // this.path = path;

    // currentTile = path.moveToNextTile();
    // nextTile = path.viewNextTile();

    // xPos = currentTile.getXPixels();
    // yPos = currentTile.getYPixels();

    // checkDirection();
    // determineMonsterImage();
    // generateDeathSequence();
    // }

    /**
     * Retrives and stores the images required for the monsters death animation.
     */
    public void generateDeathSequence() {
        // If other graphics are available this can be adapted to be more specific for
        // other sprites
        for (int i = 1; i < 4; i++) {
            deathSequence.add(ImageLoader.getImage("src/main/resources/WizardTD/gremlin" + i + ".png"));
        }
    }

    // public void adjustInitialPosition(int adjustment) {
    // if (axisMovementDirection == -1) {
    // yPos = yPos - adjustment;
    // } else {
    // xPos = xPos - adjustment;
    // }
    // }

    /**
     * Retrives and stores the appropriate monster image based on the monsterRace
     * string.
     */
    private void determineMonsterImage() {
        if (super.getMonsterRace().equals("beetle")) {
            this.imagePath = "src/main/resources/WizardTD/beetle.png";
        } else if (super.getMonsterRace().equals("worm")) {
            this.imagePath = "src/main/resources/WizardTD/worm.png";
        } else {
            this.imagePath = "src/main/resources/WizardTD/gremlin.png";
        }

        this.sprite = ImageLoader.getImage(imagePath);
    }

    /**
     * Stores a PImage that will be used to represent the monster on the map.
     * 
     * @param spriteImage the PImage to represent the monster.
     */
    public void setSpriteImage(PImage spriteImage) {
        this.sprite = spriteImage;
    }

    /**
     * Retrive the current x-axis location of the top left-hand corner of the
     * monster
     * 
     * @return The float of the number of pixels rightward along the x-axis.
     */
    public float getXPixels() {
        return xPos;
    }

    /**
     * Retrive the current y-axis location of the top left-hand corner of the
     * monster
     * 
     * @return The float of the number of pixels downward along the y-axis.
     */
    public float getYPixels() {
        return yPos;
    }

    /**
     * @return The current health of the monster.
     */
    public float getHealth() {
        return health;
    }

    /**
     * Retrives the monster's sprite.
     * 
     * @return a PImage containing the monster sprite.
     */
    public PImage getSpriteImage() {
        return sprite;
    }

    /**
     * @return A boolean that notes whether the monster has yet reached the wizard
     *         house.
     */
    public boolean attackedWizardHouse() {
        return attackedWizardHouse;
    }

    /**
     * To be called when the monster has reached the wizard house and been bannished
     * to a startime tile
     * in order to allow the monster to attack the wizard house again.
     */
    public void resetAttackedWizardHouse() {
        attackedWizardHouse = false;
    }

    /**
     * Stores the monsters path and sets the inital values such that the monster can
     * traverse
     * the path.
     * 
     * @param path the path upon which the monster will travel
     */
    public void setPath(Path path) {
        this.path = path;

        currentTile = path.moveToNextTile();
        nextTile = path.viewNextTile();

        // health = super.getMaxHealth();

        xPos = currentTile.getXPixels();
        yPos = currentTile.getYPixels();

        checkDirection();
    }

    /**
     * Updates the monsters health in response to being hit by a projecctile
     * and determine whether the monster is still alive.
     * 
     * @param damage a float value that describes the quantum of damage to be taken
     *               by the monster.
     */
    public void damageMonster(float damage) {
        health = health - damage;
        if (health <= 0) {
            dying = true;
        }
    }

    /**
     * @return boolean of whether the monster is alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Determines the direction of movement for the monster.
     */
    private void checkDirection() {
        if (currentTile.getYLocation() - nextTile.getYLocation() != 0) {
            travellingOnY = true;

            if (currentTile.getYLocation() - nextTile.getYLocation() > 0) {
                axisMovementDirection = -1;
            } else {
                axisMovementDirection = 1;
            }

        } else {
            travellingOnY = false;

            if (currentTile.getXLocation() - nextTile.getXLocation() > 0) {
                axisMovementDirection = -1;
            } else {
                axisMovementDirection = 1;
            }
        }
    }

    /**
     * Determines the direction and scale of movement for the monster and updates
     * the monster's position accordinly.
     * This function is called recursively where the movement needs to span multiple
     * tiles/directions.
     * 
     * @param remainingMovement a float of the amount the monster still needs to be
     *                          moved by.
     */
    public void move(float remainingMovement) {
        if (nextTile == null) {
            alive = false;
            return;
        }

        remainingMovement = remainingMovement * axisMovementDirection;

        if (travellingOnY) {
            if (Math.abs(nextTile.getYPixels() - yPos) < Math.abs(remainingMovement)) {
                float residual = Math.abs(remainingMovement) - Math.abs((nextTile.getYPixels()) - yPos);
                yPos = nextTile.getYPixels();

                currentTile = path.moveToNextTile();
                nextTile = path.viewNextTile();

                if (nextTile != null) {
                    checkDirection();
                    move(residual);
                }

            } else {
                yPos += remainingMovement;
            }
        } else {
            if (Math.abs(nextTile.getXPixels() - xPos) < Math.abs(remainingMovement)) {
                float residual = Math.abs(remainingMovement) - Math.abs((nextTile.getXPixels()) - xPos);
                xPos = nextTile.getXPixels();

                currentTile = path.moveToNextTile();
                nextTile = path.viewNextTile();

                if (nextTile != null) {
                    checkDirection();
                    move(residual);
                }

            } else {
                xPos += remainingMovement;
            }
        }
    }

    /**
     * Modify the logic and position of drawable objects to simulate movement and
     * motion.
     */
    public void updateLogic() {
        time++;

        if (nextTile == null) {
            attackedWizardHouse = true;
            return;
        }

        if (dying) {
            if (deathSequence.size() != 0) {
                if (time % 4 == 0) {
                    setSpriteImage(deathSequence.poll());
                }
                return;
            } else {
                alive = false;
            }

        }

        if (!alive) {
            return;
        }

        move(super.getSpeed());
    }

    /**
     * Draw objects to the window.
     * 
     * @param app the PApplet representing the game window.
     */
    public void draw(PApplet app) {
        if (nextTile == null) {
            // // GEN NEW PATH
            // currentTile = path.moveToNextTile();
            // nextTile = path.viewNextTile();

            // xPos = currentTile.getXPixels();
            // yPos = currentTile.getYPixels();

            // checkDirection();

            return;
        }

        if (alive) {
            app.image(sprite, xPos + 5, yPos + 4);
            app.noStroke();
            app.fill(204, 27, 0);
            app.rect(xPos + 8, yPos - 3, 16, (float) 2);
            app.fill(0, 204, 41);
            app.rect(xPos + 8, yPos - 3, (health / super.getMaxHealth()) * 16, (float) 2);
        }
    }
}
