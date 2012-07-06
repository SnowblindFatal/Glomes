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
public class PowerPlant {
    float[] modelPosition;
    int energyProduction;
    ArrayList<float[]> verticeList;
    
    public PowerPlant(ArrayList<float[]> newVertice, float[] newModelPosition, int newEnergyProduction){
        verticeList = newVertice;
        energyProduction = newEnergyProduction;
        modelPosition = newModelPosition;
    }
    
}
