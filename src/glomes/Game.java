/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import balls.Ball;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import maps.GridSquare;
import maps.Map;
import org.lwjgl.BufferUtils;
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
        while (quitBoolean == false){
            time();
            input();     
            map.update(scaledTime);
            //quitBoolean = true;
            if (Display.isCloseRequested()) {
                quitBoolean = true;
            }            
            drawNow+=dTime;
            if (drawNow >= 16.67f){
                draw();
                drawNow -= 16.67f;
            }
            
//            secondTest += 1;
//            if (secondTest == 600){
//                System.out.println("now");
//                System.out.println(drawNow);
//                secondTest -= 600;
//            }
            Display.sync(600);
        }
    }

    private void input(){
        if (Mouse.isButtonDown(0)){            
            mouseX = Mouse.getX() - Display.getWidth() / 2;
            mouseY = Mouse.getY() - Display.getHeight() / 2;
            selection();
            if (speed < 120) speed += scaledTime * 0.25;
        }else if (speed != 0) {
            ball = new Ball(camera, grid);
            Vector3f v = new Vector3f(mouseX,mouseY,0.0f);
            v.normalise();
            v.scale((float) speed / 300);
            ball.setSpeed(v);
            System.out.println("Speed: " + speed);
            map.addBall(ball);
            speed = 0;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            camera.setX(camera.getX() + scaledTime * 0.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            camera.setX(camera.getX() - scaledTime * 0.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            camera.setY(camera.getY() + scaledTime * 0.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            camera.setY(camera.getY() - scaledTime * 0.1f);
        }
        
        //Page up and down:
        if (Keyboard.isKeyDown(Keyboard.KEY_NEXT)) {
            camera.setZ(camera.getZ() - scaledTime * 0.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR)) {
            camera.setZ(camera.getZ() + scaledTime * 0.1f);
        }
        
        float factor = 5f;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            factor = 1f;
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            map.accelerateBall(0, scaledTime * 0.0001f * factor, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            map.accelerateBall(0, scaledTime * -0.0001f * factor, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            map.accelerateBall(scaledTime * -0.0001f * factor, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            map.accelerateBall(scaledTime * 0.0001f * factor, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            map.stopBall();
        }
        

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            quitBoolean = true;
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

    private void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            
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
