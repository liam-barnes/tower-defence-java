package WizardTD.entities.projectiles;

import WizardTD.board.towers.Tower;
import WizardTD.entities.monsters.Monster;

import WizardTD.utils.Drawable;
import processing.core.PApplet;
import processing.core.PImage;

import static WizardTD.utils.Dimensions.*;

public class Fireball implements Drawable {
    private static PImage fireballImage;

    private Tower originTower;
    private Monster targetMonster;

    private double xPos;
    private double yPos;

    private double speed = 7;

    private boolean attacking;

    /**
     * Initalise a new fireball to prepare it to begin attacking a monster.
     * 
     * @param originTower   the tower from which the Fireball is fired.
     * @param targetMonster the monster the Fireball is trying to hit.
     */
    public Fireball(Tower originTower, Monster targetMonster) {
        this.originTower = originTower;
        this.targetMonster = targetMonster;

        this.speed = originTower.getFiringSpeed();

        xPos = (double) originTower.getXPixels() + 16;
        yPos = (double) originTower.getYPixels() + 16;

        this.attacking = true;
    }

    /**
     * Seta an image for all fireballs that will attack monsters throughout the
     * game.
     * 
     * @param fbImage the sprite for the fireball.
     */
    public static void setFireballImage(PImage fbImage) {
        fireballImage = fbImage;
    }

    /**
     * @return a boolean indicating whether the fireball is currently attempting to
     *         damage a monster.
     */
    public boolean isAttacking() {
        return attacking;
    }

    /**
     * Modify the logic and position of drawable objects to simulate movement and
     * motion.
     */
    public void updateLogic() {
        if (xPos > targetMonster.getXPixels() && xPos < targetMonster.getXPixels() + 32) {
            if (yPos > targetMonster.getYPixels() && yPos < targetMonster.getYPixels() + 32) {
                attacking = false;
                targetMonster.damageMonster(originTower.getDamage());
            }

            if (xPos < 0 || yPos < 0 || xPos > BOARD_WIDTH || yPos > HEIGHT) {
                attacking = false;
            }
        }
        double angleAdjustment = 0;
        double convertRad = Math.PI / 180;

        if (yPos - targetMonster.getYPixels() < 0) {
            if (xPos - targetMonster.getXPixels() <= 0) {
                // angleAdjustment = 0;
            } else {
                angleAdjustment = 90 * convertRad;
            }
        } else {
            if (xPos - targetMonster.getXPixels() < 0) {
                angleAdjustment = 270 * convertRad;
            } else {
                angleAdjustment = 180 * convertRad;
            }
        }

        double oppositeLength = Math.abs(targetMonster.getYPixels() - yPos);
        double adjacentLength = Math.abs(targetMonster.getXPixels() - xPos);
        double angle = Math.atan(oppositeLength / adjacentLength) + angleAdjustment;

        // System.out.println(angle * 180 / Math.PI);

        xPos = xPos + Math.cos(angle) * speed;
        yPos = yPos + Math.sin(angle) * speed;
    }

    /**
     * Draw objects to the window.
     * 
     * @param app the PApplet representing the game window.
     */
    public void draw(PApplet app) {
        // add check for if in contact with the target monster)
        if (attacking) {
            app.image(fireballImage, (float) xPos, (float) yPos);
        }

    }

}
