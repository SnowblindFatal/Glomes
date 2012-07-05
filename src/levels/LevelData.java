/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levels;

import java.util.ArrayList;

/**
 *
 * @author juho
 */
public class LevelData {
    String title;
    int playerAmount, xSize, ySize;
    ArrayList<Wall> walls;
    
    public LevelData(){
        
    }
    
    public void setTitle(String newTitle){
        title = newTitle;
    }
    public void setPlayerAmount(int newAmount){
        playerAmount = newAmount;
    }
    public void setSize(int x, int y){
        xSize = x;
        ySize = y;
    }
    public void setWalls(ArrayList<Wall> newWalls){
        walls = newWalls;
    }
    public void printData(){
        System.out.println(xSize + title + playerAmount);
    }
    
}
