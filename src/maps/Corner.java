/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maps;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juho
 */
public class Corner {
    private String material;
    private float xCoordinate, yCoordinate;
    private GridSquare[][] grid;
    private GridSquare currentSquare;
    public Corner(float x, float y, String newMaterial, GridSquare[][] newGrid){
        material = newMaterial;
        xCoordinate = x;
        yCoordinate = y;
        grid = newGrid;
        occupySquare();
    }
    private void occupySquare(){
        int x = (int)xCoordinate, y = (int)yCoordinate;
        currentSquare = grid[x][y];
        currentSquare.occupyCorner(this);
    }
    public void remove(){
        try {
            currentSquare.removeCorner(this);
        } catch (Exception ex) {
            Logger.getLogger(Corner.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }
}
