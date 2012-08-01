/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glomes;

import balls.Ball;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import maps.GridSquare;
import maps.Map;
import maps.Selectable;
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
    private boolean n = false,f1 = false,shoot = false,del = false;
    private Ball ball;
    private Selectable selected;
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
            if (selected != null && selected.getType().equals("Ball") && shoot){
                if (speed < 120) speed += scaledTime * 0.25;
            } else {
                selection();
                shoot = false;
            }
        } else if (speed != 0) {
            ball = (Ball) selected;
            float mPos[] = mouseToWorld();
            mPos[0] -= ball.getLocation().getX();
            mPos[1] -= ball.getLocation().getY();
            Vector3f v = new Vector3f(mPos[0],mPos[1],0.0f);

            v.normalise();
            v.scale((float) speed / 300);
            ball.setSpeed(v);
            System.out.println("Speed: " + speed);
            //map.addBall(ball);
            speed = 0;
            //shoot = false;
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

        if (Keyboard.isKeyDown(Keyboard.KEY_N)){
            if (!n){
                ball = new Ball(camera, grid);
                map.addBall(ball);
                n = true;
            }
        } else n = false;

        if (Keyboard.isKeyDown(Keyboard.KEY_F1)){
            if (!f1){
                shoot = !shoot;
                f1 = true;
            }
        } else f1 = false;

        if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)){
            if (selected != null){
                Ball delBall = (Ball) selected;
                delBall.delete();
                map.removeBall(delBall);
                selected = null;
            }
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

    private float[] mouseToWorld (){
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

        float ret[] = {position.get(0),position.get(1)};
        return ret;
    }

    private void selection()
    {
        LinkedList<Selectable> objects = new LinkedList();
        
        float obj[] = mouseToWorld();
        float dist,distX,distY,shortestDist = 10f;

        for (int x = (int) (obj[0] - 2);x < obj[0]+3;x++){
            for (int y = (int) (obj[1] - 2);y < obj[1]+3;y++){
                try{
                    objects.addAll(grid[x][y].getBalls());
                    for (Selectable one : objects){
                        distX = one.getLocation().getX() - obj[0];
                        distY = one.getLocation().getY() - obj[1];
                        distX *= distX;
                        distY *= distY;
                        dist = (float) Math.sqrt(distX + distY);
                        if (dist < shortestDist){
                            shortestDist = dist;
                            selected = one;
                        }
                    }
                }catch(Exception e){}
            }
        }
        if (shortestDist == 10f) selected = null;
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
