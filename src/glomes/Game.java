/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author juho
 */
public class Game extends GameStateTemplate {
    boolean quitBoolean = false;
    Ball ball;
    float mouseX, mouseY;
    int speed = 0;

    private ByteBuffer tmp = ByteBuffer.allocateDirect(16*4);
    private FloatBuffer rotationMatrix;

    private My_Quaternion rotation = new My_Quaternion();

    public Game(Glomes mainGame){
        super(mainGame);
        tmp.order(ByteOrder.nativeOrder());
        rotation.setIdentity();
    }
    
    @Override
    public void use(){
        System.out.println("moved to game");
        while (quitBoolean == false){
            input();
            if (ball != null){
                ball.update();

                Vector3f v = ball.getSpeed();
                Vector4f v1 = new Vector4f(v.getY(),-v.getX(),v.getZ(),v.length()/2);

                My_Quaternion q = new My_Quaternion();
                q.setFromAxisAngle(v1);
                if (v1.length() == 0.0f) q.setIdentity();

                rotation = (My_Quaternion) My_Quaternion.mul(rotation,q,rotation);
                rotationMatrix = (FloatBuffer)tmp.asFloatBuffer().put(
                                        rotation.tomatrix()).flip();
                v = ball.getLocation();
                if (v.getX() > 100 || v.getY() > 100) ball = null;
            }
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
        if (ball != null){
            ball.getTexture().bind();

            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            Vector3f v = ball.getLocation();

            GL11.glTranslatef(v.getX(), v.getY(), v.getZ());
            GL11.glMultMatrix(rotationMatrix);
            ball.draw(ball.getR(), ball.slices, ball.slices);
            GL11.glPopMatrix();
        }
        Display.update();
    }
}
