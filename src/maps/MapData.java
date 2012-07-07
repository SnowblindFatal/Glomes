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
    private int playerAmount;
    private int xSize;
    private int ySize;
    private ArrayList<ObstacleData> obstaclesData;
    private ArrayList<PowerPlantData> powerPlantsData;
    private ArrayList<LaboratoryData> laboratoriesData;
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
        System.out.println(title);
    }
    public void addPlayerPosition(float[] newPosition){
        playerPositions.add(newPosition);
    }
    public String getTitle(){
        return title;
    }
    public int getPlayerAmount() {
        return playerAmount;
    }
    public int getXSize() {
        return xSize;
    }
    public int getYSize() {
        return ySize;
    }
    public ArrayList<ObstacleData> getObstaclesData() {
        return obstaclesData;
    }
    public ArrayList<PowerPlantData> getPowerPlantsData() {
        return powerPlantsData;
    }
    public ArrayList<LaboratoryData> getLaboratoriesData() {
        return laboratoriesData;
    }
}
