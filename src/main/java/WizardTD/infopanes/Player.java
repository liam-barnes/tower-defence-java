package WizardTD.infopanes;

import WizardTD.board.towers.Tower;
import WizardTD.game.Game;
import WizardTD.utils.Drawable;
import processing.core.PApplet;

public class Player implements Drawable {
    private static int initialMana;
    private static int initialManaCap;
    private static int initialManaGainRate;
    private static float initialManaGainMultiplier;
    private static float initialManaCapMultiplier;
    private static float initialManaPoolCost;
    private static float initialManaPoolCostIncrease;

    private boolean pause = false;
    private boolean fastForward = false;
    private boolean buildMode = false;
    private boolean upgradeSpeedMode = false;
    private boolean upgradeRangeMode = false;
    private boolean upgradeDamageMode = false;

    private boolean alive = false;
    private float mana = 1000;
    private float manaCap = 1050;
    private float manaGainRate = 3;
    private float manaGainMultiplier = (float) 1.1;
    private float manaCapMultiplier = (float) 1.25;
    private float manaPoolCost = 100;
    private float manaPoolCostIncrease = 150;

    private SideBar sideBar;
    private TopBar topBar;

    private UpgradeDisplay currentUpgradeDisplay = null;
    private ToolTip toolTip = null;

    public Player(Game game) {
        sideBar = new SideBar(this);
        this.topBar = new TopBar(game, this);

        mana = initialMana;
        manaCap = initialManaCap;
        manaGainRate = initialManaGainRate;
        manaCapMultiplier = initialManaCapMultiplier;
        manaGainMultiplier = initialManaGainMultiplier;

        manaPoolCost = initialManaPoolCost;
        manaPoolCostIncrease = initialManaPoolCostIncrease;

        alive = true;
    }

    /**
     * Set initial values for all future players.
     * 
     * @param iMana                 the initial amount of mana.
     * @param iManaCap              the initial maximum amount of mana.
     * @param iManaGainRate         the rate at which a player naturally gains mana.
     * @param iManaGainMultiplier   the increase multiplier in mana gain due to the
     *                              use of the mana pool spell.
     * @param iManaCapMultiplier    the increase in the mana cap due to the use of
     *                              the mana pool spell.
     * @param iManaPoolCost         the intial cost of using the mana pool spell.
     * @param iManaPoolCostIncrease the increase in the cost of using the mana pool
     *                              spell after every use.
     */
    public static void setInitialProperties(int iMana, int iManaCap, int iManaGainRate, float iManaGainMultiplier,
            float iManaCapMultiplier, float iManaPoolCost, float iManaPoolCostIncrease) {
        initialMana = iMana;
        initialManaCap = iManaCap;
        initialManaGainRate = iManaGainRate;
        initialManaCapMultiplier = iManaCapMultiplier;
        initialManaGainMultiplier = iManaGainMultiplier;
        initialManaPoolCost = iManaPoolCost;
        initialManaPoolCostIncrease = iManaPoolCostIncrease;
    }

    /**
     * @return A boolean indicating whether a tooltip is being shown.
     */
    public boolean showingToolTip() {
        if (toolTip != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create new tooltip to show the cost of the action of the button.
     * 
     * @param button the button overwhich the player's mouse is hovering
     */
    public void showToolTip(Button button) {
        toolTip = new ToolTip(this, button);
    }

    /**
     * Send the currentUpgradeDisplay to the garbage collector and prepare for the
     * next one.
     */
    public void hideToolTip() {
        toolTip = null;
    }

    /**
     * @return a boolean indicating whether an upgrade display is being shown.
     */
    public boolean showingUpgradeDisplay() {
        if (currentUpgradeDisplay != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create new upgrade dispaly for the tower over which the player's mouse
     * hovers.
     * 
     * @param tower the tower to be upgraded.
     */
    public void showUpgradeDisplay(Tower tower) {
        currentUpgradeDisplay = new UpgradeDisplay(this, tower);
    }

    /**
     * Send the currentUpgradeDisplay to the garbage collector and prepare for the
     * next one.
     */
    public void hideUpgradeDisplay() {
        currentUpgradeDisplay = null;
    }

    public boolean isAlive() {
        return alive;
    }

    /**
     * Exposes all of the sidebar buttons.
     * 
     * @return An array of buttons.
     */
    public Button[] getButtons() {
        return sideBar.getButtons();
    }

    public float getMana() {
        return mana;
    }

    public float getManaCap() {
        return manaCap;
    }

    public float getManaGainRate() {
        return manaGainRate;
    }

    public float getManaPoolCost() {
        return manaPoolCost;
    }

    /**
     * Adjusts the players mana including the calculation of many multipliers. Kills
     * the player if the mana is reduced below 0. Prevents the mana from
     * exceeding the mana cap.
     * 
     * @param adjustment the amount (positive or negative) that the players mana
     *                   will be adjusted by before the application of any
     *                   multipliers.
     */
    public void updateMana(float adjustment) {
        if (adjustment > 0) {
            mana = mana + (adjustment * manaGainMultiplier);

        } else {
            mana = mana + adjustment;
        }

        if (mana <= 0) {
            mana = 0;
            alive = false;
        } else if (mana > manaCap) {
            mana = manaCap;
        }
    }

    /**
     * If the player can purchase the ugrade, adjust the players mana and return
     * true.
     * 
     * @param cost the cost of the proposed upgrade.
     * @return a boolean indicating whether or not the player has successfully
     *         purchased the upgrade.
     */
    public boolean purchaseUpgrade(float cost) {
        if (mana > cost) {
            mana = mana - cost;
            return true;
        } else {
            return false;
        }
    }

    public boolean isFastforwarding() {
        return fastForward;
    }

    public boolean isPaused() {
        return pause;
    }

    public boolean isBuilding() {
        return buildMode;
    }

    public boolean isUpgradingSpeed() {
        return upgradeSpeedMode;
    }

    public boolean isUpgradingDamage() {
        return upgradeDamageMode;
    }

    public boolean isUpgradingRange() {
        return upgradeRangeMode;
    }

    /**
     * Update fastforward button and invert the state of upgrade fastforward mode.
     */
    public void toggleFastForwardState() {
        sideBar.getButtons()[0].toggleState();
        if (fastForward) {
            fastForward = false;
        } else {
            fastForward = true;
        }
    }

    /**
     * Update pause button and invert the state of upgrade pause mode.
     */
    public void togglePauseState() {
        sideBar.getButtons()[1].toggleState();
        if (pause) {
            pause = false;
        } else {
            pause = true;
        }
    }

    /**
     * Update tower build button and invert the state of build mode.
     */
    public void toggleBuildModeState() {
        sideBar.getButtons()[2].toggleState();
        if (buildMode) {
            buildMode = false;
        } else {
            buildMode = true;
        }
    }

    /**
     * Update range and invert the state of upgrade range mode.
     */
    public void toggleUpgradeRangeModeState() {
        sideBar.getButtons()[3].toggleState();
        if (upgradeRangeMode) {
            upgradeRangeMode = false;
        } else {
            upgradeRangeMode = true;
        }
    }

    /**
     * Update speed button and invert the state of upgrade speed mode.
     */
    public void toggleUpgradeSpeedModeState() {
        sideBar.getButtons()[4].toggleState();
        if (upgradeSpeedMode) {
            upgradeSpeedMode = false;
        } else {
            upgradeSpeedMode = true;
        }
    }

    /**
     * Update damage button and invert the state of upgrade damage mode.
     */
    public void toggleUpgradeDamageModeState() {
        sideBar.getButtons()[5].toggleState();
        if (upgradeDamageMode) {
            upgradeDamageMode = false;
        } else {
            upgradeDamageMode = true;
        }
    }

    /**
     * If the player can successfully purchase the pool spell, update the mana cap
     * and mana gain rate and increase the future cost of the spell.
     */
    public void useManaPoolSpell() {
        if (purchaseUpgrade(manaPoolCost)) {
            manaPoolCost = manaPoolCost + manaPoolCostIncrease;
            manaCap = manaCap * manaCapMultiplier;
            manaGainRate = manaGainRate * manaGainMultiplier;
        }
    }

    /**
     * Modify the logic and position of drawable objects to simulate movement and
     * motion.
     */
    public void updateLogic() {
        if (currentUpgradeDisplay != null) {
            currentUpgradeDisplay.updateLogic();
        }
    }

    /**
     * Draw objects to the window.
     * 
     * @param app the PApplet representing the game window.
     */
    public void draw(PApplet app) {
        sideBar.draw(app);
        topBar.draw(app);

        if (currentUpgradeDisplay != null) {
            currentUpgradeDisplay.draw(app);
        }

        if (toolTip != null) {
            toolTip.draw(app);
        }

    }
}
