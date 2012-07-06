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
    ArrayList<PowerPlant> powerPlants;
    ArrayList<Laboratory> laboratories;
    ArrayList<float[]> playerPositions;
    public LevelData(){
        playerPositions = new ArrayList();
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
    public void setPowerPlants(ArrayList<PowerPlant> newPowerPlants){
        powerPlants = newPowerPlants;
    }
    public void setLaboratories(ArrayList<Laboratory> newLaboratories){
        laboratories = newLaboratories;
    }
    public void printData(){
        System.out.println(xSize + title + playerAmount);
    }
    public void addPlayerPosition(float[] newPosition){
        playerPositions.add(newPosition);
    }
    
}
