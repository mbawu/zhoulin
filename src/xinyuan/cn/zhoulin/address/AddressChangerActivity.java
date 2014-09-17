package xinyuan.cn.zhoulin.address;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import xinyuan.cn.zhoulin.model.AddressBean;
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
public class AddressChangerActivity extends Activity implements OnClickListener {
	private EditText username;
	private Button back;
	private EditText address;
	private EditText phone;
	private Button commit;
	private LinearLayout ly;
	private TextView tv;
	private String sp1id;
	private String sp2id;
	private String sp3id;
	private String pName="";
	private String cityName="";
	private String countryName="";
	private IntentFilter ift;
	private Mybr br;
	private AddressBean ab;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addresschange);
		initView();
		initlistener();
	}

	private void initlistener() {
		back.setOnClickListener(this);
		commit.setOnClickListener(this);
		ly.setOnClickListener(this);
	}
	/**
	 * 
	 */
	private void initView() {
		ab = (AddressBean) getIntent().getExtras().get("ad");
		sp1id = ab.getProvince_id();
		sp2id = ab.getCity_id();
		sp3id = ab.getCounty_id();
		pName=ab.getProvince_name();
		cityName=ab.getCity_name();
		countryName=ab.getCounty_name();
		br = new Mybr();
		ift = new IntentFilter();
		ift.addAction("address");
		registerReceiver(br, ift);
		ly = (LinearLayout) findViewById(R.id.ly);
		tv = (TextView) findViewById(R.id.tv);
		tv.setText(ab.getProvince_name() + ab.getCity_name()
				+ ab.getCounty_name());
		username = (EditText) findViewById(R.id.username);
		username.setText(ab.getRealname());
		back = (Button) findViewById(R.id.back);
		address = (EditText) findViewById(R.id.address);
		address.setText(ab.getAddress());
		phone = (EditText) findViewById(R.id.phone);
		phone.setText(ab.getMobile());
		commit = (Button) findViewById(R.id.commit);
	}

	/**
	 * 
	 */
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AddressChangerActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;
		case R.id.ly:
			startActivity(new Intent(AddressChangerActivity.this,
					BuildAddress1.class));
			this.overridePendingTransition(R.anim.view_in_from_right,
					R.anim.view_out_to_left);
			break;
		case R.id.commit:
			if (!username.getText().toString().equals("")
					&& !address.getText().toString().equals("")
					&& !phone.getText().toString().equals("")) {
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("act", UrlPath.ACT_ADDRESS);
				ha.put("acttag", "edit");
				ha.put("uid", Myapplication.sp.getString("uid", ""));
//				ha.put("username", Myapplication.sp.getString("username", ""));
				ha.put("seskey", Myapplication.seskey);
				ha.put("store_id", Myapplication.store_id);
				ha.put("realname", username.getText().toString());
				ha.put("mobile", phone.getText().toString());
				ha.put("address", address.getText().toString());
				ha.put("address_id", ab.getAddress_id());
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
										Toast.makeText(
												AddressChangerActivity.this,
												"修改地址成功", 2000).show();
										AddressChangerActivity.this.finish();
										AddressChangerActivity.this
												.overridePendingTransition(
														R.anim.view_in_from_left,
														R.anim.view_out_to_right);
										break;
									case 2:
										Toast.makeText(
												AddressChangerActivity.this,
												"提交参数有误", 2000).show();
										break;
									case 3:
										Toast.makeText(
												AddressChangerActivity.this,
												"您的账号异常", 2000).show();
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
								Toast.makeText(AddressChangerActivity.this,
										"网络异常", 2000).show();
							}
						});
			} else {
				Toast.makeText(AddressChangerActivity.this, "请填写完整资料", 2000)
						.show();
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
