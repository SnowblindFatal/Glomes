/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maps;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juho
 */
public class Wall {
    String material;
    float xCoordinate1, yCoordinate1, xCoordinate2, yCoordinate2;
    LinkedList<GridSquare> squaresOccupied;
    private GridSquare[][] grid;
    
    public Wall(float x1, float y1, float x2, float y2, String newMaterial, GridSquare[][] newGrid){
        squaresOccupied = new LinkedList();
        xCoordinate1 = x1;
        yCoordinate1 = y1;
        xCoordinate2 = x2;
        yCoordinate2 = y2;
        
        material = newMaterial;
        grid = newGrid;
        occupyGrid();
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
    
    
    
    public void draw(){
        
    }
    
    public void remove(){
        clearFromGrid();
    }
    
}
