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
public class ObstacleData {
    private ArrayList<float[]> vertice;
    private String material;
    public ObstacleData(ArrayList<float[]> newVertice, String newMaterial){
        vertice = newVertice;
        material = newMaterial;
    }

    public ArrayList<float[]> getVertice() {
        return vertice;
    }

    public String getMaterial() {
        return material;
    }
    
}
