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
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author juho
 */
public class Menu extends GameStateTemplate {
    private boolean quitBoolean = false;
    
    private Button button[] = new Button[4];
    private Runnable buttonAction[] = new Runnable[4];
    private GUI gui;
    private ThemeManager themeManager;
    private LWJGLRenderer renderer;
    private int eventKey, mouseX, mouseY;
    
    //temp stuff for testing:
    private float rtri, rquad;
    
    
    
    
    public Menu(Glomes mainGame){
        super(mainGame);
    }
    
    @Override
    public void use(){
        System.out.println("moved to menu");
        initTWL();
        
        setupButtons();
        while (quitBoolean == false){
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            draw();
            handleInput();
            gui.update();

            if (Display.isCloseRequested()){
                quitBoolean = true;
            }
            Display.update();
            Display.sync(60);
        }
    }
    
    private void handleInput(){
        //handleEvent();
        mouseX = Mouse.getX();
        mouseY = Mouse.getY();
//        System.out.println("X: " + mouseX + ", Y: " + mouseY);
        while (Keyboard.next()) {
            eventKey = Keyboard.getEventKey();
            switch (eventKey) {
                case Keyboard.KEY_ESCAPE:
                    quitBoolean = true;
                    break;
                case Keyboard.KEY_RETURN:
                    game.addToStack(Statics.GAME_STATE);
                    quitBoolean = true;
                    break;
                case Keyboard.KEY_SPACE:
                    break;
            }
        }
        while (Mouse.next()){
            eventKey = Mouse.getEventButton();
            System.out.println(eventKey);
            switch (eventKey){
                
            }
        }
    }
    
    private void draw() {
        GL11.glLoadIdentity();                          // Reset The Current Modelview Matrix

        GL11.glTranslatef(-1.5f, 0.0f, -6.0f);                // Move Left 1.5 Units And Into The Screen 6.0
        GL11.glRotatef(rtri, 0.0f, 1.0f, 0.0f);                // Rotate The Triangle On The Y axis ( NEW )
        GL11.glBegin(GL11.GL_TRIANGLES);                    // Drawing Using Triangles
        GL11.glColor3f(1.0f, 0.0f, 0.0f);             // Set The Color To Red
        GL11.glVertex3f(0.0f, 1.0f, 0.0f);         // Move Up One Unit From Center (Top Point)
        GL11.glColor3f(0.0f, 1.0f, 0.0f);             // Set The Color To Green
        GL11.glVertex3f(-1.0f, -1.0f, 0.0f);         // Left And Down One Unit (Bottom Left)
        GL11.glColor3f(0.0f, 0.0f, 1.0f);             // Set The Color To Blue
        GL11.glVertex3f(1.0f, -1.0f, 0.0f);         // Right And Down One Unit (Bottom Right)
        GL11.glEnd();                                       // Finished Drawing The Triangle

        GL11.glLoadIdentity();                          // Reset The Current Modelview Matrix
        GL11.glTranslatef(1.5f, 0.0f, -6.0f);             // Move Right 1.5 Units And Into The Screen 6.0
        GL11.glRotatef(rquad, 1.0f, 0.0f, 0.0f);               // Rotate The Quad On The X axis ( NEW )
        GL11.glColor3f(0.5f, 0.5f, 1.0f);                 // Set The Color To Blue One Time Only
        GL11.glBegin(GL11.GL_QUADS);                        // Draw A Quad
        GL11.glVertex3f(-1.0f, 0f, 0.0f);         // Top Left
        GL11.glVertex3f(1.0f, 0f, 0.0f);         // Top Right
        GL11.glVertex3f(1.0f, -1.0f, 0.0f);         // Bottom Right
        GL11.glVertex3f(-1.0f, -1.0f, 0.0f);         // Bottom Left
        GL11.glEnd();                                       // Done Drawing The Quad

        rtri += 10f;                                       // Increase The Rotation Variable For The Triangle ( NEW )
        rquad -= 2f;                                 // Decrease The Rotation Variable For The Quad     ( NEW )
    }
    
    private void initTWL(){
        //renderer is used by TWL to draw itself on the screen.
        try {
            renderer = new LWJGLRenderer();
            //in here, "this" refers to the screen as a widget, I reckon.
            gui = new GUI(this, renderer);
            //themeManager sets the look of all the widgets on the screen I think.
            //NOTE: If modifications to the xml-file don't seem to apply, use "Clean and build project" to remove all cached shit.
            //TODO: Our own custom UI theme. Doesn't need to be anything fancy, just SOMETHING that isn't
            //classic Windows look from the early 90s.
            
            themeManager = ThemeManager.createThemeManager(getClass().getResource("/ui_themes/simple.xml"), renderer);
            //without setTheme the theme defaults to name of the class in lowercase. In this case it would be "menu".
            setTheme("");
            //all in all, there seems to be three (four?) themes with the typical UI graphics: 
            //simple.xml, gui.xml, Eforen.xml and guiTheme.xml.
            //HOWEVER, of these, I could only get simple.xml to work properly. The others have weird xml
            //that is probably completely shit by anyone's standards.
            
        } catch (LWJGLException | IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        gui.applyTheme(themeManager);
    }
    
    private void setupWidgetActions(){
        buttonAction[0] = new Runnable() {
            @Override
            public void run() {
                allDone();
            }
        };
    }
    
    private void setupButtons(){
        setupWidgetActions();
        button[0] = new Button("Epic button");
        button[0].setTheme("button");
        button[0].addCallback(buttonAction[0]);
        //method add is derived from the Widget class that the class GameStateTemplate extends
        this.add(button[0]);
    }
    
    @Override
    protected void layout(){
        button[0].setSize(200, 100);
        button[0].setPosition(300, 200);
            
    }
    private void allDone(){
        quitBoolean = true;
        game.addToStack(Statics.GAME_STATE);
    }
}