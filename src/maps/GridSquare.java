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
    private LinkedList<Ball> balls;
    private LinkedList<Corner> corners;
    private LinkedList<Wall> walls;
    
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
        checkIfEmpty();
    }
    
    public void removeCorner(Corner cornerToRemove) throws Exception {
        if (corners.remove(cornerToRemove) == false) {
            throw new Exception("Tried to vacate corner from square even though square does not have corner");
        }
        checkIfEmpty();
    }
    
    public void removeBall(Ball ballToRemove) throws Exception {
        if (balls.remove(ballToRemove) == false) {
            throw new Exception("Tried to vacate ball from square even though square does not have ball");
        }
        checkIfEmpty();
    }
    private void checkIfEmpty(){
        if (walls.size() == 0 && balls.size() == 0 && corners.size() == 0) {
            containsItems = false;
        }
    }

    public LinkedList<Ball> getBalls() {
        return balls;
    }

    public LinkedList<Corner> getCorners() {
        return corners;
    }

    public LinkedList<Wall> getWalls() {
        return walls;
    }
}
