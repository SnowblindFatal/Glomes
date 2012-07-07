/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maps;

import java.util.ArrayList;

/**
 *
 * @author juho
 */
public class MapData {
    String title;
    int playerAmount, xSize, ySize;
    ArrayList<ObstacleData> obstaclesData;
    ArrayList<PowerPlantData> powerPlantsData;
    ArrayList<LaboratoryData> laboratoriesData;
    ArrayList<float[]> playerPositions;
    public MapData(){
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
    public void setWalls(ArrayList<ObstacleData> newObstacles){
        obstaclesData = newObstacles;
    }
    public void setPowerPlants(ArrayList<PowerPlantData> newPowerPlants){
        powerPlantsData = newPowerPlants;
    }
    public void setLaboratories(ArrayList<LaboratoryData> newLaboratories){
        laboratoriesData = newLaboratories;
    }
    public void printData(){
        System.out.println(xSize + title + playerAmount);
    }
    public void addPlayerPosition(float[] newPosition){
        playerPositions.add(newPosition);
    }
    
}
