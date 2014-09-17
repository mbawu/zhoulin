package xinyuan.cn.zhoulin.activitys2.muchmore;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
/**
 * 
 */
public class ContactActivity extends Activity implements OnClickListener {
	private View back;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact);
		Myapplication.getInstance().addActivity(this);
		initView();
		initListener();

	}
	/**
	 * 
	 */
	private void initListener() {
		back.setOnClickListener(ContactActivity.this);

	}
	/**
	 * 
	 */
	private void initView() {
		back = findViewById(R.id.back);

	}

	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			ContactActivity.this.finish();
			ContactActivity.this.overridePendingTransition(
					R.anim.view_in_from_left, R.anim.view_out_to_right);
			break;

		default:
			break;
		}

	}
	/**
	 * 
	 */
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK) { // KEYCODE_BACK=4;
			ContactActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);

			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

}
