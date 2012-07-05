package glomes;

import org.lwjgl.util.vector.Quaternion;

/**
 *
 * @author Jusku
 */
public class My_Quaternion extends Quaternion {

    public My_Quaternion(){
        super();
    }

    public float[] tomatrix(){
        float[] mat = new float[16];
        mat[3] = 0.0f;
        mat[7] = 0.0f;
        mat[11] = 0.0f;
        mat[12] = 0.0f;
        mat[13] = 0.0f;
        mat[14] = 0.0f;
        mat[15] = 1.0f;

        mat[0] = (float) (1.0f - (2.0f * ((y * y) + (z * z))));
        mat[1] = (float) (2.0f * ((x * y) - (z * w)));
        mat[2] = (float) (2.0f * ((x * z) + (y * w)));

        mat[4] = (float) (2.0f * ((x * y) + (z * w)));
        mat[5] = (float) (1.0f - (2.0f * ((x * x) + (z * z))));
        mat[6] = (float) (2.0f * ((y * z) - (x * w)));

        mat[8] = (float) (2.0f * ((x * z) - (y * w)));
        mat[9] = (float) (2.0f * ((y * z) + (x * w)));
        mat[10] = (float) (1.0f - (2.0f * ((x * x) + (y * y))));

        return mat;
    }

    public float magnitude(){
        return (float) Math.sqrt(x*x + y*y + z*z + w*w);
    }
}
