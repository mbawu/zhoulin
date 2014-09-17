package xinyuan.cn.zhoulin.address;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import xinyuan.cn.zhoulin.model.AddressBean;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
/**
 * 
 */
public class ManageAddress extends Activity implements OnClickListener {
	private Button buid;
	private Button back;
	private ListView lv;
	private Myad myad;
	private ArrayList<AddressBean> al;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.manageadddress);
		initView();
		initdata();
		buid.setOnClickListener(this);
	}
	/**
	 * 
	 */
	private void initdata() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("act", UrlPath.ACT_ADDRESS);
		ha.put("acttag", "list");
		ha.put("uid", Myapplication.sp.getString("uid", ""));
		ha.put("username", Myapplication.sp.getString("username", ""));
		ha.put("seskey", Myapplication.seskey);
		ha.put("store_id", Myapplication.store_id);
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								JSONArray ja = response.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									AddressBean ab = new AddressBean();
									ab.setAddress(jb.getString("address"));
									ab.setAddress_id(jb.getString("address_id"));
									ab.setMobile(jb.getString("mobile"));
									ab.setRealname(jb.getString("realname"));
									ab.setCity_id(jb.getString("city_id"));
									ab.setCity_name(jb.getString("city_name"));
									ab.setCounty_id(jb.getString("county_id"));
									ab.setCounty_name(jb
											.getString("county_name"));
									ab.setProvince_id(jb
											.getString("province_id"));
									ab.setProvince_name(jb
											.getString("province_name"));
									al.add(ab);
								}
								myad.notifyDataSetChanged();

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});

	}
	/**
	 * 
	 */
	private void initView() {
		buid = (Button) findViewById(R.id.buildaddress);
		back = (Button) findViewById(R.id.back);
		lv = (ListView) findViewById(R.id.lv);
		myad = new Myad();
		al = new ArrayList<AddressBean>();
		lv.setAdapter(myad);
		back.setOnClickListener(this);
	}
	/**
	 * 
	 */
	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			return al.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AddressBean aa = al.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(R.layout.address_item,
						null);
			}
			TextView username = (TextView) convertView
					.findViewById(R.id.username);
			TextView phone = (TextView) convertView.findViewById(R.id.phone);
			TextView address = (TextView) convertView
					.findViewById(R.id.address);
			TextView add = (TextView) convertView.findViewById(R.id.add);

			View address_write = convertView.findViewById(R.id.addresschanger);
			View address_delet = convertView.findViewById(R.id.addressdelet);
			username.setText(aa.getRealname());
			phone.setText("(" + aa.getMobile() + ")");
			address.setText(aa.getAddress());
			add.setText(aa.getProvince_name() + aa.getCity_name()
					+ aa.getCounty_name());
			address_write.setTag(aa);
			address_delet.setTag(aa);
			address_delet.setOnClickListener(ManageAddress.this);
			address_write.setOnClickListener(ManageAddress.this);
			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addresschanger:
			Intent in = new Intent(ManageAddress.this,
					AddressChangerActivity.class);
			AddressBean am = (AddressBean) v.getTag();
			in.putExtra("ad", am);
			startActivity(in);
			this.overridePendingTransition(R.anim.view_in_from_right,
					R.anim.view_out_to_left);
			break;
		case R.id.addressdelet:
			AddressBean ab = (AddressBean) v.getTag();
			delet(ab.getAddress_id());
			break;
		case R.id.back:
			ManageAddress.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;
		case R.id.buildaddress:
			startActivity(new Intent(ManageAddress.this, BuildAddress.class));
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
	public void delet(final String addressid) {
		new AlertDialog.Builder(ManageAddress.this)
				.setMessage("确定删除?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						HashMap<String, String> ha = new HashMap<String, String>();
						ha.put("act", UrlPath.ACT_ADDRESS);
						ha.put("acttag", "delete");
						ha.put("uid", Myapplication.sp.getString("uid", ""));
						ha.put("username",
								Myapplication.sp.getString("username", ""));
						ha.put("seskey", Myapplication.seskey);
						ha.put("store_id", Myapplication.store_id);
						ha.put("address_id", addressid);
						Myapplication.client.postWithURL(UrlPath.SERVER_URL,
								ha, new Listener<JSONObject>() {
									public void onResponse(JSONObject response) {
										try {
											if (response.getInt("code") == 1) {
												al.clear();
												initdata();
											} else {
												Toast.makeText(
														ManageAddress.this,
														"删除失败", 2000).show();
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}

									}
								}, new ErrorListener() {

									@Override
									public void onErrorResponse(
											VolleyError error) {
										// TODO Auto-generated method stub

									}
								});

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						setResult(RESULT_CANCELED);//
					}
				}).show();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		al.clear();
		initdata();
	}

}
