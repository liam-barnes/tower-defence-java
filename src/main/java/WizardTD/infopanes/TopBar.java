package WizardTD.infopanes;

import processing.core.PApplet;

import static WizardTD.utils.Dimensions.*;

import WizardTD.game.Game;

public class TopBar {
    Player player;
    Game game;

    /**
     * Loads high-level classes from which the characteristics which will be
     * displayed are obtained.
     * 
     * @param game   the current game.
     * @param player the current player.
     */
    public TopBar(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public void updateLogic() {

    }

    /**
     * Draw objects to the window.
     * 
     * @param app the PApplet representing the game window.
     */
    public void draw(PApplet app) {
        app.noStroke();
        app.fill(132, 115, 74);
        app.rect(0, 0, WIDTH, TOPBAR);

        app.fill(0);
        app.textSize(20);
        app.text("Wave " + game.getNextWaveNumber() + " starts: " + game.getTimeUntilNextEvent(), LEFT_PADDING,
                (TOPBAR / 10) * 7);

        app.stroke(0);
        app.strokeWeight(2);
        app.fill(255);
        app.rect(400, 10, 315, 17);

        app.fill(2, 214, 214);
        app.rect(400, 10, ((float) player.getMana() / (float) player.getManaCap()) * 315, 17);

        app.fill(0);
        app.textSize(15);

        String manaRatioText = player.getMana() + " / " + player.getManaCap();
        app.text(manaRatioText, 500, (TOPBAR / 10) * 6);

        app.textSize(20);
        app.text("MANA:", 330, (TOPBAR / 10) * 7);
    }

}
