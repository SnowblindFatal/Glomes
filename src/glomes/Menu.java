/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Event.Type;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import misc.Model;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author juho
 */
public class Menu extends GameStateTemplate {
    private boolean quitBoolean = false;
    
    private Button testButton;
    private EditField testTextBox;
    private Runnable testButtonAction;
    private GUI gui;
    private ThemeManager themeManager;
    private LWJGLRenderer guiRenderer;
    
    
    //temp stuff for quick 3d testing:
    private float rtri = 1f, rquad;
    Model model = new Model();

    
    
    public Menu(Glomes mainGame){
        super(mainGame);
    }
    
    @Override
    public void use(){
        System.out.println("moved to menu");
        initTWL();
        

        model.load("res/test/torus.obj", "res/test/Cube.bmp");
        
        while (quitBoolean == false){
            draw3d(); // Draw my own stuff first so the gui will be in front of everything.
            gui.update();
            if (Display.isCloseRequested()){
                quitBoolean = true;
            }
            Display.update();
            Display.sync(60);
        }
    }
    //Override GUI's default input-handling scheme. Mouse input is still a bit
    //of a mystery. If a widget happens to handle a mouse event, it doesn't appear
    //here at all!
    @Override
    protected boolean handleEvent(Event evt){
        int eventKey, mouseX, mouseY, mouseWheelDelta;
        char eventChar;
        boolean pressed, allowHandling;
        Type eventType;
        eventType = evt.getType();
        eventKey = evt.getKeyCode();
        mouseX = evt.getMouseX();
        mouseY = evt.getMouseY();
        mouseWheelDelta = evt.getMouseWheelDelta();
        eventChar = evt.getKeyChar();
        pressed = evt.isKeyPressedEvent();
        allowHandling = true;
        
//        if (eventType == Type.MOUSE_BTNDOWN){
//            allowHandling = false;
//        }
        if (Arrays.binarySearch(Statics.naughtyKeys, eventKey) >= 0) {
            allowHandling = false;
        }
        
        
        //Event used by a widget.
        if (allowHandling == true && super.handleEvent(evt)) {
            
        } 
        //Event not used by a widget.
        else{
            if (pressed == true) {
                switch (eventKey) {
                    case Keyboard.KEY_ESCAPE:
                        quitBoolean = true;
                        break;
                    case Keyboard.KEY_RETURN:
                        allDone();
                        break;
                    case Keyboard.KEY_SPACE:
                        break;
                    case Keyboard.KEY_F1:

                        //Is there a better way to clear focus?
                        testButton.requestKeyboardFocus();
                        testButton.giveupKeyboardFocus();
                        break;
                }
            }
        }
        return true;
    }
    
    private void draw3d() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glTranslatef(2f, 0f, -10f);
        GL11.glRotatef(30f, 1.0f, 1.0f, 0.0f);
        model.draw();
    }
    
    private void initTWL(){
        try {
            //renderer is used by TWL to draw itself on the screen.
            guiRenderer = new LWJGLRenderer();
            //in here, "this" refers to the screen as a widget, I reckon.
            gui = new GUI(this, guiRenderer);
            //themeManager sets the look of all the widgets on the screen I think.
            //NOTE: If modifications to the xml-file don't seem to apply, use "Clean and build project" to remove all cached shit.
            //TODO: Our own custom UI theme. Doesn't need to be anything fancy, just SOMETHING that isn't
            //classic Windows look from the early 90s.
            themeManager = ThemeManager.createThemeManager(getClass().getResource("/ui_themes/simple.xml"), guiRenderer);
            //without setTheme the theme defaults to name of the class in lowercase. In this case it would be "menu".
            this.setTheme("");
            //all in all, there seems to be three (four?) themes with the typical UI graphics: 
            //simple.xml, gui.xml, Eforen.xml and guiTheme.xml.
            //HOWEVER, of these, I could only get simple.xml to work properly. The others have weird xml
            //that is probably completely shit by anyone's standards.
            
            gui.applyTheme(themeManager);
            setupWidgets();
        } catch (LWJGLException | IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setupWidgetActions(){
        testButtonAction = new Runnable() {
            @Override
            public void run() {
                button0Action();
            }
        };
    }
    
    private void setupWidgets(){
        setupWidgetActions();
        testButton = new Button("A working button!");
        testButton.addCallback(testButtonAction);
        testTextBox = new EditField();
        testTextBox.setMultiLine(true);
        
        
        //method add is inherited from the Widget class that the class GameStateTemplate extends
        this.add(testButton);
        this.add(testTextBox);
    }
    
    @Override
    protected void layout(){
        testButton.setSize(200, 100);
        testButton.setPosition(300, 200);
        testTextBox.setSize(200, 200);
        testTextBox.setPosition(20, 30);
            
    }
    private void allDone(){
        quitBoolean = true;
        this.game.addToStack(Statics.GAME_STATE);
    }
    private void button0Action(){
        allDone();
        System.out.println("button executed. Textbox content: " + testTextBox.getText());
    }
}