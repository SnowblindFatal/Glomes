package external;

/**
 *
 * simple "earcutting" polygon in convex parts.
 *
 * "ear" triangles are cut of the polygon until a convex polygon is achived.
 * those remains are not decomposed further as convex polygons can be rendered
 * directly to triangles.
 *
 * dirty: as the operation relies on clockwise vertex ordering, the given 3d
 * polygon verticies are projected onto an abitrary chosen 2d skew plane. so on
 * rare occassion the polygon is perpendicular to this plane thus resulting in
 * an useless collapsed 1d projection, which means the whole polygon is returned
 * untouched. however, the plane is chosen to ensure safe operation for polys in
 * base axes aligned planes.
 *
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;

public class Convexiser {

    // the current polygon to cut  
    List<Vector3f> currentPoly;
    int n; //number of verticies in current remaining polygon
    float px[], py[];  //2d projected contour coordinates
    boolean pConvex[]; //convexity flag for every point
    //store list of triangles already cut off
    List<List<Vector3f>> earPoly = new ArrayList<List<Vector3f>>();

    /*
     * rebuild px, py coords arrays after each cut. dirty: does a simple
     * projection to an abitrary defined skew plane. the hope is that a polygon
     * will never be orthogonal to this plane. if it where, numerical accuracy
     * quickly melt down and vanish with perfect perpendicularity. then the
     * whole thing fails. the plane is chosen skew to safely work on all base
     * planes (x,y,z) with one of x,y,z constant.
     */
    void updatePoints() {
        n = currentPoly.size();
        int i = 0;
        for (Vector3f v : currentPoly) { //do skew projection
            px[i] = v.x - v.y;
            py[i] = -v.z + v.y;
            i++;
        }
    }

    /**
     * main worker. splits given polygon up to triangles and a remaining convex
     * polygon.
     *
     * @param polygon the maybe concave polygon as contour vertex list. @returns
     * a list of triangles and convex polygon, each a list of contour verticies.
     */
    public List<List<Vector3f>> convexise(List<Vector3f> polygon) {

        currentPoly = new ArrayList<Vector3f>(polygon);


        /*
         * allocate coordinate arrays. for performance, this isn't repeated on
         * updatePoints, just the last index is updated in npoints.
         */
        n = polygon.size();
        px = new float[n];
        py = new float[n];
        pConvex = new boolean[n];

        //initially fill px,py
        updatePoints();

        //if polygon contour isn't in clockwise order, reverse it.
        if (!polygonClockwise()) {
            Collections.reverse(currentPoly);
            updatePoints();
        }

        //cut ears till a convex polygon or triangle (allways convex) is remaining
        while (n > 3) {
            classifyConvexity();
//			if(classifyConvexity()) break; //if poly is already convex, quit.
            if (!cutOneEar()) {
                break; //if cutting fails, quit. 
            }
        }

        earPoly.add(currentPoly); //add the remaining convex poly to the output
        return earPoly;
    }

    /*
     * tests a polygon for clockwise definition by angle sum
     */
    boolean polygonClockwise() {


        double convex_sum = 0;

        for (int i = 0; i < n; i++) {

            //get indicies of three neighbouring verticies
            int i0 = (i + n - 1) % n, i1 = i, i2 = (i + 1) % n;

            float aa = ((px[i2] - px[i0]) * (px[i2] - px[i0])) + ((-py[i2] + py[i0]) * (-py[i2] + py[i0])),
                    bb = ((px[i1] - px[i0]) * (px[i1] - px[i0])) + ((-py[i1] + py[i0]) * (-py[i1] + py[i0])),
                    cc = ((px[i2] - px[i1]) * (px[i2] - px[i1])) + ((-py[i2] + py[i1]) * (-py[i2] + py[i1]));

            double b = Math.sqrt(bb),
                    c = Math.sqrt(cc),
                    theta = Math.acos((bb + cc - aa) / (2 * b * c));

            if (convex(px[i0], py[i0], px[i1], py[i1], px[i2], py[i2])) {
                convex_sum += Math.PI - theta;
            } else {
                convex_sum -= Math.PI - theta;
            }
        }

        return (convex_sum >= (2 * Math.PI - .001));

    }

    /*
     * classify verticies by convexity, thus filling pConvex. @returns true if
     * all points proved convex.
	 *
     */
    boolean classifyConvexity() {

        boolean convex = true;

        for (int i = 0; i < n; i++) {

            int i0 = (i + n - 1) % n, i1 = i, i2 = (i + 1) % n;

            pConvex[i] = convex(px[i0], py[i0], px[i1], py[i1], px[i2], py[i2]);
            convex &= pConvex[i];
        }

        return convex;
    }

    /*
     * returns true if point (x2, y2) is convex
     */
    boolean convex(float x1, float y1, float x2, float y2, float x3, float y3) {
        return (area(x1, y1, x2, y2, x3, y3) < 0);
    }

    /*
     * area: determines the double area of triangle formed by three points
     */
    float area(float x1, float y1, float x2, float y2, float x3, float y3) {
        float area = 0;

        area += x1 * (y3 - y2);
        area += x2 * (y1 - y3);
        area += x3 * (y2 - y1);

        return area;
    }

    /*
     * true if the triangle formed by three points contains another vertex
     */
    boolean triangleContainsPoint(float x1, float y1, float x2, float y2, float x3, float y3) {

        for (int i = 0; i < n; i++) {
            if (!pConvex[i] && (((px[i] != x1) && (py[i] != y1))
                    || ((px[i] != x2) && (py[i] != y2))
                    || ((px[i] != x3) && (py[i] != y3)))) {

                float a1 = area(x1, y1, x2, y2, px[i], py[i]),
                        a2 = area(x2, y2, x3, y3, px[i], py[i]),
                        a3 = area(x3, y3, x1, y1, px[i], py[i]);

                if ((a1 > 0 && a2 > 0 && a3 > 0)
                        || (a1 < 0 && a2 < 0 && a3 < 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * cut away "ear" vertex with given index
     */
    void cutEar(int index) {

        int[] ind = new int[]{(index + n - 1) % n, index, (index + 1) % n};

        List<Vector3f> p = new ArrayList<Vector3f>();

        for (int i = 0; i < 3; i++) {
            p.add(currentPoly.get(ind[i]));
        }

        earPoly.add(p);
        currentPoly.remove(index);
        updatePoints();
    }

    /*
     * cut away abitrary "ear" vertex, that is any polygon vertex building a
     * triangle with its neighbours not containing any other vertex.
     */
    boolean cutOneEar() {
        for (int i = 0; i < n; i++) {
            if (pConvex[i]) { /*
                 * point is convex
                 */

                //	get indicies of three neighbouring verticies
                int i0 = (i + n - 1) % n, i1 = i, i2 = (i + 1) % n;

                if (!triangleContainsPoint(px[i0], py[i0], px[i1], py[i1], px[i2], py[i2])) {
                    cutEar(i);
                    return true; //reclassify before next cut
                }
            }
        }
        return false;
        //throw new RuntimeException("Convexiser: could not cut bad ear. ");
    }
}
