/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import external.Convexiser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

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
    private LWJGLRenderer renderer;
    
    
    //temp stuff for quick 3d testing:
    private float rtri, rquad;
    private Convexiser convexiser;
    private List<Vector3f> polygon;
    private List<List<Vector3f>> triangles;
    private Vector3f vector;
    private Texture customTexture;
    
    
    
    public Menu(Glomes mainGame){
        super(mainGame);
    }
    
    @Override
    public void use(){
        System.out.println("moved to menu");
        initTWL();
        
        //TESTSTUFF:
        //REMOVE THIS:
        convexiser = new Convexiser();
        polygon = new ArrayList();
        triangles = new ArrayList();
        //Save this for now:
        try {
            customTexture = TextureLoader.getTexture("BMP", ResourceLoader.getResourceAsStream("/res/test/Mud.bmp"));
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while (quitBoolean == false){
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            draw(); // Draw my own stuff first so the gui will be in front of everything.
            
            //Since we want to handle the input ourselves, we can't just call gui.update().
            //This is why we separately do everything gui.update() does except for the input
            //handling.
            gui.setSize();
            gui.updateTime();
            handleInput();
            gui.handleKeyRepeat();
            gui.handleTooltips();
            gui.updateTimers();
            gui.invokeRunables();
            gui.validateLayout();
            gui.draw();
            gui.setCursor();
            if (Display.isCloseRequested()){
                quitBoolean = true;
            }
            Display.update();
            Display.sync(60);
        }
    }

    private void handleInput(){
        //handleEvent();
        int eventKey, mouseX, mouseY, mouseWheelDelta;
        char eventChar;
        boolean pressed;
        mouseX = Mouse.getX();
        mouseY = Mouse.getY();
        
        while (Keyboard.next()) {
            eventKey = Keyboard.getEventKey();
            eventChar = Keyboard.getEventCharacter();
            pressed = Keyboard.getEventKeyState();
            //If no widget finds any use for the keyboard event, it will
            //return false and let my own logic deal with the input.
            if (gui.handleKey(eventKey, eventChar, pressed) == false){
                System.out.println("no widget used keyboard event. Keyboard keyCode: " + eventKey + ", Character: " + eventChar);
                switch (eventKey) {
                    case Keyboard.KEY_ESCAPE:
                        quitBoolean = true;
                        break;
                    case Keyboard.KEY_RETURN:
                        allDone();
                        break;
                    case Keyboard.KEY_SPACE:
                        //TODO: Remove this shit.
                        testTriangulation();
                        break;
                    case Keyboard.KEY_F1:
                        
                        //Is there a better way to clear focus?
                        testButton.requestKeyboardFocus();
                        testButton.giveupKeyboardFocus();
                        break;
                }
            }

        }
        while (Mouse.next()){
//            System.out.println("mouseX: " + mouseX + ", mouseY: " + mouseY);
            Mouse.getDWheel();
            eventKey = Mouse.getEventButton();
            pressed = Mouse.getEventButtonState();
            mouseWheelDelta = Mouse.getEventDWheel();
            //If no widget finds any use for the mouse event, it will
            //return false and let my own logic deal with the input.
            //IMPORTANT: TWL uses flipped Y axis for mouse location!
            if (gui.handleMouse(mouseX, Statics.getDisplayHeight() - mouseY, eventKey, pressed) == false){
                
                System.out.println("no widget used mouse event. Event button: " + eventKey);
                switch (eventKey) {
                }
            }
            if (gui.handleMouseWheel(mouseWheelDelta) == false){
                System.out.println("no widget used mouse scroll. Scroll delta: " + mouseWheelDelta);
            }
        }
        
    }
    
    private void testTriangulation(){
        //in short: convexiser doesn't work properly and turns out to be useless on top of everything.
        //I'll remove this in the near future.
        polygon.clear();
        vector = new Vector3f(2f, 0f, 0f);
        polygon.add(vector);
        vector = new Vector3f(2f, 2f, 0f);
        polygon.add(vector);
        vector = new Vector3f(1f, 1.5f, 0f);
        polygon.add(vector);
        vector = new Vector3f(0f, 2f, 0f);
        polygon.add(vector);
        vector = new Vector3f(0f, 0f, 0f);
        polygon.add(vector);
        
        triangles = convexiser.convexise(polygon);
        System.out.println(polygon.toString());
        System.out.println(triangles.toString());
        triangles.clear();
        
    }
    private void draw() {
        GL11.glLoadIdentity();                          // Reset The Current Modelview Matrix

        GL11.glTranslatef(+1.5f, 0.0f, -6.0f);                // Move Left 1.5 Units And Into The Screen 6.0
        GL11.glRotatef(rtri, 0.0f, 1.0f, 0.0f);                // Rotate The Triangle On The Y axis ( NEW )
//        GL11.glBegin(GL11.GL_TRIANGLES);                    // Drawing Using Triangles
//        GL11.glColor3f(1.0f, 0.0f, 0.0f);             // Set The Color To Red
//        GL11.glVertex3f(0.0f, 1.0f, 0.0f);         // Move Up One Unit From Center (Top Point)
//        GL11.glColor3f(0.0f, 1.0f, 0.0f);             // Set The Color To Green
//        GL11.glVertex3f(-1.0f, -1.0f, 0.0f);         // Left And Down One Unit (Bottom Left)
//        GL11.glColor3f(0.0f, 0.0f, 1.0f);             // Set The Color To Blue
//        GL11.glVertex3f(1.0f, -1.0f, 0.0f);         // Right And Down One Unit (Bottom Right)
//        GL11.glEnd();                                       // Finished Drawing The Triangle
//
//        GL11.glLoadIdentity();                          // Reset The Current Modelview Matrix
//        GL11.glTranslatef(1.5f, 0.0f, -6.0f);             // Move Right 1.5 Units And Into The Screen 6.0
//        GL11.glRotatef(rquad, 1.0f, 0.0f, 0.0f);               // Rotate The Quad On The X axis ( NEW )
//        GL11.glColor3f(0.5f, 0.5f, 1.0f);                 // Set The Color To Blue One Time Only
//        GL11.glBegin(GL11.GL_QUADS);                        // Draw A Quad
//        GL11.glVertex3f(-1.0f, 0f, 0.0f);         // Top Left
//        GL11.glVertex3f(1.0f, 0f, 0.0f);         // Top Right
//        GL11.glVertex3f(1.0f, -1.0f, 0.0f);         // Bottom Right
//        GL11.glVertex3f(-1.0f, -1.0f, 0.0f);         // Bottom Left
//        GL11.glEnd();                                       // Done Drawing The Quad
        
        //Here, 64 pixels represent one unit in the game world. 
        //Try loading Mud.bmp or Glass.bmp to see the effect.
        float heightfactor = -128f / customTexture.getTextureHeight();
        float widthfactor = 128f / customTexture.getTextureWidth();
        customTexture.bind();
        
        GL11.glBegin(GL11.GL_POLYGON);
        //Texture's X and Y coordinates are the same as those of the actual vertex, 
        //only the first are multiplied by a set number.
        GL11.glTexCoord2f(2f * widthfactor, 0f * heightfactor);
        GL11.glVertex3f(2f, 0f, -2f);
        GL11.glTexCoord2f(2f * widthfactor, 2f * heightfactor);
        GL11.glVertex3f(2f, 2f, -2f);
        GL11.glTexCoord2f(1f * widthfactor, 1.5f * heightfactor);
        GL11.glVertex3f(1f, 1.5f, -2f);
        GL11.glTexCoord2f(0f * widthfactor, 2f * heightfactor);
        GL11.glVertex3f(0f, 2f, -2f);
        GL11.glTexCoord2f(0.0f * widthfactor, 0.0f * heightfactor);
        GL11.glVertex3f(0f, 0f, -2f);
        GL11.glEnd();

//        rtri += 1f;                                       // Increase The Rotation Variable For The Triangle ( NEW )
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
        } catch (Exception ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        gui.applyTheme(themeManager);
        setupWidgets();
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
        game.addToStack(Statics.GAME_STATE);
    }
    private void button0Action(){
        System.out.println("button executed. Textbox content: " + testTextBox.getText());
    }
}