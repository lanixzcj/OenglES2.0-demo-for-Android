package example.com.opengltest.object;

import android.content.Context;

import java.nio.FloatBuffer;

import example.com.opengltest.R;
import example.com.opengltest.program.ShaderProgram;
import example.com.opengltest.util.MatrixState;
import example.com.opengltest.util.Utils;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by Administrator on 2015/6/29 0029.
 */
public class SimpleTriangle {
    private ShaderProgram mProgram;

	private float[] mVertexes = new float[]{
			-1, -1, 0,
			1, -1, 0,
			0, 1, 0
	};
	private float[] mColors = new float[]{
			1, 0, 0,
			0, 1, 0,
			0, 0, 1
	};
	private FloatBuffer mVertexesBuffer;
	private FloatBuffer mColorsBuffer;
    public SimpleTriangle(Context context) {
		mProgram = new ShaderProgram(context, R.raw.triangle_vertex_shader, R.raw.triangle_fragment_shader);

		mVertexesBuffer = Utils.getFloatBuffer(mVertexes);
		mColorsBuffer = Utils.getFloatBuffer(mColors);
    }

    public void bindData() {
        mProgram.setPositionAttribute(mVertexesBuffer, 3);
        mProgram.setColorAttribute(mColorsBuffer, 3);
    }

    public void draw() {
        useProgram();
        bindData();
		setUniform();

        glDrawArrays(GL_TRIANGLES, 0,  mVertexes.length / 3);
    }

    public void useProgram() {
        // TODO Auto-generated method stub
        mProgram.useProgram();
    }

    public void setUniform() {
		mProgram.setMatrix(MatrixState.getFinalMatrix());
    }

}