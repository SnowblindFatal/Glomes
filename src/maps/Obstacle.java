/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maps;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author juho
 */
public class Obstacle {
    private String material;
    private LinkedList<Wall> walls;
    private LinkedList<Corner> corners;
    private GridSquare[][] grid;
    public Obstacle(ObstacleData newObstacleData, GridSquare[][] newGrid){
        grid = newGrid;
        walls = new LinkedList();
        corners = new LinkedList();
        ArrayList<float[]> vertice = newObstacleData.getVertice();
        Corner newCorner;
        Wall newWall;
        material = newObstacleData.getMaterial();
        boolean first = true;
        float[] firstPair = null;
        float[] previousPair = null;
//        System.out.println("newobstacle");
        
        for (float[] vertexPair: vertice){
            newCorner = new Corner(vertexPair[0], vertexPair[1], material, grid);
            
            corners.add(newCorner);
            
            if (first == true){
                first = false;
                firstPair = vertexPair;
            }
            else{
                newWall = new Wall(previousPair[0], previousPair[1], vertexPair[0], vertexPair[1], material, grid);
//                System.out.println(previousPair[0] + ", " + previousPair[1] + ", " + vertexPair[0] + ", " + vertexPair[1]);
                walls.add(newWall);
            }
            
            previousPair = vertexPair;
            
        }
        
        newWall = new Wall(previousPair[0], previousPair[1], firstPair[0], firstPair[1], material, grid);
//        System.out.println(previousPair[0] + ", " + previousPair[1] + ", " + firstPair[0] + ", " + firstPair[1]);
        walls.add(newWall);
    }
    
}
