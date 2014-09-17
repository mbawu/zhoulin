package xinyuan.cn.zhoulin.activitys2.muchmore;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.LoginActivity;
import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
/**
 * 
 */
public class SuggesActivity extends Activity implements OnClickListener {
	private View back;
	private Spinner spinner;
	private Button commit;
	private EditText contact;
	private EditText content;
	private int type = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sugest);
		Myapplication.getInstance().addActivity(this);
		initView();
		initlistener();
	}
	/**
	 * 
	 */
	private void initlistener() {
		back.setOnClickListener(this);
		commit.setOnClickListener(this);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				type = arg2;

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}
	/**
	 * 
	 */
	private void initView() {
		back = findViewById(R.id.back);
		commit = (Button) findViewById(R.id.commit);
		contact = (EditText) findViewById(R.id.contact);
		content = (EditText) findViewById(R.id.content);
		spinner = (Spinner) findViewById(R.id.spiner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				SuggesActivity.this, R.layout.ss);
		String level[] = new String[] { "功能意见", "界面意见", "操作意见", "流量问题", "需求问题",
				"其他问题" };// 资源文件
		for (int i = 0; i < level.length; i++) {
			adapter.add(level[i]);
		}
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			SuggesActivity.this.finish();
			SuggesActivity.this.overridePendingTransition(
					R.anim.view_in_from_left, R.anim.view_out_to_right);
			break;
		case R.id.commit:
			if (Myapplication.login) {
				if (!contact.getText().toString().equals("")
						&& !content.getText().toString().equals("")) {
					HashMap<String, String> ha = new HashMap<String, String>();
					ha.put("act",UrlPath.ACT_COMMENT);
					ha.put("acttag","feedback");
					ha.put("store_id", Myapplication.store_id);
					ha.put("uid", Myapplication.sp.getString("uid", "-1"));
					ha.put("username",
							Myapplication.sp.getString("username", ""));
					ha.put("seskey", Myapplication.seskey);
					ha.put("msg_type", "" + type);
					ha.put("realname",
							Myapplication.sp.getString("username", ""));
					ha.put("message", content.getText().toString());
					ha.put("mobile", contact.getText().toString());
					Log.i("test", "意见反馈："+Myapplication.getUrl(ha));
					Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									try {
										Log.i("test","意见反馈："+response.getString("msg"));
										switch (response.getInt("code")) {
										case 1:
											
											Toast.makeText(SuggesActivity.this,
													"意见提交成功", 2000).show();
											SuggesActivity.this.finish();
											SuggesActivity.this
													.overridePendingTransition(
															R.anim.view_in_from_left,
															R.anim.view_out_to_right);
											break;
										case 2:
											Toast.makeText(SuggesActivity.this,
													"抱歉您的提交参数有误", 2000).show();
											break;
										case 3:
											Toast.makeText(SuggesActivity.this,
													"抱歉身份认证错误", 2000).show();
											break;
										case 4:
											Toast.makeText(SuggesActivity.this,
													"30秒内不能重复提交哦", 2000).show();
											break;

										default:
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
					Toast.makeText(SuggesActivity.this, "请检查您的反馈意见否输入完整", 2000)
							.show();
				}
			} else {
				startActivity(new Intent(SuggesActivity.this,
						LoginActivity.class));
				this.overridePendingTransition(R.anim.view_in_from_right,
						R.anim.view_out_to_left);
			}
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
			SuggesActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);

			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

}
