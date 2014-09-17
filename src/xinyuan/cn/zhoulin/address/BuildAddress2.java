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
import xinyuan.cn.zhoulin.model.CityBean;
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
public class BuildAddress2 extends Activity implements OnClickListener {
	private Button back;
	private ListView lv;
	private String id;
	private Myad ad;
	private List<CityBean> li;
	private String name;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bd2);
		id = getIntent().getExtras().getString("id");
		name = getIntent().getExtras().getString("pName");
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
				Intent in = new Intent(BuildAddress2.this, BuildAddress3.class);
				in.putExtra("id1", id);
				in.putExtra("id2", "" + tv.getTag());
				in.putExtra("pName", name);
				in.putExtra("cityName", tv.getText().toString());
				startActivity(in);
				BuildAddress2.this.overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
		});

	}

	/**
	 * 
	 */
	protected void onRestart() {
		super.onRestart();
		if (Myapplication.addresscreade) {
			BuildAddress2.this.finish();
		}
	}
	/**
	 * 
	 */
	private void initdata() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("store_id", Myapplication.store_id);
		ha.put("act", "city"); 
		ha.put("province_id", id);
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								JSONArray ja = response.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									CityBean cb = new CityBean();
									cb.setCity_id(jb.getString("city_id"));
									cb.setCity_name(jb.getString("city_name"));
									li.add(cb);
								}
								ad.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
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
		ad = new Myad();
		li = new ArrayList<CityBean>();
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
			CityBean cb = li.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(R.layout.sp_item, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.tv);
			tv.setText(cb.getCity_name());
			tv.setTag(cb.getCity_id());

			return convertView;
		}

	}

}
