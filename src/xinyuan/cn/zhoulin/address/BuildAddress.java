package xinyuan.cn.zhoulin.address;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
/**
 * 
 */
public class BuildAddress extends Activity implements OnClickListener {
	private EditText username;
	private Button back;
	private EditText address;
	private EditText phone;
	private Button commit;
	private LinearLayout ly;
	private TextView tv;
	private String sp1id = "-1";
	private String sp2id = "-1";
	private String sp3id = "-1";
	private String pName="";
	private String cityName="";
	private String countryName="";
	private IntentFilter ift;
	private Mybr br;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.build);
		initView();
		initlistener();
	}
	/**
	 * 
	 */
	private void initlistener() {
		back.setOnClickListener(this);
		commit.setOnClickListener(this);
		ly.setOnClickListener(this);
	}
	/**
	 * 
	 */
	private void initView() {
		br = new Mybr();
		ift = new IntentFilter();
		ift.addAction("address");
		registerReceiver(br, ift);
		ly = (LinearLayout) findViewById(R.id.ly);
		tv = (TextView) findViewById(R.id.tv);
		username = (EditText) findViewById(R.id.username);
		back = (Button) findViewById(R.id.back);
		address = (EditText) findViewById(R.id.address);
		phone = (EditText) findViewById(R.id.phone);
		commit = (Button) findViewById(R.id.commit);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	//验证电话号码
	public static boolean isMobileNO(String mobiles){     
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");     
        Matcher m = p.matcher(mobiles);     
        return m.matches();     
    } 
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			BuildAddress.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;
		case R.id.ly:
			startActivity(new Intent(BuildAddress.this, BuildAddress1.class));
			this.overridePendingTransition(R.anim.view_in_from_right,
					R.anim.view_out_to_left);
			break;
		case R.id.commit:
			String phoneNum=phone.getText().toString();
			if(!isMobileNO(phoneNum))
			{
				Toast.makeText(this, "请输入正确的电话号码！", 2000).show();
				return;
			}
			if (!username.getText().toString().equals("")
					&& !address.getText().toString().equals("")
					&& !phone.getText().toString().equals("") && sp1id != "-1"
					&& sp2id != "-1" && sp3id != "-1") {
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("act", UrlPath.ACT_ADDRESS);
				ha.put("acttag", "add"); //添加新地址
				ha.put("uid", Myapplication.sp.getString("uid", ""));
				ha.put("username", Myapplication.sp.getString("username", ""));
				ha.put("seskey", Myapplication.seskey);
				ha.put("realname", username.getText().toString());
				ha.put("mobile", phone.getText().toString());
				ha.put("address", address.getText().toString());
				ha.put("store_id", Myapplication.store_id);
				ha.put("city_id", sp2id);
				ha.put("county_id", sp3id);
				ha.put("province_id", sp1id);
				ha.put("province_name", pName);
				ha.put("city_name", cityName);
				ha.put("county_name", countryName);
				
				Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject arg0) {
								Log.e("sdfgsdgdfh", arg0.toString());
								try {
									switch (arg0.getInt("code")) {
									case 1:
										Toast.makeText(BuildAddress.this,
												"新建地址成功", 2000).show();
										Myapplication.addressneedflash = true;
										BuildAddress.this.finish();
										BuildAddress.this
												.overridePendingTransition(
														R.anim.view_in_from_left,
														R.anim.view_out_to_right);
										break;
									case 2:
										Toast.makeText(BuildAddress.this,
												"提交参数有误", 2000).show();
										break;
									case 3:
										Toast.makeText(BuildAddress.this,
												"您的账号异常", 2000).show();
										break;
									case 4:
										Toast.makeText(BuildAddress.this,
												"抱歉做多只能保存5个备选地址，请您删除以前的地址再行添加",
												2000).show();
										break;

									default:
										break;
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError arg0) {
								Toast.makeText(BuildAddress.this, "网络异常", 2000)
										.show();
							}
						});
			} else {
				Toast.makeText(BuildAddress.this, "请填写完整资料", 2000).show();
			}

			break;

		default:
			break;
		}

	}
	/**
	 * 
	 */
	private class Mybr extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			sp1id = (String) intent.getExtras().get("id1");
			sp2id = (String) intent.getExtras().get("id2");
			sp3id = (String) intent.getExtras().get("id3");
			pName=(String) intent.getExtras().get("pName");
			cityName=(String) intent.getExtras().get("cityName");
			countryName=(String) intent.getExtras().get("countryName");
			String m=pName+cityName+countryName;
			tv.setText(m);
		}

	}

}
