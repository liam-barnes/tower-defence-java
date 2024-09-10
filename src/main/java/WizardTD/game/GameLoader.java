package WizardTD.game;

import java.util.ArrayList;

import WizardTD.board.towers.Tower;
import WizardTD.entities.monsters.MonsterType;
import WizardTD.game.events.EndlessEventSchedule;
import WizardTD.game.events.EventSchedule;
import WizardTD.game.events.WaveEvent;
import WizardTD.infopanes.Player;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class GameLoader {
    JSONObject config;

    private EventSchedule schedule = new EventSchedule();

    private String layoutPath;

    private float initialTowerRange;
    private float initialTowerFiringSpeed;
    private float initialTowerDamage;

    private int initialMana;
    private int initialManaCap;
    private int initialManaGainedPerSecond;

    private float towerCost;

    private float manaPoolSpellInitialCost;
    private float manaPoolSpellCostIncreasePerUse;
    private float manaPoolSpellCapMultiplier;
    private float manaPoolSpellManaGainedMultiplier;

    private JSONArray wavesArray;
    // private ArrayList<JSONObject> waveObjects

    /**
     * Retrives all preset values from the config file and sets all the default
     * values into the relevant classes as is appropriate.
     * 
     * @param config a JSONObject representing the config file for the game.
     */
    public GameLoader(JSONObject config) {
        this.config = config;

        this.layoutPath = config.getString("layout");

        // Load Constants
        this.initialTowerRange = config.getFloat("initial_tower_range");
        this.initialTowerFiringSpeed = config.getFloat("initial_tower_firing_speed");
        this.initialTowerDamage = config.getFloat("initial_tower_damage");
        this.towerCost = config.getFloat("tower_cost");
        Tower.setInitialProperties(initialTowerDamage, initialTowerRange, initialTowerFiringSpeed, towerCost);

        this.initialMana = config.getInt("initial_mana");
        this.initialManaCap = config.getInt("initial_mana_cap");
        this.initialManaGainedPerSecond = config.getInt("initial_mana_gained_per_second");

        this.manaPoolSpellInitialCost = config.getFloat("mana_pool_spell_initial_cost");
        this.manaPoolSpellCostIncreasePerUse = config.getFloat("mana_pool_spell_cost_increase_per_use");
        this.manaPoolSpellCapMultiplier = config.getFloat("mana_pool_spell_cap_multiplier");
        this.manaPoolSpellManaGainedMultiplier = config.getFloat("mana_pool_spell_mana_gained_multiplier");

        Player.setInitialProperties(initialMana, initialManaCap, initialManaGainedPerSecond, manaPoolSpellCapMultiplier,
                manaPoolSpellManaGainedMultiplier, manaPoolSpellInitialCost, manaPoolSpellCostIncreasePerUse);

        // Load Waves
        this.wavesArray = config.getJSONArray("waves");

        if (config.hasKey("endless")) {
            if (config.getBoolean("endless")) {
                schedule = new EndlessEventSchedule();
                return;
            }
        }

        // waves.size();
        for (int i = 0; i < wavesArray.size(); i++) {
            JSONObject currentWave = wavesArray.getJSONObject(i);

            // Get Monter Type Array
            ArrayList<MonsterType> monsters = new ArrayList<>();
            JSONArray waveMonstersArray = currentWave.getJSONArray("monsters");

            for (int j = 0; j < waveMonstersArray.size(); j++) {
                JSONObject currentMonster = waveMonstersArray.getJSONObject(j);

                String monsterRace = currentMonster.getString("type");
                float monsterMaxHealth = currentMonster.getFloat("hp");
                float monsterSpeed = currentMonster.getFloat("speed");
                float monsterArmour = currentMonster.getFloat("armour");
                float monsterManaGain = currentMonster.getFloat("mana_gained_on_kill");
                int monsterQuantity = currentMonster.getInt("quantity");

                monsters.add(
                        new MonsterType(monsterRace, monsterQuantity, monsterMaxHealth, monsterSpeed, monsterArmour,
                                monsterManaGain));
            }

            // Get details
            int pause = currentWave.getInt("pre_wave_pause");
            int duration = currentWave.getInt("duration");

            // create and store event
            schedule.scheduleEvent(new WaveEvent(pause, duration, monsters));
        }
        schedule.finaliseSchedule();
    }

    public EventSchedule getEventSchedule() {
        return schedule;
    }

    public String getLayoutPath() {
        return layoutPath;
    }

    public float getInitialTowerRange() {
        return initialTowerRange;
    }

    public float getInitialTowerFiringSpeed() {
        return initialTowerFiringSpeed;
    }

    public float getInitialTowerDamage() {
        return initialTowerDamage;
    }

    public float getInitialMana() {
        return initialMana;
    }

    public float getInitialManaCap() {
        return initialManaCap;
    }

    public float getInitialManaGainedPerSecond() {
        return initialManaGainedPerSecond;
    }

    public float getTowerCost() {
        return towerCost;
    }

    public float getManaPoolSpellInitialCost() {
        return manaPoolSpellInitialCost;
    }

    public float getManaPoolSpellCostIncreasePerUse() {
        return manaPoolSpellCostIncreasePerUse;
    }

    public float getManaPoolSpellCapMultiplier() {
        return manaPoolSpellCapMultiplier;
    }

    public float getManaPoolSpellManaGainedMultiplier() {
        return manaPoolSpellManaGainedMultiplier;
    }

}