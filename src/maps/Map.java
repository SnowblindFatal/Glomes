/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maps;

import balls.Ball;
import java.util.ArrayList;
import java.util.LinkedList;


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
    private GridSquare[][] grid;
    private LinkedList<Ball> balls;
    
    public Map(MapData levelConfig){
        balls = new LinkedList();
        obstacles = new ArrayList();
        powerPlants = new ArrayList();
        laboratories = new ArrayList();
        playerAmount = levelConfig.getPlayerAmount();
        xSize = levelConfig.getXSize();
        ySize = levelConfig.getYSize();
        title = levelConfig.getTitle();
        grid = new GridSquare[xSize + 1][ySize + 1];
        initialiseGrid();
        createObstacles(levelConfig.getObstaclesData());
        createPowerPlants(levelConfig.getPowerPlantsData());
        createLaboratories(levelConfig.getLaboratoriesData());
    }
    public GridSquare getSquareAt(int x, int y){
        return grid[x][y];
    }
    private void initialiseGrid(){
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                grid[i][j] = new GridSquare();
            }
        }
        System.out.println(grid[0][0].hasItems());
    }
    private void createObstacles(ArrayList<ObstacleData> obstacleDataList){
        Obstacle newObstacle;
        for (ObstacleData obstacleData : obstacleDataList){
            newObstacle = new Obstacle(obstacleData, grid);
            obstacles.add(newObstacle);
        }
    }
    private void createPowerPlants(ArrayList<PowerPlantData> powerPlantDataList){
        PowerPlant newPowerPlant;
        for (PowerPlantData powerPlantData : powerPlantDataList) {
            newPowerPlant = new PowerPlant(powerPlantData, grid);
            powerPlants.add(newPowerPlant);
        }
    }
    private void createLaboratories(ArrayList<LaboratoryData> laboratoryDataList){
        Laboratory newLaboratory;
        for (LaboratoryData laboratoryData : laboratoryDataList) {
            newLaboratory = new Laboratory(laboratoryData, grid);
            laboratories.add(newLaboratory);
        }
    }
    
    public void update(){
        for (Ball ball : balls) {
            ball.update();
        }
    }
    
    public void draw(){
        for (Obstacle obstacle : obstacles){
            obstacle.draw();
        }
        for (Ball ball : balls){
            ball.draw();
        }
    }
    public GridSquare[][] getGrid(){
        return grid;
    }
    
    
    //Some test methods.
    public void addBall(Ball newBall){
        balls.add(newBall);
    }
    public void accelerateBall(float x, float y, float z){
        if (balls.size() > 0){
            balls.getFirst().accelerate(x, y, z);
        }
    }
}
