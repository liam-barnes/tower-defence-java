package WizardTD.infopanes;

import WizardTD.board.towers.Tower;
import WizardTD.utils.Drawable;
import static WizardTD.utils.Dimensions.*;

import java.util.ArrayList;

import processing.core.PApplet;

public class UpgradeDisplay implements Drawable {
    // private Player player;
    // private Tower tower;

    private float total;

    private boolean show;

    private ArrayList<String> upgrades = new ArrayList<>();

    /**
     * Initalises the upgrade display determining which upgrades the display should
     * show and calculating the total cost of these upgrades
     * 
     * @param player an object Player representing the player playing the current
     *               game.
     * @param tower  the Tower over which the player's mouse is hovering.
     */
    public UpgradeDisplay(Player player, Tower tower) {
        // this.player = player;
        // this.tower = tower;

        if (player.isUpgradingDamage() || player.isUpgradingSpeed() || player.isUpgradingRange()) {
            this.show = true;
        } else {
            this.show = false;
        }

        if (player.isUpgradingDamage()) {
            upgrades.add("Damage: " + tower.getDamageUpgradeCost());
            total += tower.getDamageUpgradeCost();
        }

        if (player.isUpgradingSpeed()) {
            upgrades.add("Speed: " + tower.getFiringSpeedUpgradeCost());
            total += tower.getFiringSpeedUpgradeCost();
        }

        if (player.isUpgradingRange()) {
            upgrades.add("Range: " + tower.getRangeUpgradeCost());
            total += tower.getRangeUpgradeCost();
        }

    }

    public void updateLogic() {
        // do something
    }

    /**
     * Draw objects to the window.
     * 
     * @param app the PApplet representing the game window.
     */
    public void draw(PApplet app) {
        if (!show) {
            return;
        }
        int flexibleSize = (upgrades.size() - 1) * 20;

        app.fill(255);
        app.strokeWeight(1);
        app.rect(WIDTH - SIDEBAR + 10, HEIGHT - 150, 100, 60 + flexibleSize);

        // BARS
        // app.strokeWeight(2);
        app.fill(0);
        app.textSize(13);
        app.text("Upgrade Cost", WIDTH - SIDEBAR + 10 + 2, HEIGHT - 130 - 3);

        app.textSize(11);
        // if (upgrades.size())
        for (int i = 0; i < upgrades.size(); i++) {
            app.text(upgrades.get(i), WIDTH - SIDEBAR + 10 + 2, HEIGHT - 110 - 3 + (20 * i));
        }

        app.fill(255);
        app.rect(WIDTH - SIDEBAR + 10, HEIGHT - 130, 100, 1);

        app.rect(WIDTH - SIDEBAR + 10, HEIGHT - 110 + flexibleSize, 100, 1);

        app.fill(0);
        app.text("Total: " + total, WIDTH - SIDEBAR + 10 + 2, HEIGHT - 110 + flexibleSize + 15);

    }

}
