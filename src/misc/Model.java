/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package misc;
/**
 *
 * @author Jusku
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Model {
    ArrayList<Vector3f> vertices = new ArrayList();
    ArrayList<Vector3f> normals = new ArrayList();
    ArrayList<Vector3f> tCoords = new ArrayList();
    ArrayList<String> mtlName = new ArrayList();
    ArrayList<int[]> polygonMaker = new ArrayList();
    String mtlFile = "";
    Texture texture;
    int dlIndex;

    enum choice{
        vertex,
        normal,
        texture;
    }

    InputStream fileIn;
    DataInputStream inputStream;
    BufferedReader reader;

    public Model(){}

    public void load(String fileName){
        char[] input = null;
        String[] str = new String[1];
        ArrayList<String[]> lines = new ArrayList();
        int i = 0,j = 0;
        boolean done = false;
        dlIndex = GL11.glGenLists(1);

        try{
            fileIn = new FileInputStream(fileName);
            inputStream = new DataInputStream(fileIn);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            
            input = new char[inputStream.available()];
            reader.read(input);
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        str[0] = "";
        for (i = 0; i < input.length;i++){
            if (input[i] == '\n') {
                str = str[0].split(String.valueOf(' '));
                lines.add(str);
                str = new String[1];
                str[0] = "";
                j++;
                continue;
            }
            str[0] += input[i];
        }
        
        for (i = 0;i < j;i++){
            str = lines.get(i);
            if (str[0].equals("mtlfile")) mtlFile += str[1];
            else if (str[0].equals("usemtl")) mtlName.add(str[1]);
            else if(str[0].equals("v")) createVertex(str, choice.vertex);
            else if (str[0].equals("vn")) createVertex(str,choice.normal);
            else if (str[0].equals("vt")) createVertex(str,choice.texture);
            else if (str[0].equals("f")) readPolygonInfo(str);
        }
        createDisplayList();
    }

    private void createVertex(String[] info, choice choice){
        float[] point = new float[3];
        
        for (int i = 1;i < 4;i++){
            point[i-1] = Float.parseFloat(info[i]);
            if (i == 2 && choice == choice.texture) break;
        }

        switch(choice){
            case vertex:
                vertices.add(new Vector3f(point[0],point[1],point[2]));
                break;
            case normal:
                normals.add(new Vector3f(point[0],point[1],point[2]));
                break;
            case texture:
                tCoords.add(new Vector3f(point[0],point[1],0f));
                break;
        }
    }

    private void readPolygonInfo(String[] str){
        int i = 0,j = 0,neg = (int) '0';
        int[] values = new int[9];
        for (i = 1;i < 4;i++) {
            values[j++] = (int)str[i].charAt(0)-neg;
            values[j++] = (int)str[i].charAt(2)-neg;
            values[j++] = (int)str[i].charAt(4)-neg;
        }
        polygonMaker.add(values);
    }

    public void draw(){
        texture.bind();
        GL11.glCallList(dlIndex);
    }

    private void createDisplayList() {
        try {
            texture = TextureLoader.getTexture("BMP", ResourceLoader.getResourceAsStream(mtlFile));
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }

        Vector3f help = new Vector3f();        
        GL11.glNewList(dlIndex, GL11.GL_COMPILE);        
        for (int i = 0;i < polygonMaker.size();i++){
            help = normals.get(polygonMaker.get(i)[2]-1);
            GL11.glNormal3f(help.getX(),help.getY(),help.getZ());
            GL11.glBegin(GL11.GL_TRIANGLES);
            for (int j = 0;j < 3;j++){
                help = tCoords.get(polygonMaker.get(i)[3*j+1]-1);
                GL11.glTexCoord2f(help.getX(), help.getY());
                help = vertices.get(polygonMaker.get(i)[3*j]-1);
                GL11.glVertex3f(help.getX(), help.getY(), help.getZ());
            }
            GL11.glEnd();
        }
        GL11.glEndList();
    }
}
