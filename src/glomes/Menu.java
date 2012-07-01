/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author juho
 */
public class Menu extends GameStateTemplate {
    private boolean quitBoolean = false;
    
    private Button button[] = new Button[4];
    private GUI gui;
    private ThemeManager themeManager;
    private LWJGLRenderer renderer;
    
    
    
    
    public Menu(Glomes mainGame){
        super(mainGame);
    }
    
    @Override
    public void run(){
        System.out.println("moved to menu");
        initTWL();
        setupButtons();
        while (quitBoolean == false){
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            draw();
            Display.update();
            Display.sync(60);
            
            if (Display.isCloseRequested()){
                quitBoolean = true;
            }
        }
    }
    
    private void draw() {
        Display.update();
    }
    
    private void initTWL(){
        //renderer is used by TWL to draw itself on the screen.
        try {
            renderer = new LWJGLRenderer();
            //in here, "this" refers to the screen as a widget, I reckon.
            gui = new GUI(this, renderer);
            //themeManager sets the look of all the widgets on the screen I think.
            themeManager = ThemeManager.createThemeManager(getClass().getResource("/ui_themes/simple.xml"), renderer);
            
        } catch (LWJGLException | IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        gui.applyTheme(themeManager);
    }
    
    private void setupButtons(){
        button[0] = new Button("Epic button");
        button[0].setTheme("button");
        //method add is derived from the Widget class that the class GameStateTemplate extends
        this.add(button[0]);
    }
    
    @Override
    protected void layout(){
        button[0].setSize(200, 100);
        button[0].setPosition(300, 200);
    }
}