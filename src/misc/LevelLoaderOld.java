/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class LevelLoaderOld {
    //THIS IS ONLY HALF DONE
    //Don't even try to use.
    FileInputStream fileStream;
    DataInputStream inputStream;
    BufferedReader bufferedReader;
    String currentLine, currentFile;
    Map config;
    boolean done, properMap;

    
    public LevelLoaderOld(){

    }
    public Map loadLevel(String levelFile) throws Exception{
        initReading(levelFile);
        
        while (done = false){
            if (currentLine.equals("")){
            }
            else if (currentLine.startsWith("[")){
                currentLine = currentLine.substring(1, currentLine.length()- 2);
                
            }
            
            
            nextLine();
        }
        
        return config;
    }
    private void initReading(String levelFile){
        currentFile = levelFile;
        done = false;
        properMap = false;
        
        config = new HashMap();


        try {
            fileStream = new FileInputStream("leveldata/" + currentFile);
        } catch (Exception ex) {
            System.out.println("Error opening file: " + currentFile);
        }
        inputStream = new DataInputStream(fileStream);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            currentLine = bufferedReader.readLine().trim();
        } catch (Exception ex) {
            System.out.println("Empty level file: " + currentFile);
        }
    }
    private void nextLine() throws Exception{
        try {
            currentLine = bufferedReader.readLine().trim();
        } catch (Exception ex) {
            if (properMap == true) {
                done = true;
            } else {
                System.out.println("Level file invalid: " + currentFile);
                throw new Exception();
            }
        }
    }
}
