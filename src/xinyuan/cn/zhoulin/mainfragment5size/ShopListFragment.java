package xinyuan.cn.zhoulin.mainfragment5size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import xinyuan.cn.zhoulin.activitys2.shop.ShoptwoActivity;
import xinyuan.cn.zhoulin.model.ShopTwoBean;
import xinyuan.cn.zhoulin.model.Shop_oneBean;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

public class ShopListFragment extends Fragment implements OnClickListener {
	private View v;
	private List<ShopTwoBean> lv2list;
	private GridView gridView;  //二级商品
	private Myad ad2;
	private String id; //一级商品ID
	private ArrayList<Shop_oneBean> lv1list;
	private ListView lv1; //一级商品列表
	private Myad1 ad1;
	public Boolean gc = false;
	private ProgressBar pr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		gc = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.shoplistfragment, container, false);
			initView();
			initlistener();
			initData();
		}
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}

		return v;
	}

	//当点击一级商品分类列表的某一项是，请求该一级分类的二级分类列表并显示在gridView里面
	private void flush() {
		pr.setVisibility(View.VISIBLE);
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("store_id", Myapplication.store_id);
		ha.put("act", UrlPath.ACT_CATEGORY);
		ha.put("acttag", "2"); //表示请求二级商品列表
		ha.put("category_id",id);  //一级商品列表ID
		Log.i("test", "一级商品列表ID："+id);
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						String ss = arg0.toString();
						try {
							JSONObject jb = new JSONObject(ss);
							if (jb.getInt("code") == 1) {
								lv2list = new ArrayList<ShopTwoBean>();
								JSONArray ja = jb.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jj = ja.getJSONObject(i);
									ShopTwoBean sp = new ShopTwoBean();
									sp.setCategory2_id(jj
											.getString("category_id"));
									sp.setCategory2_name(jj
											.getString("category_name"));
									sp.setUrl(jj.getString("category_images"));
									lv2list.add(sp);
								}
								ad2.notifyDataSetChanged();

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pr.setVisibility(View.GONE);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						pr.setVisibility(View.GONE);
					}
				});
	}

	//一级商品列表
	private void initData() {
		if (lv1list == null) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("store_id", Myapplication.store_id);
			ha.put("act", UrlPath.ACT_CATEGORY);
			ha.put("acttag", "1"); //表示一级商品列表
			Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject arg0) {
							String content = arg0.toString();
							try {
								JSONObject jb = new JSONObject(content); //test
								JSONArray ja = jb.getJSONArray("list");
								lv1list = new ArrayList<Shop_oneBean>();
								for (int i = 0; i < ja.length(); i++) {
									Shop_oneBean shp = new Shop_oneBean();
									JSONObject jj = ja.getJSONObject(i);
									shp.setCategory1_id(jj
											.getString("category_id"));
									shp.setCategory1_name(jj
											.getString("category_name"));
									shp.setCategory1_intro(jj
											.getString("category_intro"));
									shp.setCategory1_images(jj
											.getString("category_images"));
									lv1list.add(shp);
									if (i == 0) {
										initlv2Data(jj
												.getString("category_id"));
										shp.setShow(true);
									}
								}
								ad1.notifyDataSetChanged();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
						}
					});
		} else {
			pr.setVisibility(View.GONE);
		}

	}

	//二级商品列表
	private void initlv2Data(String id) {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("store_id", Myapplication.store_id);
		ha.put("act", UrlPath.ACT_CATEGORY);
		ha.put("acttag", "2"); //表示二级商品列表
		ha.put("category_id",id); //一级分类ID
		Log.i("test", "一级列表请求二级列表--->"+Myapplication.getUrl(ha));
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						String ss = arg0.toString();
						try {
							JSONObject jb = new JSONObject(ss);
							if (jb.getInt("code") == 1) {
								lv2list = new ArrayList<ShopTwoBean>();
								JSONArray ja = jb.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jj = ja.getJSONObject(i);
									ShopTwoBean sp = new ShopTwoBean();
									sp.setCategory2_id(jj
											.getString("category_id"));
									sp.setCategory2_name(jj
											.getString("category_name"));
									sp.setUrl(jj.getString("category_images")); 
									lv2list.add(sp);
								}
								ad2.notifyDataSetChanged();

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pr.setVisibility(View.GONE);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						pr.setVisibility(View.GONE);

					}
				});
	}

	private void initlistener() {
		//一级商品列表的点击事件
		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg1.findViewById(R.id.nofull).getVisibility() == View.VISIBLE) {
				} else {
					for (int i = 0; i < lv1list.size(); i++) {
						if (arg2 == i) {
							lv1list.get(i).setShow(true);
						} else {
							lv1list.get(i).setShow(false);
						}

					}
					ad1.notifyDataSetChanged();
					id = (String) arg1.findViewById(R.id.full).getTag();
					flush();
				}

			}
		});
		
		//二级分类商品点击事件
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.id);
				TextView name = (TextView) arg1.findViewById(R.id.name);
				String idd = id.getText().toString();
				String namee = name.getText().toString();
				Intent in = new Intent(getActivity(), ShoptwoActivity.class);
				in.putExtra("id", idd);
				Log.i("test", "二级商品ID："+idd);
				in.putExtra("name", namee);
				startActivity(in);
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
		});
	}

	private void initView() {
		pr = (ProgressBar) v.findViewById(R.id.pr);
		gridView = (GridView) v.findViewById(R.id.gridview); //商品列表页面的二级产品展示
		ad2 = new Myad(); //二级商品适配器
		gridView.setAdapter(ad2); //绑定数据
		lv1 = (ListView) v.findViewById(R.id.lv1);  //一级商品列表ListView
		ad1 = new Myad1();  //一级商品列表适配器
		lv1.setAdapter(ad1); //绑定数据
	}

	@Override
	public void onPause() {
		super.onPause();
		Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
	}

	@Override
	public void onClick(View v) {

	}

	/**
	 * 展示二级商品的适配器
	 * @author Administrator
	 *
	 */
	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			if (lv2list == null) {
				return 0;
			} else {
				return lv2list.size();
			}

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
			Log.e("sdfgsgsg", "10");
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(
						R.layout.shoplistfragmentlv2_item, null);
			}
			ShopTwoBean shop = lv2list.get(position);
			Log.e("sdfgsgsg", "11");
			NetworkImageView photo = (NetworkImageView) convertView
					.findViewById(R.id.photo);
			/*
			 * Myapplication.client.getImageForNetImageView(shop.getUrl(),
			 * photo, R.drawable.ic_launcher);
			 */
			TextView id = (TextView) convertView.findViewById(R.id.id);
			id.setText(shop.getCategory2_id());
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(shop.getCategory2_name());
			Myapplication.client.getImageForNetImageView(shop.getUrl(),
					photo, R.drawable.ic_launcher);
//			NetworkImageView im = vh.photo;
			Log.e("sdfgsgsg", "12");
			return convertView;
		}
	}

	//一级商品适配器
	private class Myad1 extends BaseAdapter {

		@Override
		public int getCount() {
			if (lv1list == null) {
				return 0;
			} else {
				return lv1list.size();
			}

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
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(
						R.layout.shoplistfragmentlv1_item, null);
			}
			Shop_oneBean sp = lv1list.get(position);
			TextView full = (TextView) convertView.findViewById(R.id.full);
			TextView nofull = (TextView) convertView.findViewById(R.id.nofull);
			full.setTag(sp.getCategory1_id());
			nofull.setTag(sp.getCategory1_id());
			full.setText(sp.getCategory1_name());
			nofull.setText(sp.getCategory1_name());
			if (sp.getShow()) {
				nofull.setVisibility(View.VISIBLE);
				full.setVisibility(View.GONE);
				nofull.setTextColor(Color.parseColor("#ffffff"));
			} else {
				full.setVisibility(View.VISIBLE);
				nofull.setVisibility(View.GONE);
				full.setTextColor(Color.parseColor("#4d4547"));
			}
			return convertView;
		}

	}

}
