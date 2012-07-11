/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maps;

import glomes.Statics;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author juho
 */
public class Wall {
    private String material;
    private float length, textureOffset;
    private LinkedList<GridSquare> squaresOccupied;
    private GridSquare[][] grid;
    private int displayListIndex;
    private Texture wallTexture;
    private Vector3f endPoint1, endPoint2, vector, normal;
    
    public Wall(float x1, float y1, float x2, float y2, String newMaterial, GridSquare[][] newGrid, int newDisplayListIndex){
        textureOffset = 0f; //Do something meaningful with this in the future!
        
        displayListIndex = newDisplayListIndex;
        squaresOccupied = new LinkedList();
        endPoint1 = new Vector3f(x1, y1, Statics.FLOOR_HEIGHT);
        endPoint2 = new Vector3f(x2, y2, Statics.FLOOR_HEIGHT);
        vector = Vector3f.sub(endPoint2, endPoint1, null);
        length = vector.length();
        
        material = newMaterial;
        grid = newGrid;
        occupyGrid();
        generateNormal();
        generateDisplayList();
    }
    private void occupyGrid(){
        //Tell all squares this wall intersects with to add the wall to collision checks.
        int increment = 1;
        int xInt1 = (int) endPoint1.getX();
        int yInt1 = (int) endPoint1.getY();
        int xInt2 = (int) endPoint2.getX();
        int yInt2 = (int) endPoint2.getY();
        int diff;
        
        if (yInt1 == yInt2){
            diff = Math.abs(xInt1 - xInt2);
            if (xInt2 < xInt1) {
                increment = -1;
            }
            for (int i = diff; i >= 0; i--) {
//                System.out.println(xInt1 + ", " + yInt1 + " horizontal");
                occupySquare(xInt1, yInt1);
                xInt1 += increment;

            }
        }else if (xInt1 == xInt2){
            diff = Math.abs(yInt1 - yInt2);
            if (yInt2 < yInt1) {
                increment = -1;
            }
            for (int i = diff; i >= 0; i--) {
//                System.out.println(xInt1 + ", " + yInt1 + " vertical");
                occupySquare(xInt1, yInt1);
                yInt1 += increment;
            }
        }else{
            occupyDiagonally();
        }
    }
    private void occupyDiagonally(){        
        //The following is pretty much copied from here: http://playtechs.blogspot.fi/2007/03/raytracing-on-grid.html
        //TODO: Figure out what actually happens here. I kinda get it, but the error variable is still a bit confusing.
        //Note: doesn't work for horizontal or vertical lines as I removed checks for them.
        
        float dx = Math.abs(vector.getX());
        float dy = Math.abs(vector.getY());

        int xInt1 = (int) endPoint1.getX();
        int yInt1 = (int) endPoint1.getY();
        int xInt2 = (int) endPoint2.getX();
        int yInt2 = (int) endPoint2.getY();

        int n = 1;
        int x_inc, y_inc;
        float error;
        
        n += Math.abs(xInt1 - xInt2) + Math.abs(yInt1 - yInt2);
        if (endPoint2.getX() > endPoint1.getX()) {
            x_inc = 1;
            error = (xInt1 + 1 - endPoint1.getX()) * dy;
        } else {
            x_inc = -1;
            error = (endPoint1.getX() - xInt1) * dy;
        }
        if (endPoint2.getY() > endPoint1.getY()) {
            y_inc = 1;
            error -= (yInt1 + 1 - endPoint1.getY()) * dx;
        } else {
            y_inc = -1;
            error -= (endPoint1.getY() - yInt1) * dx;
        }

        for (int i = n; i > 0; i--) {
            occupySquare(xInt1, yInt1);
//            System.out.println(xInt1 + ", " + yInt1 + " Diagonal");
            if (error > 0) {
                yInt1 += y_inc;
                error -= dx;
            } else {
                xInt1 += x_inc;
                error += dy;
            }
        }
    }
    private void clearFromGrid(){
        for (GridSquare square : squaresOccupied){
            try {
                square.removeWall(this);
            } catch (Exception ex) {
                Logger.getLogger(Wall.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1002);
            }
        }
        squaresOccupied.clear();
    }
    
    
    private void occupySquare(int x, int y){
        try{
            grid[x][y].occupyWall(this);
        }catch(Exception ex){
            System.out.println("Wall out of boundaries! A map vertex is outside the playable map area.");
            Logger.getLogger(Wall.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(2002);
        }
        
        squaresOccupied.add(grid[x][y]);
    }
    
    private void generateDisplayList(){
        
        try {
            wallTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("/res/test/wall_tiles_013.png"));
        } catch (IOException ex) {
            Logger.getLogger(Wall.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        float heightFactor = Statics.getTextureFactor(wallTexture.getTextureHeight());
        float widthFactor = Statics.getTextureFactor(wallTexture.getTextureWidth());
        
        GL11.glNewList(displayListIndex, GL11.GL_COMPILE); // Start With The List.
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f(normal.getX(), normal.getY(), normal.getZ());
        
        GL11.glTexCoord2f(
                (length + textureOffset) * widthFactor, 
                -Statics.FLOOR_HEIGHT * heightFactor);
        GL11.glVertex3f(
                endPoint2.getX(), 
                endPoint2.getY(), 
                Statics.FLOOR_HEIGHT);
        
        GL11.glTexCoord2f(
                (length + textureOffset) * widthFactor,
                -Statics.WALL_HEIGHT * heightFactor);
        GL11.glVertex3f(
                endPoint2.getX(),
                endPoint2.getY(),
                Statics.WALL_HEIGHT);

        GL11.glTexCoord2f(
                (0f + textureOffset) * widthFactor,
                -Statics.WALL_HEIGHT * heightFactor);
        GL11.glVertex3f(
                endPoint1.getX(),
                endPoint1.getY(),
                Statics.WALL_HEIGHT);

        GL11.glTexCoord2f(
                (0f + textureOffset) * widthFactor,
                -Statics.FLOOR_HEIGHT * heightFactor);
        GL11.glVertex3f(
                endPoint1.getX(),
                endPoint1.getY(),
                Statics.FLOOR_HEIGHT);

        GL11.glEnd();
        GL11.glEndList();
    }
    
    private void generateNormal(){
        //Important: If the vertice of an obstacle have been defined in a clockwise pattern, the 
        //normals generated here will point to the wrong way.
        
        //The Z component of the first vector is zero, and the X and Y components of the second vector are zero.
        normal = new Vector3f(vector.getY() / length, -vector.getX() / length, 0f);
//        System.out.println(vectorX + ", " + vectorY + "; normal: " + normalX + ", " + normalY + ".");
        
        
        //To refresh memory about cross product:
        /*
         * Begin Function CalculateSurfaceNormal (Input Triangle) Returns Vector
         *
         * Set Vector U to (Triangle.p2 minus Triangle.p1) Set Vector V to
         * (Triangle.p3 minus Triangle.p1)
         *
         * Set Normal.x to (multiply U.y by V.z) minus (multiply U.z by V.y) Set
         * Normal.y to (multiply U.z by V.x) minus (multiply U.x by V.z) Set
         * Normal.z to (multiply U.x by V.y) minus (multiply U.y by V.x)
         *
         * Returning Normal
         *
         * End Function
         */
    }
    
    public Vector3f getEnd(){
        return endPoint2;
    }

    public Vector3f getBeginning(){
        return endPoint1;
    }

    public Vector3f getNormal(){
        return normal;
    }
    
    public void draw(){
        wallTexture.bind();
        GL11.glCallList(displayListIndex);
    }
    
    public void remove(){
        clearFromGrid();
    }
    
}
