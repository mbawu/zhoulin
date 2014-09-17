package xinyuan.cn.zhoulin.model;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

public class Myvf extends ViewFlipper {

	public Myvf(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.e("dfsfsdf", "onInterceptTouchEvent");
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e("dfsfsdf", "onTouchEvent");
		return true;
	}

}
