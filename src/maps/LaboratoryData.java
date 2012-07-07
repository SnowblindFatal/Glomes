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
public class LaboratoryData {
    float[] modelPosition;
    String techToPick;
    ArrayList<float[]> verticeList;

    public LaboratoryData(ArrayList<float[]> newVertice, float[] newModelPosition, String pickableTech) {
        verticeList = newVertice;
        techToPick = pickableTech;
        modelPosition = newModelPosition;
    }
}
