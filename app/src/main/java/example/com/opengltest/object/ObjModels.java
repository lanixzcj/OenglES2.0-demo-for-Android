package example.com.opengltest.object;

import android.content.Context;

import java.nio.FloatBuffer;

import example.com.opengltest.R;
import example.com.opengltest.program.ShaderProgram;
import example.com.opengltest.util.LoadUtil;
import example.com.opengltest.util.MatrixState;
import example.com.opengltest.util.Utils;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by Administrator on 2015/6/29 0029.
 */
public class ObjModels {
	private int mIndex = 0;
    public ShaderProgram mProgram;

    private int mTextureId;

    private FloatBuffer[] mVertexesBuffer = new FloatBuffer[31];
    private FloatBuffer mTextureCoordsBuffer;
    private int mSize;
    public ObjModels(Context context, String objString, int textureId) {
        mProgram = new ShaderProgram(context,
                R.raw.base_vertex_shader, R.raw.base_fragment_shader);

        mTextureId = textureId;

		for (int i = 0;i < 31;i++) {
			String obj = objString + i + ".obj";
			ObjModelEnity objModelEnity = LoadUtil.loadFromFile(obj, context.getResources());
			mVertexesBuffer[i] = Utils.getFloatBuffer(objModelEnity.getVertices());
			if (i == 0) {
				mTextureCoordsBuffer = Utils.getFloatBuffer(objModelEnity.getmTexCoors());
				mSize = objModelEnity.getVertices().length / 3;
			}
		}

    }

    public void bindData() {
		mIndex = (mIndex + 1) % 31;
        mProgram.setPositionAttribute(mVertexesBuffer[mIndex], 3);
        mProgram.setTextureAttribute(mTextureCoordsBuffer, 2);
    }

    public void draw() {
        useProgram();
        bindData();
        setUniform();

        glDrawArrays(GL_TRIANGLES, 0,  mSize);
    }

	public void useProgram() {
        // TODO Auto-generated method stub
        mProgram.useProgram();
    }

    public void setUniform() {
        mProgram.setMatrix(MatrixState.getFinalMatrix());
        mProgram.setTexture0(mTextureId);
    }

}