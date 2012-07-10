/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLUtessellator;
import org.lwjgl.util.glu.tessellation.GLUtessellatorImpl;

/**
 *
 * @author juho
 */

//All (most?) helpful helper functions go here :)
public class HelperFunctions {
    public static void tesselatePolygon(double[][] coordinates, double widthFactor, double heightFactor, int displayListIndex){
        GLUtessellator tesselator = new GLUtessellatorImpl();
        TessCallBackCustom callBacker = new TessCallBackCustom();


        tesselator.gluTessCallback(GLU.GLU_TESS_VERTEX, callBacker);
        tesselator.gluTessCallback(GLU.GLU_TESS_BEGIN, callBacker);
        tesselator.gluTessCallback(GLU.GLU_TESS_END, callBacker);
        tesselator.gluTessCallback(GLU.GLU_TESS_COMBINE, callBacker);

        GL11.glNewList(displayListIndex, GL11.GL_COMPILE);


        tesselator.gluBeginPolygon();
//        tesselator.gluTessNormal(0f, 0f, 1f);
        tesselator.gluTessBeginContour();
        double[] newData;
        for (int i = 0; i < coordinates.length; i++) {
            newData = new double[] {coordinates[i][0], coordinates[i][1], coordinates[i][2], widthFactor, heightFactor};
            tesselator.gluTessVertex(newData, 0, new VertexData(newData));
        }
        tesselator.gluTessEndContour();

        tesselator.gluEndPolygon();
        GL11.glEndList();
    }
}
