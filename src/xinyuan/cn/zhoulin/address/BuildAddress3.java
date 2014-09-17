package xinyuan.cn.zhoulin.address;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import xinyuan.cn.zhoulin.model.CountyBean;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
public class BuildAddress3 extends Activity implements OnClickListener {
	private Button back;
	private ListView lv;
	private Myad ad;
	private ArrayList<CountyBean> li;
	private String id1;
	private String id2;
	private String pName;
	private String cityName;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bd3);
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
				Intent in = new Intent();
				in.setAction("address");
				in.putExtra("id1", id1);
				in.putExtra("id2", id2);
				in.putExtra("id3", "" + tv.getTag());
				in.putExtra("pName",pName);
				in.putExtra("cityName", cityName);
				in.putExtra("countryName", tv.getText().toString());
				sendBroadcast(in);
				Myapplication.addresscreade = true;
				BuildAddress3.this.finish();
				BuildAddress3.this.overridePendingTransition(
						R.anim.view_in_from_left, R.anim.view_out_to_right);

			}
		});

	}
	/**
	 * 
	 */
	private void initdata() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("store_id", Myapplication.store_id);
		ha.put("act", "county"); 
		ha.put("city_id", id2);
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								JSONArray ja = response.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									CountyBean cb = new CountyBean();
									cb.setCounty_id(jb.getString("county_id"));
									cb.setCounty_name(jb
											.getString("county_name"));
									li.add(cb);

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
		pName = getIntent().getExtras().getString("pName");
		cityName = getIntent().getExtras().getString("cityName");
		id1 = getIntent().getExtras().getString("id1");
		id2 = getIntent().getExtras().getString("id2");
		back = (Button) findViewById(R.id.back);
		lv = (ListView) findViewById(R.id.lv);
		ad = new Myad();
		li = new ArrayList<CountyBean>();
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
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CountyBean cb = li.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(R.layout.sp_item, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.tv);
			tv.setText(cb.getCounty_name());
			tv.setTag(cb.getCounty_id());
			return convertView;
		}

	}

}
