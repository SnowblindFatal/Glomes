/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maps;

import glomes.Statics;
import java.io.IOException;
import java.util.ArrayList;
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
public class Obstacle {
    private String material;
    private LinkedList<Wall> walls;
    private LinkedList<Corner> corners;
    private GridSquare[][] grid;
    private ArrayList<float[]> vertice;
    private Texture topTexture;
    private int displayListIndex;
    public Obstacle(ObstacleData newObstacleData, GridSquare[][] newGrid){
        grid = newGrid;
        walls = new LinkedList();
        corners = new LinkedList();
        vertice = newObstacleData.getVertice();
        Corner newCorner;
        Wall newWall;
        material = newObstacleData.getMaterial();
        boolean first = true;
        float[] firstPair = null;
        float[] previousPair = null;
        //The displayListIndex "pointer" will first assign lists to the walls after which
        //it will be used to point to the top of the obstacle.
        displayListIndex = GL11.glGenLists(vertice.size() + 1);
//        System.out.println("newobstacle");
        
        for (float[] vertexPair: vertice){
            newCorner = new Corner(vertexPair[0], vertexPair[1], material, grid);
            
            corners.add(newCorner);
            
            if (first == true){
                first = false;
                firstPair = vertexPair;
            }
            else{
                newWall = new Wall(previousPair[0], previousPair[1], vertexPair[0], vertexPair[1], material, grid, displayListIndex);
                displayListIndex++;
//                System.out.println(previousPair[0] + ", " + previousPair[1] + ", " + vertexPair[0] + ", " + vertexPair[1]);
                walls.add(newWall);
            }
            
            previousPair = vertexPair;
            
        }
        
        newWall = new Wall(previousPair[0], previousPair[1], firstPair[0], firstPair[1], material, grid, displayListIndex);
        displayListIndex++;
//        System.out.println(previousPair[0] + ", " + previousPair[1] + ", " + firstPair[0] + ", " + firstPair[1]);
        walls.add(newWall);
        createTop();
    }
    
    private void createTop(){
        try {
            //TODO: create dynamic texture loading. ie. load the texture associated with the material.
            topTexture = TextureLoader.getTexture("BMP", ResourceLoader.getResourceAsStream("/res/test/Crate.bmp"));
        } catch (IOException ex) {
            Logger.getLogger(Obstacle.class.getName()).log(Level.SEVERE, null, ex);
        }
        //GL_POLYGON doesn't work properly. Gotta find triangulation library after all. Bloody hell.
        GL11.glNewList(displayListIndex, GL11.GL_COMPILE); // Start With The List.
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glNormal3f(0f, 0f, 1f);
//        for (float[] vertex : vertice){
//            System.out.println(vertex[0] + ", " + vertex[1]);
//            GL11.glTexCoord2f(vertex[0], -vertex[1]);
//            GL11.glVertex3f(vertex[0], vertex[1], Statics.WALL_HEIGHT);
//        }
        
//        GL11.glTexCoord2f(50f, 20.0f);
//        GL11.glVertex3f(50f, 20.0f, Statics.WALL_HEIGHT);
//        GL11.glTexCoord2f(80f, 20.0f);
//        GL11.glVertex3f(80f, 20.0f, Statics.WALL_HEIGHT);
//        GL11.glTexCoord2f(80f, 50.0f);
//        GL11.glVertex3f(80f, 50.0f, Statics.WALL_HEIGHT);
//        GL11.glVertex3f(60f, 50.0f, Statics.WALL_HEIGHT);
//        GL11.glVertex3f(60f, 60.0f, Statics.WALL_HEIGHT);
//        GL11.glVertex3f(70f, 60.0f, Statics.WALL_HEIGHT);
//        GL11.glVertex3f(70f, 70.0f, Statics.WALL_HEIGHT);
//        GL11.glVertex3f(50f, 70.0f, Statics.WALL_HEIGHT);
        
//        GL11.glTexCoord2f(141.5f, 20.0f);
//        GL11.glVertex3f(141.5f, 20.0f, Statics.WALL_HEIGHT);
//        GL11.glTexCoord2f(158.5f, 20.0f);
//        GL11.glVertex3f(158.5f, 20.0f, Statics.WALL_HEIGHT);
//        GL11.glTexCoord2f(170.0f, 70.0f);
//        GL11.glVertex3f(170.0f, 70.0f, Statics.WALL_HEIGHT);
//        GL11.glTexCoord2f(130.0f, 70.0f);
//        GL11.glVertex3f(130.0f, 70.0f, Statics.WALL_HEIGHT);
//        
//        GL11.glTexCoord2f(130.0f, 75.0f);
//        GL11.glVertex3f(130.0f, 75.0f, Statics.WALL_HEIGHT);
//        GL11.glTexCoord2f(140.0f, 75.0f);
//        GL11.glVertex3f(140.0f, 75.0f, Statics.WALL_HEIGHT);
//        GL11.glTexCoord2f(140.0f, 71.0f);
//        GL11.glVertex3f(140.0f, 71.0f, Statics.WALL_HEIGHT);
//        GL11.glTexCoord2f(150.0f, 71.0f);
//        GL11.glVertex3f(150.0f, 71.0f, Statics.WALL_HEIGHT);
//        GL11.glTexCoord2f(150.0f, 80.0f);
//        GL11.glVertex3f(150.0f, 80.0f, Statics.WALL_HEIGHT);
//        GL11.glTexCoord2f(110.0f, 80.0f);
//        GL11.glVertex3f(110.0f, 80.0f, Statics.WALL_HEIGHT);
        
        
        GL11.glEnd();
        GL11.glEndList();
    }
    
    public void draw(){
        for (Wall wall : walls){
            wall.draw();
        }
        topTexture.bind();
        GL11.glCallList(displayListIndex);
    }
    
}
