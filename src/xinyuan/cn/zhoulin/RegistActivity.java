package xinyuan.cn.zhoulin;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class RegistActivity extends Activity implements OnClickListener { // 注册Activity
	private EditText username;
	private EditText password;
	private EditText password2;
	private EditText email;
	private Boolean gou = false;
	private Button regist;
	private View back;
	private String userna;
	private String passwr;
	private String passw2;
	private String emai;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.regis);
		Myapplication.getInstance().addActivity(this);
		initView();
		initListner();
	}

	private void initView() {
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		password2 = (EditText) findViewById(R.id.password2);
		email = (EditText) findViewById(R.id.email);
		regist = (Button) findViewById(R.id.regist);
		back = findViewById(R.id.back);
	}

	private void initListner() {
		regist.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK) { // KEYCODE_BACK=4;
			RegistActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);

			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regist:
			userna = username.getText().toString();
			passwr = password.getText().toString();
			passw2 = password2.getText().toString();
			emai = email.getText().toString();
			if (!userna.equals("")&& !passw2.equals("") && !passwr.equals("")&& !emai.equals("")) {
				HashMap<String, String> hs = new HashMap<String, String>();
				hs.put("store_id", Myapplication.store_id);
				hs.put("act", UrlPath.ACT_REGISTER); //标识为注册的动作
				hs.put("username", userna);
				hs.put("password", passwr);
				hs.put("repassword", passw2);
				hs.put("email", emai);
				//使用统一入口协议地址发送数据
				Myapplication.client.postWithURL(UrlPath.SERVER_URL, hs,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									switch (response.getInt("code")) {
									case 1:
										Toast.makeText(RegistActivity.this,
												"恭喜你，注册成功", 2000).show();
										if (!Myapplication.sp.getString(
												"username", "").equals("")) {
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
												response.getString("username"));
										Myapplication.ed.putString("uid",
												response.getString("uid"));
										Myapplication.ed.putString("email",
												response.getString("email"));
										Myapplication.ed.putString("credit",
												response.getString("credit"));
										Myapplication.ed.putString("exp",
												response.getString("exp"));
										Myapplication.ed.putString("lastlogin",
												response.getString("lastlogin"));
										Myapplication.ed.putString("login_times",
												response.getString("login_times"));
										Myapplication.ed.putString("photo",
												response.getString("photo"));
										Myapplication.ed.putString("password",
												passwr);
										Myapplication.ed.commit();
										Myapplication.seskey = response
												.getString("seskey");
										Myapplication.login = true;
										Myapplication.orderneedflash = true;
										RegistActivity.this.finish();
										RegistActivity.this
												.overridePendingTransition(
														R.anim.view_in_from_left,
														R.anim.view_out_to_right);
										break;
									default:
										Toast.makeText(RegistActivity.this,
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
								Toast.makeText(RegistActivity.this, "请检查网络连接或服务器异常！",
										2000).show();
							}
						});
			} else {
				Toast.makeText(RegistActivity.this, "请填写完整资料再点击注册！", 1).show();
			}

			break;
		case R.id.back:
			RegistActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);

			break;

		default:
			break;
		}

	}
	
	protected void onPause() {
		super.onPause();
		Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
	}

}
