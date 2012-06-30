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
public class Menu extends GameStateTemplate {
    boolean quitBoolean = false;
    public Menu(Glomes mainGame){
        super(mainGame);
        
    }
    
    @Override
    public void run(){
        while (quitBoolean == false){
            draw();
            System.out.println("moved to menu");
            quitBoolean = true;
        }
    }
    
    private void draw() {
        Display.update();
    }
}