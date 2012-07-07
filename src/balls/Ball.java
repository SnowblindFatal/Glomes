package balls;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
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
    public final int slices = 32;
    private float r;
    private Vector3f speed = new Vector3f(0,0,0), location = new Vector3f(0,0,0);
    private Texture texture;

    private ByteBuffer tmp = ByteBuffer.allocateDirect(16*4);
    private FloatBuffer rotationMatrix;

    private My_Quaternion rotation = new My_Quaternion();

    public Ball(){
        super();
        //init();
    }

    public Ball(float r){
        super();
        this.r = r;
        init();
    }

    public Ball(float r, float x, float y, float z){
        super();
        this.r = r;
        this.set_coords(x, y, z);
        init();
    }

    private void init(){
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

    public void drawing(){
        getTexture().bind();

        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        Vector3f v = location;

        GL11.glTranslatef(v.getX(), v.getY(), v.getZ());
        GL11.glMultMatrix(rotationMatrix);
        draw(r, slices, slices);
        GL11.glPopMatrix();
    }

    public boolean update(){
        Vector3f.add(location, speed, location);

        Vector3f v = speed;
        Vector4f v1 = new Vector4f(v.getY(),-v.getX(),v.getZ(),v.length()/2);

        My_Quaternion q = new My_Quaternion();
        q.setFromAxisAngle(v1);
        if (v1.length() == 0.0f) q.setIdentity();

        rotation = (My_Quaternion) My_Quaternion.mul(rotation,q,rotation);
        rotationMatrix = (FloatBuffer)tmp.asFloatBuffer().put(
                                rotation.tomatrix()).flip();
        v = getLocation();

        drawing();
        if (v.getX() > 100 || v.getY() > 100) return false;
        return true;
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
