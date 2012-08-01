/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import maps.Map;
import maps.MapData;
import misc.MapLoader;
import misc.Model;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author juho
 */
public class Glomes {
    private Stack<GameStateTemplate> stateStack = new Stack();
    private Game gameState;
    private Menu menuState;
    private GameStateTemplate currentState;
    private int[] currentResolution;
    
    private MapData mapData;
    private ArrayList<Map> maps;
    
    
    public Glomes(){
        currentResolution = Statics.getResolution();
        gameState = new Game(this);
        menuState = new Menu(this);
    }
    public void run(){
        //TODO: Load config file from which settings, such as screen resolution, fullscreen mode, vsync, anti-aliasing, keyboard configuration
        //and so on are loaded.
        initGL();
        
        loadTextures();
        
        loadMaps();
        
        stateStack.push(menuState);
        stackHandler();
        
    }
    private void initGL(){
        try {
            Display.setDisplayMode(new DisplayMode(currentResolution[0], currentResolution[1]));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace(System.out);
            System.exit(0);
        }
        
        Display.setTitle(Statics.TITLE);
        //TODO: Replace with the perspective view mode can't remember whatsitcalled. Also probably redo this whole method.

        GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
        
        //Anti-aliasing stuff:
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glShadeModel(GL11.GL_SMOOTH); // Enable Smooth Shading
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0); // Depth Buffer Setup
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do

        GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
        GL11.glLoadIdentity(); // Reset The Projection Matrix

        // Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(
                45.0f,
                (float) currentResolution[0] / (float) currentResolution[1],
                0.1f,
                1000.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix

        // Really Nice Perspective Calculations
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        
        
        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
        GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_LINE); //Backsides of textures are just wireframes.
        //If your textures are just wireframes, make sure you draw the vertice in counter-clockwise
        //rotation. E.g.: (0,0; 1,0; 1,1; 0,1).
        
    }
    private void stackHandler(){
        while (stateStack.empty() == false){
            currentState = stateStack.pop();
            currentState.use();
        }
        Display.destroy();

    }
    public void addToStack(int newState){
        switch (newState){
            case (Statics.GAME_STATE):
                gameState.use();
                break;
            case (Statics.MENU_STATE):
                menuState.use();
                break;
        }
    }
    
    private void loadMaps(){
        maps = new ArrayList();
        MapLoader mapLoader = new MapLoader();
        Map newMap;
        
        mapData = mapLoader.loadMap("cfg/mapdata/test map.umf");
        newMap = new Map(mapData);
        maps.add(newMap);
        //TODO: make a loop that goes through all *.umf (ultimate map format) files in the mapdata folder.        
    }
    
    private void loadTextures() {
        HashMap<String, Texture> textureMap = new HashMap();
        Texture texture;
        //TODO: change the folder to res/textures/
        File folder = new File("res/test/");
        File[] files = folder.listFiles();
        String fileName = "";
        for (int i = 0; i < files.length; i++) {

            if (files[i].isFile()) {
                fileName = files[i].getName();
            }
            try {
                if (fileName.substring(fileName.length() - 4).equalsIgnoreCase(".jpg")) {
                    texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/test/" + fileName));
                } else if (fileName.substring(fileName.length() - 4).equalsIgnoreCase(".png")) {
                    texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/test/" + fileName));
                } else if (fileName.substring(fileName.length() - 4).equalsIgnoreCase(".bmp")) {
                    texture = TextureLoader.getTexture("BMP", ResourceLoader.getResourceAsStream("res/test/" + fileName));
                } else {
                    continue;
                }
                System.out.println(fileName);
                textureMap.put(fileName, texture);
                textureMap.put("/res/test/" + fileName, texture);
                textureMap.put("res/test/" + fileName, texture);

            } catch (IOException ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Statics.setTextures(textureMap);
    }
    
    public Map getMap(){
        return maps.get(0);
    }
}
