/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import java.util.Stack;
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
    public Game gameState;
    public Menu menuState;
    public GameStateTemplate currentState;
    
    public Glomes(){
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
            Display.setDisplayMode(new DisplayMode(Statics.DisplayWidth, Statics.DisplayHeight));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace(System.out);
            System.exit(0);
        }
        
        Display.setTitle(Statics.TITLE);
        
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        
        //TODO: Replace with the perspective view mode can't remember whatsitcalled. Also probably redo this whole method.
        GL11.glOrtho(0, Statics.DisplayWidth, 0, Statics.DisplayHeight, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

    }
    private void stackHandler(){
        while (stateStack.empty() == false){
            currentState = stateStack.pop();
            currentState.run();
        }

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
