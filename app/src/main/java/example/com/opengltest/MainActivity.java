package example.com.opengltest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
    private MySurfaceView mGlSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlSurfaceView = new MySurfaceView(this);

        setContentView(R.layout.main_layout);

		RelativeLayout mainRelativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
		mainRelativeLayout.addView(mGlSurfaceView, 0);

		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.simple_triangle:
						mGlSurfaceView.setDrawType(0);
						break;
					case R.id.tiger_obj:
						mGlSurfaceView.setDrawType(1);
						break;
					case R.id.tiger_obj_frame:
						mGlSurfaceView.setDrawType(2);
						break;
					case R.id.tiger_obj_key_frame:
						mGlSurfaceView.setDrawType(3);
						break;
					case R.id.bone_animation:
						mGlSurfaceView.setDrawType(4);
						break;
					case R.id.ms3d_bone_animation:
						mGlSurfaceView.setDrawType(5);
						break;
				}
			}
		});
    }

	@Override
	protected void onPause() {
		super.onPause();
		mGlSurfaceView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGlSurfaceView.onResume();
	}
}
