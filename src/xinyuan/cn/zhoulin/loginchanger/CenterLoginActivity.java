package xinyuan.cn.zhoulin.loginchanger;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.LoginActivity;
import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.RegistActivity;
import xinyuan.cn.zhoulin.UrlPath;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
/**
 * 个人中心登录页面
 */
public class CenterLoginActivity extends Activity implements OnClickListener {
	private View back;
	private EditText username;
	private EditText password;
	private Button login;
	private Button regist;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.logi);
		Myapplication.getInstance().addActivity(this);
		initView();
		initlistener();
		initData();
	}
	/**
	 * 
	 */
	private void initData() {
		username.setText(Myapplication.sp.getString("username", ""));
		password.setText(Myapplication.sp.getString("password", ""));
	}
	/**
	 * 
	 */
	private void initView() {
		back = findViewById(R.id.back);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		regist = (Button) findViewById(R.id.regist);

	}
	/**
	 * 
	 */
	private void initlistener() {
		back.setOnClickListener(this);
		login.setOnClickListener(this);
		regist.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			CenterLoginActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;
		case R.id.login:
			if (!username.getText().toString().equals("")
					&& !password.getText().toString().equals("")) {
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("store_id", Myapplication.store_id);
				ha.put("act", UrlPath.ACT_LOGIN);
				ha.put("username", username.getText().toString());
				ha.put("password", password.getText().toString());
				Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								String content = response.toString();
								try {
									JSONObject jb = new JSONObject(content);
									switch (jb.getInt("code")) {
									case 1:
										Toast.makeText(
												CenterLoginActivity.this,
												"登录成功", 2000).show();
										if (!Myapplication.sp.getString(
												"username", "").equals(
												jb.getString("username"))) {
											Myapplication.machineCachelist
													.clear();
											try {
												Myapplication.machineCachetool
														.saveCache(Myapplication.machineCachelist);
												Myapplication.machine_refresh = true;
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
										Myapplication.ed.putString("username",
												jb.getString("username"));
										Myapplication.ed.putString("uid",
												jb.getString("uid"));
										Myapplication.ed.putString("email",
												jb.getString("email"));
										Myapplication.ed.putString("credit",
												jb.getString("credit"));
										Myapplication.ed.putString("exp",
												jb.getString("exp"));
										Myapplication.ed.putString("lastlogin",
												jb.getString("lastlogin"));
										Myapplication.ed.putString("login_times",
												jb.getString("login_times"));
										Myapplication.ed.putString("photo",
												jb.getString("photo"));
										Myapplication.ed.putString("password",
												password.getText().toString());
										Myapplication.ed.commit();
										Myapplication.seskey = jb
												.getString("seskey");
										Myapplication.login = true;
										Myapplication.orderneedflash = true;
										CenterLoginActivity.this.finish();
										CenterLoginActivity.this
												.overridePendingTransition(
														R.anim.view_in_from_left,
														R.anim.view_out_to_right);
										break;
									default:
										Toast.makeText(CenterLoginActivity.this,
												response.getString("msg"), 2000).show();
										break;
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// TODO Auto-generated method stub

							}
						});

			} else {
				Toast.makeText(CenterLoginActivity.this, "请检查您的输入是否完整", 2000)
						.show();
			}

			break;
		case R.id.regist:
			startActivity(new Intent(CenterLoginActivity.this,
					RegistActivity.class));
			this.overridePendingTransition(R.anim.view_in_from_right,
					R.anim.view_out_to_left);
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 */
	protected void onRestart() {
		super.onRestart();
		if (Myapplication.login) {
			CenterLoginActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
		}
	}
	/**
	 * 
	 */
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK) { // KEYCODE_BACK=4;
			CenterLoginActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);

			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

	protected void onPause() {
		super.onPause();
		Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
	}

}
