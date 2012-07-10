/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;


/**
 *
 * @author juho
 */
/*
 * Copyright (c) 2002-2008 LWJGL Project All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of its contributors may be used
 * to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLUtessellatorCallbackAdapter;

public class TessCallBackCustom extends GLUtessellatorCallbackAdapter {

    @Override
    public void begin(int type) {
        GL11.glBegin(type);
    }

    @Override
    public void combine(double[] coords, Object[] data, float[] weight, Object[] outData) {
        VertexData inData;
        for (int i = 0; i < outData.length; i++) {
            inData = (VertexData) data[i];
            double[] combined = new double[5];
            combined[0] = coords[0];
            combined[1] = coords[1];
            combined[2] = coords[2];
            combined[3] = inData.getData()[3];
            combined[4] = inData.getData()[4];

            outData[i] = new VertexData(combined);
        }
//		vertex[0] = coords[0];
//		vertex[1] = coords[1];
//		vertex[2] = coords[2];
//
//		for (int i = 3; i < 6; i++)
//		{
//		vertex[i] = weight[0] * vertex_data[0][i] +
//		indent indweight[1] * vertex_data[1][i] +
//		indent indweight[2] * vertex_data[2][i] +
//		indent indweight[3] * vertex_data[3][i];
//		}
//
//		*dataOut = vertex;
    }

    @Override
    public void end() {
        GL11.glEnd();
    }

    @Override
    public void vertex(Object newVertexData) {
        VertexData vertex = (VertexData) newVertexData;
        GL11.glTexCoord2d(vertex.getData()[0] * vertex.getData()[3], -vertex.getData()[1] * vertex.getData()[4]);
        GL11.glNormal3d(0d, 0d, 1d);
        GL11.glVertex3d(vertex.getData()[0], vertex.getData()[1], vertex.getData()[2]);
        
    }
}
