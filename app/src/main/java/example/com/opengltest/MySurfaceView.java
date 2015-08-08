package example.com.opengltest;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2015/6/27 0027.
 */
public class MySurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private MyRenderer mRenderer;//场景渲染器

    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标

    public MySurfaceView(Context context) {
        super(context);
        mRenderer = new MyRenderer(context);
        setEGLContextClientVersion(2);
        setEGLConfigChooser(new MSConfigChooser());
		setPreserveEGLContextOnPause(true);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;//计算Y位移
                float dx = x - mPreviousX;//计算X位移
				mRenderer.angdegAzimuth += dx * TOUCH_SCALE_FACTOR;//设置方位角
				mRenderer.angdegElevation += dy * TOUCH_SCALE_FACTOR;//设置仰角
				//将仰角限制在-90～90度范围内
				mRenderer.angdegElevation = Math.max(mRenderer.angdegElevation, -90);
				mRenderer.angdegElevation = Math.min(mRenderer.angdegElevation, 90);
				//设置摄像机的位置
				mRenderer.setCameraPostion();
        }
        mPreviousY = y;//记录位置
        mPreviousX = x;//记录位置
        return true;
    }

	public void setDrawType(int type) {
		mRenderer.setDrawType(type);
	}

}
