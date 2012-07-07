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
    ArrayList<float[]> vertice;
    String theme;
    public ObstacleData(ArrayList<float[]> newVertice, String newTheme){
        vertice = newVertice;
        theme = newTheme;
        
    }
    
}
