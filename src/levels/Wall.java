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
public class Wall {
    ArrayList<float[]> vertice;
    String theme;
    public Wall(ArrayList<float[]> newVertice, String newTheme){
        vertice = newVertice;
        theme = newTheme;
        
    }
    
}
