package xinyuan.cn.zhoulin.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import xinyuan.cn.zhoulin.model.ProvinceBean;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
/**
 * 
 */
public class BuildAddress1 extends Activity implements OnClickListener {
	private Button back;
	private ListView lv;
	private List<ProvinceBean> li;
	private Myad ad;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bd1);
		initview();
		initdata();
		initListener();

	}
	/**
	 * 
	 */
	private void initListener() {
		back.setOnClickListener(this);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView tv = (TextView) arg1.findViewById(R.id.tv);
				Intent in = new Intent(BuildAddress1.this, BuildAddress2.class);
				in.putExtra("id", "" + tv.getTag());
				in.putExtra("pName", tv.getText().toString());
				Log.i("test",  "pName:"+tv.getText().toString());
				startActivity(in);
				BuildAddress1.this.overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
		});

	}

	protected void onRestart() {
		super.onRestart();
		if (Myapplication.addresscreade) {
			BuildAddress1.this.finish();
			Myapplication.addresscreade = false;
		}
	}
	/**
	 * 
	 */
	private void initdata() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("store_id", Myapplication.store_id);
		ha.put("act","province");
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								JSONArray ja = response.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									ProvinceBean pb = new ProvinceBean();
									pb.setProvince_id(jb
											.getString("province_id"));
									pb.setProvince_name(jb
											.getString("province_name"));
									li.add(pb);
								}
								ad.notifyDataSetChanged();
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
	private void initview() {
		back = (Button) findViewById(R.id.back);
		lv = (ListView) findViewById(R.id.lv);
		li = new ArrayList<ProvinceBean>();
		ad = new Myad();
		lv.setAdapter(ad);
	}

	@Override
	public void onClick(View v) {
		finish();
		this.overridePendingTransition(R.anim.view_in_from_left,
				R.anim.view_out_to_right);
	}
	/**
	 * 
	 */
	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return li.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ProvinceBean pb = li.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(R.layout.sp_item, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.tv);
			tv.setText(pb.getProvince_name());
			tv.setTag(pb.getProvince_id());
			return convertView;
		}

	}

}
