/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import levels.LevelData;
import levels.Wall;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

/**
 *
 * @author juho
 */
public class LevelLoader {
    Wini iniReader;
    Section section;
    List<Section> sectionList;
    LevelData levelData;
    ArrayList<Wall> walls;
    Wall wall;
    public LevelData loadLevel(String fileName){
        levelData = new LevelData();
        walls = new ArrayList();
        try {
            iniReader = new Wini();
            iniReader.getConfig().setMultiSection(true);
            iniReader.load(new File(fileName));
            
            
            handleGeneral();
            handleWalls();
//            handlePowerPlants();
//            handleLaboratories();
//            handlePlayerPositions();
            
            
        } catch (IOException ex) {
            Logger.getLogger(LevelLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        levelData.printData();
        return levelData;
    }
    private void handleGeneral(){
        section = iniReader.get("GENERAL");
        levelData.setTitle(section.get("title"));
        levelData.setPlayerAmount(Integer.parseInt(section.get("playerAmount")));
        levelData.setSize(Integer.parseInt(section.get("xSize")), Integer.parseInt(section.get("ySize")));
        
    }
    private void handleWalls(){
        sectionList = iniReader.getAll("WALL");
        for (int i = 0; i < sectionList.size(); i++){
            wall = new Wall(parseVertice(sectionList.get(i).get("vertice")), sectionList.get(i).get("theme"));
            walls.add(wall);
        }
        levelData.setWalls(walls);
    }
    private ArrayList<float[]> parseVertice(String verticeString){
        ArrayList<float[]> returnVertice = new ArrayList();
        System.out.println(verticeString);
        String subString = verticeString.trim().substring(1, verticeString.length() - 1);
        String[] vertexPairs = subString.split(";");
        String[] stringCoordinate;
        for (int i = 0; i < vertexPairs.length; i++){
            stringCoordinate = vertexPairs[i].split(",");
            float[] coordinatePair = {Float.parseFloat(stringCoordinate[0]), Float.parseFloat(stringCoordinate[1])};
            returnVertice.add(coordinatePair);
            System.out.println(coordinatePair[0] + ", " + coordinatePair[1]);
        }
        
        return returnVertice;
    }
}
