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
public class Map {
    private int xSize, ySize, playerAmount;
    private String title;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<PowerPlant> powerPlants;
    private ArrayList<Laboratory> laboratories;
    
    public Map(MapData levelConfig){
        playerAmount = levelConfig.getPlayerAmount();
        xSize = levelConfig.getXSize();
        ySize = levelConfig.getYSize();
        title = levelConfig.getTitle();
        createObstacles(levelConfig.getObstaclesData());
        createPowerPlants(levelConfig.getPowerPlantsData());
        createLaboratories(levelConfig.getLaboratoriesData());
                
    }
    private void createObstacles(ArrayList<ObstacleData> obstacleDataList){
        Obstacle newObstacle;
        for (ObstacleData obstacleData : obstacleDataList){
            newObstacle = new Obstacle(obstacleData);
            obstacles.add(newObstacle);
        }
    }
    private void createPowerPlants(ArrayList<PowerPlantData> powerPlantDataList){
        PowerPlant newPowerPlant;
        for (PowerPlantData powerPlantData : powerPlantDataList) {
            newPowerPlant = new PowerPlant(powerPlantData);
            powerPlants.add(newPowerPlant);
        }
    }
    private void createLaboratories(ArrayList<LaboratoryData> laboratoryDataList){
        Laboratory newLaboratory;
        for (LaboratoryData laboratoryData : laboratoryDataList) {
            newLaboratory = new Laboratory(laboratoryData);
            laboratories.add(newLaboratory);
        }
    }
}
