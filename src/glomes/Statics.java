/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

/**
 *
 * @author juho
 */
public class Statics {
    
    public static final String TITLE = "Glomes";
    public static final int FRAMERATE = 60, MENU_STATE = 0, GAME_STATE = 1;
    public static final float FLOOR_HEIGHT = 0f, WALL_HEIGHT  = 20f;
    
    private static int DisplayWidth = 800, DisplayHeight = 600;
    
    
    
    public static int getDisplayWidth() {
        return DisplayWidth;
    }
    public static int getDisplayHeight() {
        return DisplayHeight;
    }
    public static void setResolution(int aDisplayWidth, int aDisplayHeight) {
        DisplayWidth = aDisplayWidth;
        DisplayHeight = aDisplayHeight;
    }
    public static int[] getResolution() {
        return new int[] {DisplayWidth, DisplayHeight};
    }
    
}
