/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maps;

import balls.Ball;
import glomes.Statics;
import java.util.ArrayList;
import java.util.LinkedList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;


/**
 *
 * @author juho
 */
public class Map {
    private int xSize, ySize, playerAmount, displayListIndex;
    private String title;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<PowerPlant> powerPlants;
    private ArrayList<Laboratory> laboratories;
    private GridSquare[][] grid;
    private LinkedList<Ball> balls;
    private Texture floorTexture;
    
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
        initialiseFloor();
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
    private void initialiseFloor(){
        //TODO: Support for areas with different types of floor with different properties and textures.
        floorTexture = Statics.textureMap.get("Cube.bmp");
        
        float widthFactor = Statics.getTextureFactor(floorTexture.getTextureWidth());
        float heightFactor = Statics.getTextureFactor(floorTexture.getTextureHeight());
        
        
        displayListIndex = GL11.glGenLists(1);

        GL11.glNewList(displayListIndex, GL11.GL_COMPILE); // Start With The List.
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f(0f, 0f, 1f);

        GL11.glTexCoord2f(
                10f * widthFactor, 
                - 10f * heightFactor);
        GL11.glVertex3f(
                10f, 
                10f, 
                Statics.FLOOR_HEIGHT);
        
        GL11.glTexCoord2f(
                (float) (xSize - 20) * widthFactor, 
                - 10f * heightFactor);
        GL11.glVertex3f(
                (float) (xSize - 20), 
                10f, 
                Statics.FLOOR_HEIGHT);
        
        GL11.glTexCoord2f(
                (float) (xSize - 20) * widthFactor, 
                -((float) (ySize - 20)) * heightFactor);
        GL11.glVertex3f(
                (float) (xSize - 20), 
                (float) (ySize - 20), 
                Statics.FLOOR_HEIGHT);
        
        GL11.glTexCoord2f(
                10f * widthFactor, 
                - ((float) (ySize - 20)) * heightFactor);
        GL11.glVertex3f(
                10f, 
                (float) (ySize - 20), 
                Statics.FLOOR_HEIGHT);

        GL11.glEnd();
        GL11.glEndList();
        
        
    }
    
    public void update(float dTime){
        for (Ball ball : balls) {
            ball.update(dTime);
        }
    }
    
    public void draw(float dTime){
        drawFloor();
        for (Obstacle obstacle : obstacles){
            obstacle.draw();
        }
        for (Ball ball : balls){
            ball.draw(dTime);
        }
    }
    private void drawFloor(){
        
        floorTexture.bind();
        GL11.glCallList(displayListIndex);
    }
    
    public GridSquare[][] getGrid(){
        return grid;
    }
    
    
    //Some test methods.
    public void addBall(Ball newBall){
        balls.add(newBall);
    }

    public void removeBall(Ball ball){
        balls.removeFirstOccurrence(ball);
    }

    public void accelerateBall(float x, float y, float z){
        if (balls.size() > 0){
            balls.getFirst().accelerate(x, y, z);
        }
    }
    public void stopBall(){
        if (balls.size() > 0) {
            balls.getFirst().setSpeed(new Vector3f());
        }
    }
}
