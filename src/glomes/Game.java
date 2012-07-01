/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import org.lwjgl.opengl.Display;

/**
 *
 * @author juho
 */
public class Game extends GameStateTemplate {
    boolean quitBoolean = false;
    public Game(Glomes mainGame){
        super(mainGame);
        
    }
    
    @Override
    public void run(){
        System.out.println("moved to game");
        while (quitBoolean == false){
            draw();
            quitBoolean = true;
            if (Display.isCloseRequested()) {
                quitBoolean = true;
            }
        }
    }
    
    private void draw() {
        Display.update();
    }
}
