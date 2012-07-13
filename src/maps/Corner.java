/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maps;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author juho
 */
public class Corner {
    private String material;
    private Vector3f location;
    private GridSquare[][] grid;
    private GridSquare currentSquare;
    public Corner(float x, float y, String newMaterial, GridSquare[][] newGrid){
        material = newMaterial;
        location = new Vector3f(x, y, 0f);
        grid = newGrid;
        occupySquare();
    }
    private void occupySquare(){
        int x = (int)location.getX(), y = (int)location.getY();
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
    public Vector3f getLocation(){
        return location;
    }
}
