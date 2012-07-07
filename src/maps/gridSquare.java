/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maps;

import balls.Ball;
import java.util.LinkedList;

/**
 *
 * @author juho
 */
public class GridSquare {
    boolean containsItems;
    LinkedList<Ball> balls;
    LinkedList<Corner> corners;
    LinkedList<Wall> walls;
    
    public GridSquare(){
        balls = new LinkedList();
        corners = new LinkedList();
        walls = new LinkedList();
    }
    public boolean hasItems(){
        return containsItems;
    }
    public void occupyWall(Wall newWall){
        walls.add(newWall);
        containsItems = true;
    }
    public void occupyCorner(Corner newCorner) {
        corners.add(newCorner);
        containsItems = true;
    }
    public void occupyBall(Ball newBall) {
        balls.add(newBall);
        containsItems = true;
    }
    
    public void removeWall(Wall wallToRemove) throws Exception{
        if (walls.remove(wallToRemove) == false){
            throw new Exception("Tried to vacate wall from square even though square does not have wall");
        }
    }
    
    public void removeCorner(Corner cornerToRemove) throws Exception {
        if (corners.remove(cornerToRemove) == false) {
            throw new Exception("Tried to vacate corner from square even though square does not have corner");
        }
    }
    
    public void removeBall(Ball ballToRemove) throws Exception {
        if (balls.remove(ballToRemove) == false) {
            throw new Exception("Tried to vacate ball from square even though square does not have ball");
        }
    }
}
