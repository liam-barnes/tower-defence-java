package WizardTD.board.towers;

import WizardTD.utils.Drawable;
import WizardTD.utils.ImageLoader;
import processing.core.PApplet;
import processing.core.PImage;

import WizardTD.entities.projectiles.Fireball;

import static WizardTD.utils.Dimensions.*;

import java.util.Stack;

public class Tower implements Drawable {
    private static float initialDamage = 10;
    private static float initialRange = 32;
    private static float initialFiringSpeed = 3;
    private static float buildCost = 100;

    private float range;
    private float firingSpeed;
    private float damage;

    private int rangeLevel;
    private int damageLevel;
    private int firingSpeedLevel;

    private int xPixels;
    private int yPixels;

    private int time = 0;

    private boolean displayRange = false;

    private Stack<Fireball> fireball = new Stack<>();

    private PImage towerImage;

    /**
     * Initialises the tower by setting initial values and loading the basic tower
     * image.
     * 
     * @param xPixels the x position of the tower in the window in pixels.
     * @param yPixels the y position of the tower in the window in pixels.
     */
    public Tower(int xPixels, int yPixels) {
        this.range = initialRange;
        this.firingSpeed = initialFiringSpeed;
        this.damage = initialDamage;

        rangeLevel = 0;
        damageLevel = 0;
        firingSpeedLevel = 0;

        this.xPixels = xPixels;
        this.yPixels = yPixels;

        this.towerImage = ImageLoader.getImage("src/main/resources/WizardTD/tower0.png");
    }

    /**
     * Set initial values for all future towers.
     * 
     * @param iDamage        the initial damage the tower's fireballs inflict.
     * @param iRange         the initial maximum distance from which the tower can
     *                       shoot fireballs.
     * @param iSpeed         the initial rate at which the tower shoots fireballs.
     * @param towerBuildCost the initial cost of building a tower.
     */
    public static void setInitialProperties(float iDamage, float iRange, float iSpeed, float towerBuildCost) {
        initialDamage = iDamage;
        initialRange = iRange;
        initialFiringSpeed = iSpeed;
        buildCost = towerBuildCost;
    }

    /**
     * The X position of the tower in pixels.
     * 
     * @return int of pixels rightward.
     */
    public int getXPixels() {
        return xPixels;
    }

    /**
     * The Y position of the tower in pixels.
     * 
     * @return int of pixels downard.
     */
    public int getYPixels() {
        return yPixels;
    }

    /**
     * @return a PImage object of the tower's current image.
     */
    public PImage getTowerImage() {
        return towerImage;
    }

    /**
     * @return A float denoting the current range.
     */
    public float getRange() {
        return range;
    }

    /**
     * @return A float denoting the current firing speed.
     */
    public float getFiringSpeed() {
        return firingSpeed;
    }

    /**
     * @return A float denoting amount of damage the tower currently inflicts on
     *         monster's that its fireballs hit.
     */
    public float getDamage() {
        return damage;
    }

    /**
     * @return Either the default or config specified cost of building a tower.
     */
    public static float getBuildCost() {
        return buildCost;
    }

    /**
     * @return The current cost of upgrading the tower's range.
     */
    public int getRangeUpgradeCost() {
        return 10 + (rangeLevel + 1) * 10;
    }

    /**
     * @return The current cost of upgrading the tower's damage.
     */
    public int getDamageUpgradeCost() {
        return 10 + (damageLevel + 1) * 10;
    }

    /**
     * @return The current cost of upgrading the tower's firing speed.
     */
    public int getFiringSpeedUpgradeCost() {
        return 10 + (firingSpeedLevel + 1) * 10;
    }

    public int getRangeLevel() {
        return rangeLevel;
    }

    public int getDamageLevel() {
        return damageLevel;
    }

    public int getSpeedLevel() {
        return firingSpeedLevel;
    }

    private int minUpgradeLevel = 0;
    private int firingSpeedSquares = 0;
    private int damageXs = 0;
    private int rangeOs = 0;

    /**
     * Determines the decorations that need to be added to the tower to indicate its
     * current upgrades.
     */
    public void calculateLevelIdentifiers() {
        if (minUpgradeLevel <= 2) {
            firingSpeedSquares = firingSpeedLevel - minUpgradeLevel;
            damageXs = damageLevel - minUpgradeLevel;
            rangeOs = rangeLevel - minUpgradeLevel;
        } else {
            firingSpeedSquares = firingSpeedLevel - 2;
            damageXs = damageLevel - 2;
            rangeOs = rangeLevel - 2;
        }

    }

    /**
     * Determines the appropriate image for the tower based on the tower's current
     * upgrade levels.
     */
    public void determineTowerImage() {
        minUpgradeLevel = Math.min(rangeLevel, Math.min(firingSpeedLevel, damageLevel));

        if (minUpgradeLevel == 1) {
            setTowerImage(ImageLoader.getImage("src/main/resources/WizardTD/tower1.png"));
        } else if (minUpgradeLevel == 2) {
            setTowerImage(ImageLoader.getImage("src/main/resources/WizardTD/tower2.png"));
        }
    }

    /**
     * Increases the distance from which a tower can shoot a fireball at a monster.
     */
    public void upgradeRange() {
        range = range + 32;
        rangeLevel++;

        determineTowerImage();
        calculateLevelIdentifiers();
    }

    /**
     * Increases the rate at which the tower shoots fireballs.
     */
    public void upgradeFiringSpeed() {
        firingSpeed = firingSpeed + 1; // CORRECT TO PROPER UPGRADE
        firingSpeedLevel++;

        determineTowerImage();
        calculateLevelIdentifiers();
    }

    /**
     * Increates the amount of damage inflicted the fireballs that hit monsters.
     */
    public void upgradeDamage() {
        damage += initialDamage / 2;
        damageLevel++;

        determineTowerImage();
        calculateLevelIdentifiers();
    }

    /**
     * Sets and stores an image for the tower.
     * 
     * @param towerImage an image for the tower.
     */
    public void setTowerImage(PImage towerImage) {
        this.towerImage = towerImage;
    }

    /**
     * @return Whether the tower is currently displaying the yellow circle
     *         indicating its range.
     */
    public boolean getDisplayRangeState() {
        return displayRange;
    }

    /**
     * Enables/disables the yellow range indicating the range of the tower.
     */
    public void toggleDisplayRange() {
        if (displayRange) {
            displayRange = false;
        } else {
            displayRange = true;
        }
    }

    /**
     * Increments the internal tower time by 1
     */
    private void tick() {
        time++;
    }

    /**
     * Removes the current fireball from the stack in order to prepare to generate
     * the next fireball.
     * 
     * @return The current fireball.
     */
    public Fireball nextFireball() {
        return fireball.pop();
    }

    /**
     * @return The current fireball.
     */
    public Fireball getCurrentFireball() {
        if (fireball.size() != 0) {
            return fireball.peek();
        } else {
            return null;
        }
    }

    /**
     * If the tower does not currently have a Fireball, a new Fireball will be added
     * to attack monsters.
     * 
     * @param newFireball the next fireball this tower will use to attack.
     */
    public void createFireball(Fireball newFireball) {
        if (fireball.size() == 0) {
            fireball.add(newFireball);
        } else {
            return;
        }
    }

    /**
     * Modify the logic and position of drawable objects to simulate movement and
     * motion.
     */
    public void updateLogic() {
        tick();

        if (getCurrentFireball() != null) {
            if (getCurrentFireball().isAttacking() == true) {
                getCurrentFireball().updateLogic();
                // time = 0;

            } else {
                if (time > 60) {
                    nextFireball();
                    time = 0;
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
        app.image(getTowerImage(), xPixels, yPixels);

        // Tower Modifications
        if (rangeOs != 0) {
            for (int i = 0; i < Math.min(rangeOs, 6); i++) {
                app.fill(204, 27, 0);
                app.textSize(8);
                app.text('O', xPixels + (5 * i), yPixels + 5);
            }
        }

        if (damageXs != 0) {
            for (int i = 0; i < Math.min(damageXs, 6); i++) {
                app.fill(204, 27, 0);
                app.textSize(8);
                app.text('X', xPixels + (5 * i), yPixels + 32);
            }
        }

        if (firingSpeedSquares != 0) {
            app.noFill();
            app.strokeWeight(Math.min(1 + (firingSpeedSquares * (float) 0.5), 4));
            app.stroke(124, 180, 255);
            app.rect(xPixels + 5, yPixels + 5, 21, 21);
        }

        if (displayRange) {
            app.noFill();
            app.strokeWeight(2);
            app.stroke(256, 231, 58);
            app.ellipse(xPixels + CELLSIZE / 2, yPixels + CELLSIZE / 2, range * 2, range * 2);
        }

        if (getCurrentFireball() != null) {
            getCurrentFireball().draw(app);
        }
    }
}
