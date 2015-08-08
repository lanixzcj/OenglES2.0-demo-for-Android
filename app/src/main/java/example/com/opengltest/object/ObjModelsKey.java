package example.com.opengltest.object;

import android.content.Context;

import java.nio.FloatBuffer;

import example.com.opengltest.R;
import example.com.opengltest.program.KeyFrameShaderProgram;
import example.com.opengltest.util.LoadUtil;
import example.com.opengltest.util.MatrixState;
import example.com.opengltest.util.Utils;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by Administrator on 2015/6/29 0029.
 */
public class ObjModelsKey {
	private float mFactor = 0;
    public KeyFrameShaderProgram mProgram;

    private int mTextureId;

    private FloatBuffer[] mVertexesBuffer = new FloatBuffer[7];
    private FloatBuffer mTextureCoordsBuffer;
    private int mSize;
    public ObjModelsKey(Context context, String objString, int textureId) {
        mProgram = new KeyFrameShaderProgram(context, R.raw.key_frame_vertex_shader,
				R.raw.base_fragment_shader);

        mTextureId = textureId;

		// 0 3 7 9 11 20 30 帧
		for (int i = 0;i < 7;i++) {
			String obj = objString + i + "key.obj";
			ObjModelEnity objModelEnity = LoadUtil.loadFromFile(obj, context.getResources());
			mVertexesBuffer[i] = Utils.getFloatBuffer(objModelEnity.getVertices());
			if (i == 0) {
				mTextureCoordsBuffer = Utils.getFloatBuffer(objModelEnity.getmTexCoors());
				mSize = objModelEnity.getVertices().length / 3;
			}
		}
    }

	

    public void bindData() {
		for (int i = 0;i < 7;i++) {
			mProgram.setKeyPositionAttribute(mVertexesBuffer[i], 3, i);
		}

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

		mFactor = (mFactor + 0.2f) % 6;
		float realFactor  = mFactor > 2 ? mFactor / 2f + 3 : mFactor * 2;
		mProgram.setFactor(realFactor);
    }

}