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
public class Laboratory {
    float[] modelPosition;
    String techToPick;
    ArrayList<float[]> verticeList;

    public Laboratory(ArrayList<float[]> newVertice, float[] newModelPosition, String pickableTech) {
        verticeList = newVertice;
        techToPick = pickableTech;
        modelPosition = newModelPosition;
    }
}
