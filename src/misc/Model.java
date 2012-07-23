/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package misc;
/**
 *
 * @author Jusku
 */
import glomes.Statics;
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

public class Model {
    ArrayList<Vector3f> vertices = new ArrayList();
    ArrayList<Vector3f> normals = new ArrayList();
    ArrayList<Vector3f> tCoords = new ArrayList();
    ArrayList<String> mtlName = new ArrayList();
    ArrayList<int[]> polygonMaker = new ArrayList();
    String textureFile = "";
    Texture texture;
    int dlIndex;
    float scale;

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
        parseString(fileName);
        System.out.println(Statics.textureMap.containsKey(textureFile));
        System.out.println(Statics.textureMap.get(textureFile));
        texture = Statics.textureMap.get(textureFile);
        createDisplayList();
    }
    
    private void parseString(String fileName){
        char[] input = null;
        String[] str = new String[1];
        ArrayList<String[]> lines = new ArrayList();
        int i = 0, j = 0;
        boolean done = false;
        dlIndex = GL11.glGenLists(1);

        try {
            fileIn = new FileInputStream(fileName);
            inputStream = new DataInputStream(fileIn);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            input = new char[inputStream.available()];
            reader.read(input);
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        str[0] = "";
        for (i = 0; i < input.length; i++) {
            if (input[i] == '\n') {
                str = str[0].split(" ");
                lines.add(str);
                str = new String[1];
                str[0] = "";
                j++;
                continue;
            }
            str[0] += input[i];
        }

        for (i = 0; i < j; i++) {
            str = lines.get(i);
            if (str[0].equals("mtlfile")) {
                textureFile = str[1].trim();
            } else if (str[0].equals("usemtl")) {
                mtlName.add(str[1]);
            } else if (str[0].equals("v")) {
                createVertex(str, choice.vertex);
            } else if (str[0].equals("vn")) {
                createVertex(str, choice.normal);
            } else if (str[0].equals("vt")) {
                createVertex(str, choice.texture);
            } else if (str[0].equals("f")) {
                readPolygonInfo(str);
            }
        }
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
        String[] help;
        int[] values = new int[9];
        for (int i = 1;i < 4;i++) {
            help = str[i].split("/");
            for (int j = 0; j < 3; j++){
                values[(i - 1) * 3 + j] = Integer.parseInt(help[j].trim());
            }
        }
        polygonMaker.add(values);
    }

    private void createDisplayList() {
        Vector3f help;        
        GL11.glNewList(dlIndex, GL11.GL_COMPILE);        
        for (int i = 0;i < polygonMaker.size();i++){            
            GL11.glBegin(GL11.GL_TRIANGLES);
            for (int j = 0;j < 3;j++){
                help = normals.get(polygonMaker.get(i)[3*j+2]-1);
                GL11.glNormal3f(help.getX(), help.getY(), help.getZ());
                help = tCoords.get(polygonMaker.get(i)[3*j+1]-1);
                GL11.glTexCoord2f(help.getX(), help.getY());
                help = vertices.get(polygonMaker.get(i)[3*j]-1);
                GL11.glVertex3f(help.getX(), help.getY(), help.getZ());
            }
            GL11.glEnd();
        }
        GL11.glEndList();
    }

    public void draw(){
        texture.bind();
        GL11.glCallList(dlIndex);
    }
}
