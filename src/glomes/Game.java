/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import balls.Ball;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import maps.GridSquare;
import maps.Map;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author juho
 */
public class Game extends GameStateTemplate {
    private boolean quitBoolean;
    private Ball ball;
    private float mouseX, mouseY, dTime, scaledTime;
    private float speed = 0;
    private GridSquare[][] grid;
    private Map map;
    private double time, timeNew;
    
    private GUI gui;
    private ThemeManager themeManager;
    private LWJGLRenderer guiRenderer;
    
    
    private Vector3f camera;

    public Game(Glomes mainGame){
        super(mainGame);
    }
    
    @Override
    public void use(){
        float drawNow = 0;
        int secondTest = 0;
        dTime = 0;
        scaledTime = 0;
        quitBoolean = false;
        map = game.getMap();
        grid = map.getGrid();
        camera = new Vector3f(-50f, -50f, -60f);
        
        System.out.println("moved to game");
        time();
        initTWL();
        while (quitBoolean == false){
            time();
            map.update(scaledTime);
            
            
            if (Display.isCloseRequested()) {
                quitBoolean = true;
            }            
            drawNow+=dTime;
            if (drawNow >= 16.67f){
                draw3d();
                input();
                gui.update();
                drawNow -= 16.67f;
            }
            Display.sync(600);
        }
    }
    
    private void initTWL() {
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
    
    private void setupWidgets(){
        
    }
    
    @Override
    protected boolean handleEvent(Event evt){
        
        boolean allowHandling;
        allowHandling = true;
        if (Arrays.binarySearch(Statics.naughtyKeys, evt.getKeyCode()) >= 0) {
            allowHandling = false;
        }
        //Event used by a widget.
        if (allowHandling == true && super.handleEvent(evt)) {
            
        } 
        //Event not used by a widget.
        else {
            if (evt.isKeyEvent() == true){
                if (evt.isKeyPressedEvent() == true) {
                    handleKeyDown(evt);
                }
                else{
                    handleKeyUp(evt);
                }
            }
            else if (evt.isMouseEventNoWheel() == true){
                if (evt.getType() == Event.Type.MOUSE_BTNDOWN){
                    handleMouseDown(evt);
                }
                else if (evt.getType() == Event.Type.MOUSE_BTNUP){
                    handleMouseUp(evt);
                }
            }
        }
        return true;
    }
    
    private void handleKeyDown(Event evt){
        switch (evt.getKeyCode()) {
            case Keyboard.KEY_ESCAPE:
                quitBoolean = true;
                break;
        }
    }

    private void handleKeyUp(Event evt) {
        
    }

    private void handleMouseDown(Event evt) {
        selection();
    }

    private void handleMouseUp(Event evt) {
        
        Vector3f v;
        switch (evt.getMouseButton()) {
            case Event.MOUSE_LBUTTON:
                System.out.println("X: " + mouseX + ", Y: " + mouseY);
                ball = new Ball(camera, grid);
                v = new Vector3f(mouseX, mouseY, 0.0f);
                v.normalise();
                v.scale((float) speed / 300);
                ball.setSpeed(v);
                System.out.println("Speed: " + speed);
                map.addBall(ball);
                speed = 0;
                break;
            case Event.MOUSE_RBUTTON:
                v = new Vector3f(mouseX, mouseY, 0.0f);
                v.normalise();
                v.scale((float) speed / 300);
                for (int i = 0; i < 20; i++) {
                    ball = new Ball(camera, grid);
                    ball.setSpeed(v);
                    map.addBall(ball);
                }
                System.out.println("20 Ball extravaganza! Speed: " + speed);
                speed = 0;
                break;
        }
    }

    //Only "isKeyDown()" methods should be used in this one.
    private void input(){
        
        mouseX = Mouse.getX() - Display.getWidth() / 2;
        mouseY = Mouse.getY() - Display.getHeight() / 2;
        
        if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)){
            if (speed < 60) {
                speed += dTime * 0.5;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_C)){
            Vector3f v = new Vector3f(mouseX, mouseY, 0.0f);
            v.normalise();
            ball = new Ball(camera, grid);
            v.scale((float) 40 / 300);
            ball.setSpeed(v);
            v.normalise();
            map.addBall(ball);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
            ball = new Ball(camera, grid);
            map.addBall(ball);
        }
        

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            camera.setX(camera.getX() + scaledTime);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            camera.setX(camera.getX() - scaledTime);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            camera.setY(camera.getY() + scaledTime);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            camera.setY(camera.getY() - scaledTime);
        }
        
        //Page up and down:
        if (Keyboard.isKeyDown(Keyboard.KEY_NEXT)) {
            camera.setZ(camera.getZ() - scaledTime);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR)) {
            camera.setZ(camera.getZ() + scaledTime);
        }
        
        float factor = 5f;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            factor = 1f;
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            map.accelerateBall(0, scaledTime * 0.001f * factor, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            map.accelerateBall(0, scaledTime * -0.001f * factor, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            map.accelerateBall(scaledTime * -0.001f * factor, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            map.accelerateBall(scaledTime * 0.001f * factor, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            map.stopBall();
        }

    }

    private void selection()
    {
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        FloatBuffer winZ = BufferUtils.createFloatBuffer(1);
        FloatBuffer position = BufferUtils.createFloatBuffer(3);
        float winX, winY;

        GL11.glGetFloat( GL11.GL_MODELVIEW_MATRIX, modelview );
        GL11.glGetFloat( GL11.GL_PROJECTION_MATRIX, projection );
        GL11.glGetInteger( GL11.GL_VIEWPORT, viewport );

        winX = (float)Mouse.getX();
        winY = (float)Mouse.getY();

        GL11.glReadPixels((int)winX, (int)winY, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, winZ);
        GLU.gluUnProject(winX, winY, winZ.get(0), modelview, projection, viewport, position);
//        for (int i = 0;i < 3;i++)
//            System.out.println(position.get(i));
    }

    private void draw3d() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        GL11.glColor4f(1f, 1f, 1f, 1f);
            
        GL11.glLoadIdentity();                          // Reset The Current Modelview Matrix
        //GL11.glRotatef(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glTranslatef(camera.getX(), camera.getY(), camera.getZ());
        
        map.draw(scaledTime);
        
        Display.update();
    }
    private void time() {
        timeNew = System.nanoTime();
        dTime = (float) (timeNew - time);
        dTime /= 1000000f; //Convert to milliseconds.
        if (dTime > 5f){
            dTime = 5f;
        }
        scaledTime = dTime * Statics.GAME_SPEED;
        time = timeNew;
    }
}
