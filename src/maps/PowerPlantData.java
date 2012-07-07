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
public class PowerPlantData {
    float[] modelPosition;
    int energyProduction;
    ArrayList<float[]> verticeList;
    
    public PowerPlantData(ArrayList<float[]> newVertice, float[] newModelPosition, int newEnergyProduction){
        verticeList = newVertice;
        energyProduction = newEnergyProduction;
        modelPosition = newModelPosition;
    }
    
}
