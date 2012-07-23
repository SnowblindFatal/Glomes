/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import java.util.HashMap;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author juho
 */
public class Statics {
    
    public static final String TITLE = "Glomes";
    public static final int FRAMERATE = 60, MENU_STATE = 0, GAME_STATE = 1;
    public static final float FLOOR_HEIGHT = 0f, WALL_HEIGHT  = 4f;
    public static float GAME_SPEED = 0.4f; //The global game speed setting. Affects delta Time which in turn should affect everything.
    
    //A hashmap containing all the game textures. Keys are the filenames and they are case sensitive.
    public static HashMap<String, Texture> textureMap = new HashMap();
    
    private static int DisplayWidth = 800, DisplayHeight = 600;
    private static final float pixelsPerUnit = 32f;
    
    
    
    
    
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
    
    public static float getTextureFactor(int textureDimension){
        return pixelsPerUnit / (float)textureDimension;
    }
    
    public static void setTextures(HashMap<String, Texture> newTextureMap){
        textureMap = newTextureMap;
    }
    
}
