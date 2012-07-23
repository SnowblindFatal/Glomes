/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maps;

import glomes.Statics;
import java.util.ArrayList;
import java.util.LinkedList;
import misc.HelperFunctions;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;


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
        //TODO: create dynamic texture loading. ie. load the texture associated with the material.
        topTexture = Statics.textureMap.get("/res/test/Crate.bmp");
        
        double heightFactor = Statics.getTextureFactor(topTexture.getTextureHeight());
        double widthFactor = Statics.getTextureFactor(topTexture.getTextureWidth());
        double[][] drawVertice = new double[vertice.size()][3];
        for (int i = 0; i < vertice.size(); i++) {
            drawVertice[i] = new double[] {vertice.get(i)[0], vertice.get(i)[1] ,Statics.WALL_HEIGHT};
        }
        HelperFunctions.tesselatePolygon(drawVertice, widthFactor, heightFactor, displayListIndex);
    }

    
//    private void createTopOLD(){
//        try {
//            //TODO: create dynamic texture loading. ie. load the texture associated with the material.
//            topTexture = TextureLoader.getTexture("BMP", ResourceLoader.getResourceAsStream("/res/test/Crate.bmp"));
//        } catch (IOException ex) {
//            Logger.getLogger(Obstacle.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        float heightFactor = Statics.getTextureFactor(topTexture.getTextureHeight());
//        float widthFactor = Statics.getTextureFactor(topTexture.getTextureWidth());
//        
//        
//        PolygonPoint[] points = new PolygonPoint[vertice.size()];
//        for (int i = 0; i < vertice.size(); i++) {
//            points[i] = new PolygonPoint(vertice.get(i)[0], vertice.get(i)[1]);
//        }
//        Polygon topPolygon = new Polygon(points);
//        Poly2Tri.triangulate(topPolygon);
//        List<DelaunayTriangle> triangles = topPolygon.getTriangles();
//        TriangulationPoint[] newPoint;
//        
//        
//        
//        GL11.glNewList(displayListIndex, GL11.GL_COMPILE); // Start With The List.
//        GL11.glNormal3f(0f, 0f, 1f);
//        float x, y;
//        //Iterate through all triangles and draw the corresponding GL shapes.
//        for (int i = 0; i < triangles.size(); i++) {
//            newPoint = triangles.get(i).points;
//            GL11.glBegin(GL11.GL_TRIANGLES);
//            for (int j = 0; j < newPoint.length; j++) {
//                x = (float) newPoint[j].getXf();
//                y = (float) newPoint[j].getYf();
//                GL11.glTexCoord2f(x * widthFactor, -y * heightFactor);
//                GL11.glVertex3f(x, y, Statics.WALL_HEIGHT);
//            }
//            GL11.glEnd();
//        }
//        GL11.glEndList();
//    }
    
    public void draw(){
        for (Wall wall : walls){
            wall.draw();
        }
        topTexture.bind();
        GL11.glCallList(displayListIndex);
    }
    
}
