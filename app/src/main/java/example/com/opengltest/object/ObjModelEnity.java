package example.com.opengltest.object;

/**
 * Created by Administrator on 2015/7/1 0001.
 */
public class ObjModelEnity {
    private float[] mVertices;
    private float[] mNormals;
    private float[] mTexCoors;

    public void setVertices(float[] vertices) {
        mVertices = vertices;
    }

    public float[] getVertices() {
        return mVertices;
    }

    public void setNormals(float[] normals) {
        mNormals = normals;
    }

    public float[] getNormals() {
        return mNormals;
    }

    public void setTexCoors(float[] texCoors) {
        mTexCoors = texCoors;
    }

    public float[] getmTexCoors() {
        return mTexCoors;
    }

    @Override
    public String toString() {
        StringBuilder verticeString = new StringBuilder();
        for(Float vertice : mTexCoors) {
            verticeString.append(" " + vertice);
        }
        return verticeString.toString();
    }
}
