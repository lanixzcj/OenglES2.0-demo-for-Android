package example.com.opengltest;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import example.com.opengltest.object.ObjModel;
import example.com.opengltest.object.ObjModels;
import example.com.opengltest.object.ObjModelsKey;
import example.com.opengltest.object.SimpleTriangle;
import example.com.opengltest.object.bone.DoActionThread;
import example.com.opengltest.object.bone.Robot;
import example.com.opengltest.object.ms3d.MS3DModel;
import example.com.opengltest.util.MatrixState;
import example.com.opengltest.util.TextureHelper;
import example.com.opengltest.util.TextureManager;

import static android.opengl.GLES20.glClear;

/**
 * Created by Administrator on 2015/6/29 0029.
 */
public class MyRenderer implements GLSurfaceView.Renderer {
    private Context mContext;
    private ObjModel mObjModel;
	private SimpleTriangle mSimpleTriangle;
	private ObjModels mObjModels;
	private ObjModelsKey mObjModelsKey;
	private ObjModel[] mBoneParts = new ObjModel[12];
	private Robot mRobot;

	private DoActionThread dat;

	private int mTigerTextureId;
	private int mArmTexId;

	private float mTriangleDeep = 0;
	private float mTriangleAngle = 0;
	private float mTigerDeep = 0;

	int type;

	TextureManager manager;	//纹理管理器
	MS3DModel ms3d;			//ms3d模型
	float time = 0;			//当前时间（用于动画播放）

	//关于摄像机的变量
	float cx = 0;//摄像机x位置
	float cy = 0;//摄像机y位置
	float cz = 500;//摄像机z位置

	float tx = 0;//目标点x位置
	float ty = 0;//目标点y位置
	float tz = 0;//目标点z位置
	public float currSightDis = 500;//摄像机和目标的距离
	float angdegElevation = 30;//仰角
	float angdegAzimuth = 180;//方位角

    public MyRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        MatrixState.setInitStack();
		mTigerTextureId = TextureHelper.loadTexture(mContext, R.drawable.tiger);
		mArmTexId = TextureHelper.loadTexture(mContext, R.drawable.wood);
		mSimpleTriangle = new SimpleTriangle(mContext);
        mObjModel = new ObjModel(mContext, "tiger.obj", mTigerTextureId);
		mObjModels = new ObjModels(mContext, "tiger",  mTigerTextureId);
		mObjModelsKey = new ObjModelsKey(mContext, "tiger", mTigerTextureId);

		mBoneParts[0] = new ObjModel(mContext, "body.obj", mArmTexId);
		mBoneParts[1] = new ObjModel(mContext, "head.obj", mArmTexId);
		mBoneParts[2] = new ObjModel(mContext, "left_top.obj", mArmTexId);
		mBoneParts[3] = new ObjModel(mContext, "left_bottom.obj", mArmTexId);
		mBoneParts[4] = new ObjModel(mContext, "right_top.obj", mArmTexId);
		mBoneParts[5] = new ObjModel(mContext, "right_bottom.obj", mArmTexId);
		mBoneParts[6] = new ObjModel(mContext, "right_leg_top.obj", mArmTexId);
		mBoneParts[7] = new ObjModel(mContext, "right_leg_bottom.obj", mArmTexId);
		mBoneParts[8] = new ObjModel(mContext, "left_leg_top.obj", mArmTexId);
		mBoneParts[9] = new ObjModel(mContext, "left_leg_bottom.obj", mArmTexId);
		mBoneParts[10] = new ObjModel(mContext, "left_foot.obj", mArmTexId);
		mBoneParts[11] = new ObjModel(mContext, "right_foot.obj", mArmTexId);

		mRobot = new Robot(mBoneParts);
		dat = new DoActionThread(mRobot);
		dat.start();

		manager = new TextureManager(mContext.getResources());
		//获取ms3d文件的输入流
		InputStream in = null;
		try{
			in = mContext.getResources().getAssets().open("tiger.ms3d");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//从输入流加载模型
		ms3d = MS3DModel.load(in,manager,mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 2000);

        setCameraPostion();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0, 0 , 0 ,1);

		MatrixState.pushMatrix();
		switch (type) {
			case 0:
				mTriangleAngle = (mTriangleAngle + 10) % 360;
				mTriangleDeep = (mTriangleDeep + 0.1f) % 16;

				MatrixState.translate(0, 0, -(mTriangleDeep > 8 ? 16 - mTriangleDeep : mTriangleDeep) );
				MatrixState.rotate(mTriangleAngle, 0, 1, 0);
				currSightDis = 10;
				GLES20.glDisable(GLES20.GL_CULL_FACE);
				setCameraPostion();
				mSimpleTriangle.draw();
				break;
			case 1:
				mTigerDeep = (mTigerDeep + 1f) % 200;

				MatrixState.translate(0, 0, -(mTigerDeep > 100 ? 200 - mTigerDeep : mTigerDeep) );
				currSightDis = 500;
				GLES20.glEnable(GLES20.GL_CULL_FACE);
				setCameraPostion();
				mObjModel.draw();
				break;
			case 2:
				currSightDis = 500;
				GLES20.glEnable(GLES20.GL_CULL_FACE);
				setCameraPostion();
				mObjModels.draw();
				break;
			case 3:
				currSightDis = 500;
				GLES20.glEnable(GLES20.GL_CULL_FACE);
				setCameraPostion();
				mObjModelsKey.draw();
				break;
			case 4:
				currSightDis = 3;
				GLES20.glEnable(GLES20.GL_CULL_FACE);
				setCameraPostion();
				mRobot.drawSelf();
				break;
			case 5:
				currSightDis = 500;
				GLES20.glDisable(GLES20.GL_CULL_FACE);
				setCameraPostion();
				this.ms3d.animate(time);
				time += 0.015f;
				//若当前播放时间大于总的动画时间则实际播放时间等于当前播放时间减去总的动画时间
				if(time > this.ms3d.getTotalTime()) {
					time = time - this.ms3d.getTotalTime();
				}
				break;
		}
		MatrixState.popMatrix();
    }

	public void setCameraPostion() {
		//计算摄像机的位置
		double angradElevation = Math.toRadians(angdegElevation);// 仰角（弧度）
		double angradAzimuth = Math.toRadians(angdegAzimuth);// 方位角
		cx = (float) (tx - currSightDis * Math.cos(angradElevation)	* Math.sin(angradAzimuth));
		cy = (float) (ty + currSightDis * Math.sin(angradElevation));
		cz = (float) (tz - currSightDis * Math.cos(angradElevation) * Math.cos(angradAzimuth));

		MatrixState.setCamera(
				cx, cy, cz,
				tx, ty, tz,
				0, 1, 0);
	}

	public void setDrawType(int type) {
		this.type = type;
	}
}
