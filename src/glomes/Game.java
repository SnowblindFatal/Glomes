/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import balls.Ball;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author juho
 */
public class Game extends GameStateTemplate {
    boolean quitBoolean = false;
    Ball ball;
    float mouseX, mouseY;
    int speed = 0;
    
    private float cameraX, cameraY, cameraZ;

    public Game(Glomes mainGame){
        super(mainGame);
    }
    
    @Override
    public void use(){
        cameraX = 0f;
        cameraY = 0f;
        cameraZ = -60f;
        
        System.out.println("moved to game");
        while (quitBoolean == false){
            input();            
            draw();
            //quitBoolean = true;
            if (Display.isCloseRequested()) {
                quitBoolean = true;
            }            
            Display.sync(60);
        }
    }

    private void input(){
        if (Mouse.isButtonDown(0)){
            mouseX = Mouse.getX() - Display.getWidth() / 2;
            mouseY = (Mouse.getY() - Display.getHeight() / 2);
            if (speed < 120) speed++;
        }else if (speed != 0) {
            ball = new Ball(-cameraX,-cameraY,cameraZ);
            Vector3f v = new Vector3f(mouseX,mouseY,0.0f);
            v.normalise();
            v.scale((float) speed / 30);
            ball.setSpeed(v);
            System.out.println("Speed: " + speed);
            speed = 0;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            cameraX += 1.1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            cameraX -= 1.1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            cameraY += 1.1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            cameraY -= 1.1f;
        }
        
        //Page up and down:
        if (Keyboard.isKeyDown(Keyboard.KEY_NEXT)) {
            cameraZ -= 1.1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR)) {
            cameraZ += 1.1f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            quitBoolean = true;
        }

    }

    private void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        if (ball != null)
            if (!ball.update(cameraX,cameraY,cameraZ)) ball = null;
        
        GL11.glLoadIdentity();                          // Reset The Current Modelview Matrix
        GL11.glRotatef(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glTranslatef(cameraX, cameraY, cameraZ);
        game.getMap().draw();
        
        Display.update();
    }
}
