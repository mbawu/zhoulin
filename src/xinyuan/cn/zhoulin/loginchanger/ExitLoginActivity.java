package xinyuan.cn.zhoulin.loginchanger;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
/**
 * 
 */
public class ExitLoginActivity extends Activity implements OnClickListener {
	private Button back;
	private Button exit;
	private Button cancel;
	private TextView username;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.exitlogin);
		initview();
		initlistener();
	}
	/**
	 * 
	 */
	private void initlistener() {
		back.setOnClickListener(this);
		exit.setOnClickListener(this);
		cancel.setOnClickListener(this);

	}
	/**
	 * 
	 */
	private void initview() {
		back = (Button) findViewById(R.id.back);
		exit = (Button) findViewById(R.id.exit);
		cancel = (Button) findViewById(R.id.cancel);
		username = (TextView) findViewById(R.id.username);
		username.setText(Myapplication.sp.getString("username", ""));

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			ExitLoginActivity.this.finish();
			ExitLoginActivity.this.overridePendingTransition(
					R.anim.view_in_from_left, R.anim.view_out_to_right);
			break;
		case R.id.exit:
			Myapplication.login = false;
			Myapplication.orderneedflash = true;
			ExitLoginActivity.this.finish();
			ExitLoginActivity.this.overridePendingTransition(
					R.anim.view_in_from_left, R.anim.view_out_to_right);
			break;
		case R.id.back:
			ExitLoginActivity.this.finish();
			ExitLoginActivity.this.overridePendingTransition(
					R.anim.view_in_from_left, R.anim.view_out_to_right);
			break;

		default:
			break;
		}

	}

}
