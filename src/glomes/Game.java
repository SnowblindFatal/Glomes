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

    public Game(Glomes mainGame){
        super(mainGame);
    }
    
    @Override
    public void use(){
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
            ball = new Ball((float)Math.PI,0,0,-99.0f);
            Vector3f v = new Vector3f(mouseX,mouseY,0.0f);
            v.normalise();
            v.scale((float) speed / 20);
            ball.setSpeed(v);
            System.out.println("Speed: " + speed);
            speed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) quitBoolean = true;

    }

    private void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        if (ball != null)
            if (!ball.update()) ball = null;
        Display.update();
    }
}
