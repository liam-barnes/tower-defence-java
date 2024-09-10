package WizardTD.infopanes;

import processing.core.PApplet;

import static WizardTD.utils.Dimensions.*;

public class Button {
    Player player;

    private String name;
    private String code;
    private String text;
    private boolean state = false;

    private static final int BUTTON_WIDTH = 49;
    private static final int BUTTON_HEIGHT = 50;

    private static final int xPos = BOARD_DIM * CELLSIZE + 5;;
    private int yPos;

    public Button(Player player, String name, String code, String text) {
        this.player = player;
        this.name = name;
        this.code = code;
        this.text = text;
    }

    public void setIndex(int index) {
        this.yPos = TOPBAR + 20 + 60 * index;
    }

    public void toggleState() {
        if (state) {
            state = false;
        } else {
            state = true;
        }
    }

    public boolean getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public static int getXPosition() {
        return xPos;
    }

    public int getYPosition() {
        return yPos;
    }

    public static int getButtonWidth() {
        return BUTTON_WIDTH;
    }

    public static int getButtonHeight() {
        return BUTTON_HEIGHT;
    }

    public void action() {
        switch (name) {
            case "FASTFORWARD":
                player.toggleFastForwardState();
                break;
            case "PAUSE":
                player.togglePauseState();
                break;
            case "BUILD TOWER":
                player.toggleBuildModeState();
                break;
            case "UPGRADE RANGE":
                player.toggleUpgradeRangeModeState();
                break;
            case "UPGRADE SPEED":
                player.toggleUpgradeSpeedModeState();
                break;
            case "UPGRADE DAMAGE":
                player.toggleUpgradeDamageModeState();
                break;
            case "MANA POOL SPELL":
                player.useManaPoolSpell();
                break;
        }
    }

    public void updateLogic() {
        return;
    }

    /**
     * Draw objects to the window.
     * 
     * @param app the PApplet representing the game window.
     */
    public void draw(PApplet app) {
        // Button Outline
        app.stroke(0);
        app.strokeWeight(2);

        if (state == true) {
            app.fill(254, 254, 6); // Yellow true
        } else {
            app.fill(132, 115, 74); // background false
        }

        app.rect(xPos, yPos, BUTTON_WIDTH, 50);

        // Button Text
        app.fill(0);
        app.textSize(35);
        app.text(code, xPos + 3, yPos + 40);

        // Button Description
        app.textSize(12);

        if (text.split(" ").length == 1) {
            app.text(text, xPos + 5 + BUTTON_WIDTH + 1, yPos + 30);
        } else {
            String[] splitText = text.split(" ");
            app.text(splitText[0], xPos + 5 + BUTTON_WIDTH + 1, yPos + 20);
            app.text(splitText[1], xPos + 5 + BUTTON_WIDTH + 1, yPos + 40);
        }
    }

    // public void draw(PApplet app, int index, String[] altText) {
    // app.stroke(0);
    // app.strokeWeight(2);
    // app.fill(132, 115, 74);
    // app.rect(xPos, yPos, BUTTON_WIDTH, BUTTON_HEIGHT);

    // // Button Text
    // app.fill(0);
    // app.textSize(35);
    // app.text(code, xPos + 3, TOPBAR + 20 + 40 + 60 * index);

    // // Button Description
    // app.textSize(12);
    // app.text(altText[0], BOARD_DIM * CELLSIZE + 10 + BUTTON_WIDTH + 1, TOPBAR +
    // 20 + 20 + 60 * index);
    // app.text(altText[1], BOARD_DIM * CELLSIZE + 10 + BUTTON_WIDTH + 1, TOPBAR +
    // 20 + 40 + 60 * index);
    // }

}
