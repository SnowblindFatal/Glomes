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
import maps.LaboratoryData;
import maps.MapData;
import maps.PowerPlantData;
import maps.ObstacleData;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

/**
 *
 * @author juho
 */
public class MapLoader {
    Wini iniReader;
    MapData mapData;
    
    
    
    public MapData loadMap(String fileName){
        mapData = new MapData();
        File file = new File(fileName);
        try {
            iniReader = new Wini();
            iniReader.getConfig().setMultiSection(true);
            iniReader.load(file);
            
            
            handleGeneral();
            handleWalls();
            handlePowerPlants();
            handleLaboratories();
            handlePlayers();
            
            
        } catch (IOException ex) {
            Logger.getLogger(MapLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
//        mapData.printData();
        return mapData;
    }
    
    
    
    
    
    private void handleGeneral(){
        Section section = iniReader.get("GENERAL");
        mapData.setTitle(section.get("title"));
        mapData.setPlayerAmount(Integer.parseInt(section.get("playerAmount")));
        mapData.setSize(Integer.parseInt(section.get("xSize")), Integer.parseInt(section.get("ySize")));
        
    }
    
    
    private void handleWalls(){
        List<Section> sectionList = iniReader.getAll("OBSTACLE");
        String theme;
        ArrayList<float[]> verticeList;
        ArrayList<ObstacleData> wallList = new ArrayList();
        ObstacleData wall;
        for (int i = 0; i < sectionList.size(); i++){
            verticeList = parseVerticeArray(sectionList.get(i).get("vertice"));
            theme = sectionList.get(i).get("theme");
            wall = new ObstacleData(verticeList, theme);
            wallList.add(wall);
        }
        mapData.setWalls(wallList);
    }
    
    
    private void handlePowerPlants(){
        List<Section> sectionList = iniReader.getAll("POWER_PLANT");
        float[] position;
        int energy;
        ArrayList<float[]> verticeList;
        PowerPlantData powerPlant;
        ArrayList<PowerPlantData> powerPlantList = new ArrayList();
        for (int i = 0; i < sectionList.size(); i++) {
            verticeList = parseVerticeArray(sectionList.get(i).get("vertice"));
            position = parseVertice(sectionList.get(i).get("modelLocation"));
            energy = Integer.parseInt(sectionList.get(i).get("energy"));
            powerPlant = new PowerPlantData(verticeList, position, energy);
            powerPlantList.add(powerPlant);
        }
        mapData.setPowerPlants(powerPlantList);
    }
    
    
    private void handleLaboratories(){
        List<Section> sectionList = iniReader.getAll("LABORATORY");
        float[] position;
        String tech;
        ArrayList<float[]> verticeList;
        LaboratoryData laboratory;
        ArrayList<LaboratoryData> laboratoryList = new ArrayList();
        for (int i = 0; i < sectionList.size(); i++) {
            verticeList = parseVerticeArray(sectionList.get(i).get("vertice"));
            position = parseVertice(sectionList.get(i).get("modelLocation"));
            tech = sectionList.get(i).get("tech");
            laboratory = new LaboratoryData(verticeList, position, tech);
            laboratoryList.add(laboratory);
        }
        mapData.setLaboratories(laboratoryList);
    }
    
    
    private void handlePlayers(){
        List<Section> sectionList = iniReader.getAll("PLAYER");
        float[] position;
        for (int i = 0; i < sectionList.size(); i++) {
            position = parseVertice(sectionList.get(i).get("position"));
//            System.out.println(position[0] + ", " + position[1]);
            mapData.addPlayerPosition(position);
        }
    }
    
    
    
    
    private float[] parseVertice(String verticeString){
        //Parses a pair of coordinates such as "(3, 94.2)" to float array.
        String subString = verticeString.trim().substring(1, verticeString.length() - 1);
        String[] stringCoordinate = subString.split(",");
        float[] coordinatePair = {Float.parseFloat(stringCoordinate[0]), Float.parseFloat(stringCoordinate[1])};
//        System.out.println(Float.parseFloat(stringCoordinate[1]));
        return coordinatePair;
    }
    
    
    private ArrayList<float[]> parseVerticeArray(String verticeString){
        ArrayList<float[]> returnVertice = new ArrayList();
//        System.out.println(verticeString);
        String subString = verticeString.trim().substring(1, verticeString.length() - 1);
        String[] vertexPairs = subString.split(";");
        String[] stringCoordinate;
        for (int i = 0; i < vertexPairs.length; i++){
            stringCoordinate = vertexPairs[i].split(",");
            float[] coordinatePair = {Float.parseFloat(stringCoordinate[0]), Float.parseFloat(stringCoordinate[1])};
            returnVertice.add(coordinatePair);
//            System.out.println(coordinatePair[0] + ", " + coordinatePair[1]);
        }
        return returnVertice;
    }
}
