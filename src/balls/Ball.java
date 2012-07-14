package balls;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import maps.Corner;
import maps.GridSquare;
import maps.Wall;
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
    private final int slices = 20;
    private final float radius = 1.0f;
    private final float collisionDistance = radius + 0.0f;
    private final float mass = 1.0f;

    private Vector3f speed, location, camera;
    private Texture texture;
    private GridSquare[][] grid;
    private GridSquare currentSquare;
    private int gridX, gridY, displayListIndex;

    private ByteBuffer tmp = ByteBuffer.allocateDirect(16*4);
    private FloatBuffer rotationMatrix;

    private My_Quaternion rotation = new My_Quaternion();

    public Ball(Vector3f newCamera, GridSquare[][] newGrid){
        grid = newGrid;
        System.out.println(grid.length + ", " + grid[0].length);
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
        displayListIndex = GL11.glGenLists(1);
        GL11.glNewList(displayListIndex, GL11.GL_COMPILE); // Start With The List.
        this.draw(radius, slices, slices);
        GL11.glEndList();
    }

    public void draw(float dTime){
        Vector4f v1 = new Vector4f(speed.getY() * dTime, -speed.getX() * dTime, speed.getZ() * dTime, speed.length() * dTime);

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
        GL11.glCallList(displayListIndex);
        GL11.glPopMatrix();
    }

    public void update(float dTime){
//        Vector3f.add(location, speed, location);
        location.setX(location.getX() + speed.getX() * dTime);
        location.setY(location.getY() + speed.getY() * dTime);
        location.setZ(location.getZ() + speed.getZ() * dTime);
        updateGridPosition();
        checkCollisions();       
    }

    private void checkCollisions(){
        HashSet<Ball> collisionBalls = new HashSet();
        HashSet<Wall> collisionWalls = new HashSet();
        Wall collisionWall = null;
        HashSet<Corner> collisionCorners = new HashSet();
        float shortestDistance, newDistance;
        
        //First find all the objects this thing even could collide with!
        for (int x = gridX - 3; x < gridX + 3; x++){
            for (int y = gridY - 3; y < gridY + 3; y++){
                try {
                    if (grid[x][y].hasItems() == true){
                        for (Corner newCorner : grid[x][y].getCorners()){
                            collisionCorners.add(newCorner);
                        }
                        for (Wall newWall : grid[x][y].getWalls()) {
                            collisionWalls.add(newWall);
                        }
                        for (Ball newBall : grid[x][y].getBalls()) {
                            if (newBall != this){
                                collisionBalls.add(newBall);
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Ball too near edge!!");
                    Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(2003);
                }
            }
        }
        //Actually check for collisions here!
        //return if one collision is found. Numerous collisions per tick may (or may not!) fuck up the physics.
        shortestDistance = 2f;
        for (Wall wall : collisionWalls) {
            newDistance = distanceFromWall(wall.getVector(), wall.getEnd());
            if (newDistance < collisionDistance && newDistance < shortestDistance) {
                if (collisionPointInWall(wall, newDistance) == true) {
                    shortestDistance = newDistance;
                    collisionWall = wall;
                }
            }
        }
        if (collisionWall != null) {
            applyWallCollision(collisionWall, shortestDistance);
            return;
        }
        for (Corner corner : collisionCorners) {
            if (checkCornerCollision(corner) == true) {
                return;
            }
        }
        for (Ball ball : collisionBalls) {
            if (checkBallCollisions(ball)) return;
        }


        
    }
    private boolean checkCornerCollision(Corner corner){
        Vector3f direction = new Vector3f();
        Vector3f.sub(location, corner.getLocation(), direction);
        //Corner's Z is 0f, Ball's Z is ball.radius. We don't care about Z so...
        direction.setZ(0f);
        float distance = direction.length();
//        System.out.println(direction.length());
        if (distance < collisionDistance){
            applyCornerCollision(direction, distance);
            return true;
        }
        return false;
    }
    private void applyCornerCollision(Vector3f direction, float distance){
        System.out.println("cornercollide");
        float help;
        float moveDistance = collisionDistance - distance;
        Vector3f moveVector = new Vector3f();
        direction.normalise();
        moveVector.set(direction);
        moveVector.scale(moveDistance);
        Vector3f.add(location, moveVector, location);

        help = (float) Math.cos(Vector3f.angle(speed, direction));
        help *= speed.length();
        help *= 2;
        
        direction.scale(Math.abs(help));
        accelerate(direction);
    }
    private boolean collisionPointInWall(Wall wall, float distance){
        boolean check = true;
        
        Vector3f collisionPoint = new Vector3f();
        Vector3f cornerDist = new Vector3f();
        Vector3f cornerDist1 = new Vector3f();
        //We have to use the set command because otherwise we would be actually altering the wall's normal.
        collisionPoint.set(wall.getNormal());
        collisionPoint.scale(distance);
//        System.out.println("CollisionP: " + collisionPoint.length());
        Vector3f.sub(location, collisionPoint, collisionPoint);
        Vector3f.sub(collisionPoint,wall.getEnd(),cornerDist);
        Vector3f.sub(collisionPoint,wall.getBeginning(),cornerDist1);
        cornerDist.setZ(0f);
        cornerDist1.setZ(0f);

        if (cornerDist.length() > wall.getVector().length()) check = false;
        else if(cornerDist1.length() > wall.getVector().length()) check = false;

        if (check == true){
            return true;
        }
        return false;
    }

    private float distanceFromWall(Vector3f wallVector, Vector3f wallEnd){
        //y = kx + b
        float dist,k,b;
        if (wallVector.getX() == 0){
            return Math.abs(wallEnd.getX() - location.getX());
        }
        if (wallVector.getY() == 0) {
            return Math.abs(wallEnd.getY() - location.getY());
        }
        k = wallVector.getY() / wallVector.getX();
        b = wallEnd.getY() - k* wallEnd.getX();
        dist = Math.abs(-k*location.getX() + location.getY() - b);
        dist /= Math.sqrt(k*k + 1);
        return dist;
    }
    
    private void applyWallCollision(Wall wall, float distance){
        Vector3f normal = new Vector3f();
        normal.set(wall.getNormal());
        System.out.println("WALLcollide");
        float help;
        float moveDistance = collisionDistance - distance;
//        System.out.println("mutliplierlength: " + moveDistance);
        Vector3f moveVector = new Vector3f();
        moveVector.set(normal);
        moveVector.scale(moveDistance);
        Vector3f.add(location, moveVector, location);

        help = (float) Math.cos(Vector3f.angle(speed, normal));
        help *= speed.length();
        help *= 2;
        
        normal.scale(Math.abs(help));
        accelerate(normal);
    }

    private void applyBallCollision(Vector3f normal,Ball ball){
        System.out.println("BallCollide");
        Vector3f tangent = new Vector3f();
        float v1n,v1t,v2n,v2t,nv1,nv2,dist = normal.length();
        normal.normalise();
        tangent.set(-normal.getY(), normal.getX());
        
        v1n = Vector3f.dot(normal, speed);
        v1t = Vector3f.dot(tangent, speed);
        v2n = Vector3f.dot(normal, ball.getSpeed());
        v2t = Vector3f.dot(tangent, ball.getSpeed());

        nv1 = v1n*(mass - ball.getMass()) + 2 *ball.getMass()*v2n;
        nv1 /= mass + ball.getMass();
        nv2 = v2n*(ball.getMass() - mass) + 2*mass*v1n;
        nv2 /= mass + ball.getMass();

        float moveDistance = collisionDistance - dist/2;
        Vector3f moveVector = new Vector3f();
        normal.normalise();
        moveVector.set(normal);
        moveVector.scale(moveDistance);
        Vector3f.add(location, moveVector, location);
        Vector3f.add(ball.getLocation(), (Vector3f) moveVector.negate(), ball.getLocation());


        normal.normalise();
        normal.scale(nv1);
        tangent.scale(v1t);
        Vector3f.add(normal,tangent,normal);
        setSpeed(normal);

        normal.normalise();
        tangent.normalise();
        normal.scale(nv2);
        tangent.scale(v2t);
        Vector3f.add(normal,tangent,normal);
        ball.setSpeed(normal);
        
    }

    private boolean checkBallCollisions(Ball ball) {
        Vector3f dist = new Vector3f();
        Vector3f check = new Vector3f();
        Vector3f.sub(location,ball.getLocation(),dist);
        
        if (dist.length() < radius + collisionDistance){
            applyBallCollision(dist,ball);
            return true;
        }
        return false;
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
            System.exit(2002);
        }
    }
    

    public final void set_coords(float x,float y,float z){
        location.set(x,y,z);
    }

    public void setSpeed(Vector3f speed) {
        this.speed.set(speed);
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

    public float getMass() {
        return mass;
    }
}
