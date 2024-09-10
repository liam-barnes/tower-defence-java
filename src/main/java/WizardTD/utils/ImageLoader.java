package WizardTD.utils;

import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageLoader {
    private static HashMap<String, PImage> images = new HashMap<>();
    private PApplet app;

    private static String[] imagePaths = { "src/main/resources/WizardTD/beetle.png",
            "src/main/resources/WizardTD/fireball.png", "src/main/resources/WizardTD/grass.png",
            "src/main/resources/WizardTD/gremlin.png", "src/main/resources/WizardTD/gremlin1.png",
            "src/main/resources/WizardTD/gremlin2.png", "src/main/resources/WizardTD/gremlin3.png",
            "src/main/resources/WizardTD/gremlin4.png", "src/main/resources/WizardTD/gremlin5.png",
            "src/main/resources/WizardTD/path0.png", "src/main/resources/WizardTD/path1.png",
            "src/main/resources/WizardTD/path2.png", "src/main/resources/WizardTD/path3.png",
            "src/main/resources/WizardTD/shrub.png", "src/main/resources/WizardTD/tower0.png",
            "src/main/resources/WizardTD/tower1.png", "src/main/resources/WizardTD/tower2.png",
            "src/main/resources/WizardTD/wizard_house.png", "src/main/resources/WizardTD/worm.png" };

    /**
     * Loads all of the images into the currrent game for the first time.
     * 
     * @param app the PApplet representing the game.
     */
    public ImageLoader(PApplet app) {
        this.app = app;

        reloadImages();
    }

    /**
     * Reload images from there file paths.
     */
    public void reloadImages() {
        for (int i = 0; i < imagePaths.length; i++) {
            images.put(imagePaths[i], app.loadImage(imagePaths[i]));
        }
    }

    /**
     * Generate empty images to prevent crashing even if certain game images cannot
     * load.
     */
    public static void generateEmptyImages() {
        for (int i = 0; i < imagePaths.length; i++) {
            images.put(imagePaths[i], new PImage(0, 0));
        }
    }

    /**
     * Retrive an image from all of the images that have been loaded into the game.
     * 
     * @param path a String of the relative file path to the image which acts as its
     *             identifier within this class.
     * @return A PImage object of the selected image.
     */
    public static PImage getImage(String path) {
        return images.get(path);
    }

}
