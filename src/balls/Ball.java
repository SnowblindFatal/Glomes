package balls;

import java.io.FileInputStream;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author Jusku
 */
public class Ball extends Sphere{
    public final int slices = 32;
    private float r;
    private Vector3f speed = new Vector3f(0,0,0), location = new Vector3f(0,0,0);
    private Texture texture;

    public Ball(){
        super();
        loadTexture();
    }

    public Ball(float r){
        super();
        this.r = r;
        loadTexture();
    }

    public Ball(float r, float x, float y, float z){
        super();
        this.r = r;
        this.set_coords(x, y, z);
        loadTexture();
    }

    private void loadTexture(){
        setNormals(GLU.GLU_SMOOTH);
        setTextureFlag(true);
        try{
            texture = TextureLoader.getTexture("BMP",
                                    new FileInputStream("res/test/Glass.bmp"));
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void update(){
        Vector3f.add(location, speed, location);
    }

    public final void set_coords(float x,float y,float z){
        location.set(new Vector3f(x,y,z));
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

    public float getR(){
        return r;
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
