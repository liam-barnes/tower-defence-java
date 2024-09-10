package WizardTD.utils;

public class Dimensions {

    public static final int FPS = 60;

    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_DIM = 20;

    public static final int BOARD_WIDTH = BOARD_DIM;

    public static final int WIDTH = CELLSIZE * BOARD_WIDTH + SIDEBAR;
    public static final int HEIGHT = BOARD_WIDTH * CELLSIZE + TOPBAR;

    public static final int LEFT_PADDING = 5;
}
