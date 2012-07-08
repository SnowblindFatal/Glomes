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
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author juho
 */
public class Wall {
    private String material;
    private float xCoordinate1, yCoordinate1, xCoordinate2, yCoordinate2, length, vectorX, vectorY, normalX, normalY, normalZ;
    private LinkedList<GridSquare> squaresOccupied;
    private GridSquare[][] grid;
    private int displayListIndex;
    private Texture wallTexture;
    
    public Wall(float x1, float y1, float x2, float y2, String newMaterial, GridSquare[][] newGrid, int newDisplayListIndex){
        displayListIndex = newDisplayListIndex;
        squaresOccupied = new LinkedList();
        xCoordinate1 = x1;
        yCoordinate1 = y1;
        xCoordinate2 = x2;
        yCoordinate2 = y2;
        
        vectorX = xCoordinate2 - xCoordinate1;
        vectorY = yCoordinate2 - yCoordinate1;
        length = (float) (Math.sqrt(vectorY * vectorY + vectorX * vectorX));
        
        material = newMaterial;
        grid = newGrid;
        occupyGrid();
        generateNormal();
        try {
            wallTexture = TextureLoader.getTexture("BMP", ResourceLoader.getResourceAsStream("/res/test/Mud.bmp"));
        } catch (IOException ex) {
            Logger.getLogger(Wall.class.getName()).log(Level.SEVERE, null, ex);
        }
        generateDisplayList();
    }
    private void occupyGrid(){
        //Tell all squares this wall intersects with to add the wall to collision checks.
        int increment = 1;
        int xInt1 = (int) xCoordinate1;
        int yInt1 = (int) yCoordinate1;
        int xInt2 = (int) xCoordinate2;
        int yInt2 = (int) yCoordinate2;
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
        
        float dx = Math.abs(xCoordinate2 - xCoordinate1);
        float dy = Math.abs(yCoordinate2 - yCoordinate1);

        int xInt1 = (int) xCoordinate1;
        int yInt1 = (int) yCoordinate1;
        int xInt2 = (int) xCoordinate2;
        int yInt2 = (int) yCoordinate2;

        int n = 1;
        int x_inc, y_inc;
        float error;
        
        n += Math.abs(xInt1 - xInt2) + Math.abs(yInt1 - yInt2);
        if (xCoordinate2 > xCoordinate1) {
            x_inc = 1;
            error = (xInt1 + 1 - xCoordinate1) * dy;
        } else {
            x_inc = -1;
            error = (xCoordinate1 - xInt1) * dy;
        }
        if (yCoordinate2 > yCoordinate1) {
            y_inc = 1;
            error -= (yInt1 + 1 - yCoordinate1) * dx;
        } else {
            y_inc = -1;
            error -= (yCoordinate1 - yInt1) * dx;
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
                System.exit(0);
            }
        }
        squaresOccupied.clear();
    }
    
    
    private void occupySquare(int x, int y){
        grid[x][y].occupyWall(this);
        squaresOccupied.add(grid[x][y]);
    }
    
    private void generateDisplayList(){
        GL11.glNewList(displayListIndex, GL11.GL_COMPILE); // Start With The List.
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f(normalX, normalY, normalZ);
        
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(xCoordinate1, yCoordinate1, Statics.FLOOR_HEIGHT);
        
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(xCoordinate1, yCoordinate1, Statics.WALL_HEIGHT);
        
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f(xCoordinate2, yCoordinate2, Statics.WALL_HEIGHT);
        
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f(xCoordinate2, yCoordinate2, Statics.FLOOR_HEIGHT);
        
        GL11.glEnd();
        GL11.glEndList();
    }
    
    private void generateNormal(){
        //Important: If the vertice of an obstacle have been defined in a clockwise pattern, the 
        //normals generated here will point to the wrong way.
        
        //The Z component of the first vector is zero, and the X and Y components of the second vector are zero.
        
        
//        float vector2Z = 1f;
        
        normalX = vectorY / length;
        normalY = -vectorX / length;
        normalZ = 0f;
        System.out.println(vectorX + ", " + vectorY + "; normal: " + normalX + ", " + normalY + ".");
        
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
    
    
    
    public void draw(){
        wallTexture.bind();
        GL11.glCallList(displayListIndex);
    }
    
    public void remove(){
        clearFromGrid();
    }
    
}
