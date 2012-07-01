/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

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
    
    
    public Glomes(){
        currentResolution = Statics.getResolution();
        gameState = new Game(this);
        menuState = new Menu(this);
    }
    public void run(){
        //TODO: Load config file from which settings, such as screen resolution, fullscreen mode, vsync, anti-aliasing, keyboard configuration
        //and so on are loaded.
        initGL();
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
        
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        
        //TODO: Replace with the perspective view mode can't remember whatsitcalled. Also probably redo this whole method.
        GL11.glOrtho(0, currentResolution[0], 0, currentResolution[1], 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0, 0, 0, 1);
        
    }
    private void stackHandler(){
        while (stateStack.empty() == false){
            currentState = stateStack.pop();
            currentState.run();
        }
        Display.destroy();

    }
    public void addToStack(int newState){
        switch (newState){
            case (Statics.GAME_STATE):
                gameState.run();
                break;
            case (Statics.MENU_STATE):
                menuState.run();
                break;
        }
    }
}
