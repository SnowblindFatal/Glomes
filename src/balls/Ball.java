package balls;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import maps.GridSquare;
import misc.My_Quaternion;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author Jusku
 */
public class Ball extends Sphere{
    private final int slices = 32;
    private final float radius = 1.0f;
    private Vector3f speed, location, camera;
    private Texture texture;
    private GridSquare[][] grid;
    private GridSquare currentSquare;
    private int gridX, gridY;

    private ByteBuffer tmp = ByteBuffer.allocateDirect(16*4);
    private FloatBuffer rotationMatrix;

    private My_Quaternion rotation = new My_Quaternion();

    public Ball(Vector3f newCamera, GridSquare[][] newGrid){
        grid = newGrid;
        camera = newCamera;
        speed = new Vector3f(0, 0, 0);
        location = new Vector3f(-camera.getX(), -camera.getY(), radius);
        System.out.println(location.toString());
        init();
    }

    private void init(){
        addToGrid();
        tmp.order(ByteOrder.nativeOrder());
        rotation.setIdentity();
        setNormals(GLU.GLU_SMOOTH);
        setTextureFlag(true);
        try{
            texture = TextureLoader.getTexture("BMP",
                                    new FileInputStream("res/test/Glass.bmp"));
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void draw(){
        Vector4f v1 = new Vector4f(speed.getY(), -speed.getX(), speed.getZ(), speed.length());

        My_Quaternion quaternion = new My_Quaternion();
        quaternion.setFromAxisAngle(v1);
        if (v1.length() == 0.0f) {
            quaternion.setIdentity();
        }

        rotation = (My_Quaternion) My_Quaternion.mul(rotation, quaternion, rotation);
        rotationMatrix = (FloatBuffer) tmp.asFloatBuffer().put(
                rotation.tomatrix()).flip();
        
        getTexture().bind();

        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        GL11.glTranslatef(location.getX() + camera.getX(), location.getY() + camera.getY(), location.getZ() + camera.getZ());
        GL11.glMultMatrix(rotationMatrix);
        draw(radius, slices, slices);
        GL11.glPopMatrix();
    }

    public boolean update(){
        
        Vector3f.add(location, speed, location);
        updateGridPosition();
        checkCollisions();

        if (location.getX() > 1000 || location.getY() > 1000){
            return false;
        }
        else if(location.getX() < -1000 || location.getY() < -1000){
            return false;
        }
        return true;
    }

    private void checkCollisions(){
        
    }
    
    private void addToGrid(){
        gridX = (int) (location.getX());
        gridY = (int) (location.getY());
        try {
            currentSquare = grid[gridX][gridY];
        } catch (Exception ex) {
            System.out.println("Ball out of boundaries!");
            Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(2001);
        }
        currentSquare = grid[gridX][gridY];
        currentSquare.occupyBall(this);   
    }
    private void updateGridPosition(){
        if (((int) location.getX() != gridX || ((int) location.getY()) != gridY)){
            removeFromGrid();
            addToGrid();
        }
    }
    private void removeFromGrid(){
        try {
            currentSquare.removeBall(this);
        } catch (Exception ex) {
            Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1001);
        }
    }
    

    public final void set_coords(float x,float y,float z){
        location.set(x,y,z);
    }

    public void setSpeed(Vector3f speed) {
        this.speed = speed;
    }

    public void accelerate(float x,float y,float z){
        Vector3f.add(speed, new Vector3f(x,y,z), speed);
    }

    public void accelerate(Vector3f v ){
        Vector3f.add(speed, v, speed);
    }

    public float getRadius(){
        return radius;
    }

    public Vector3f getLocation() {
        return location;
    }

    public Vector3f getSpeed() {
        return speed;
    }

     public Texture getTexture() {
        return texture;
    }
}
