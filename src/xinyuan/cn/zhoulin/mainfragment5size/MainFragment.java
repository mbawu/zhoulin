package xinyuan.cn.zhoulin.mainfragment5size;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.Commodity;
import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import xinyuan.cn.zhoulin.model.Product;
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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

public class MainFragment extends Fragment implements OnClickListener,
		OnItemClickListener {
	private String tag = "SalesFragment";
	private View v;
	public Boolean gc = false;
	private TextView recommendtv;
	private TextView persontv;
	private TextView newshoptv;
	private TextView salepricetv;
	private ViewFlipper vf;
	private TextView tv;
	private LayoutParams ly;
	private ProgressBar pr;
	private ViewFlipper shopvf;
	private GridView recommendgridview;
	private GridView persongridview;
	private GridView newshopgridview;
	private GridView salepricegridview;
	private ArrayList<Product> gd1ist1;
	private ArrayList<Product> gd1ist2;
	private ArrayList<Product> gd1ist3;
	private ArrayList<Product> gd1ist4;
	private Myad1 ad1;
	private Myad2 ad2;
	private Myad3 ad3;
	private Myad4 ad4;
	private ScrollView sc;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		gc = true;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.salefragment, container, false);
			initView();
			initData();
			initlistener();
			if (sc == null) {
				sc = (ScrollView) v.findViewById(R.id.sc);
			}
			sc.smoothScrollTo(0, 0); //设置滚动条焦点
		} else {
			if (gd1ist1 == null) {
				pr.setVisibility(View.VISIBLE);
				initrecommendData();
			}
			if (gd1ist2 == null) {
				pr.setVisibility(View.VISIBLE);
				initpersonData();
			}
			if (gd1ist3 == null) {
				pr.setVisibility(View.VISIBLE);
				initnewshopData();
			}
			if (gd1ist4 == null) {
				pr.setVisibility(View.VISIBLE);
				inittepriceData();
			}
		}
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}
		return v;
	}

	private void initlistener() {
		recommendtv.setOnClickListener(this);
		persontv.setOnClickListener(this);
		newshoptv.setOnClickListener(this);
		salepricetv.setOnClickListener(this);
		recommendgridview.setOnItemClickListener(this);
		persongridview.setOnItemClickListener(this);
		newshopgridview.setOnItemClickListener(this);
		salepricegridview.setOnItemClickListener(this);
	}

	private void initData() {
		initvfData(); // 首页广告
		initrecommendData(); // 首页推荐
		initpersonData(); // 人气排行
		initnewshopData(); // 新品上架
		inittepriceData(); // 热销特卖
	}

	private void initView() { // 初始化布局静态View
		if (ly == null) {
			ly = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
					Myapplication.width * 3 / 8);
		}
		recommendtv = (TextView) v.findViewById(R.id.recommendtv);
		persontv = (TextView) v.findViewById(R.id.persontv);
		newshoptv = (TextView) v.findViewById(R.id.newshoptv);
		salepricetv = (TextView) v.findViewById(R.id.salepricetv);
		pr = (ProgressBar) v.findViewById(R.id.pr);
		vf = (ViewFlipper) v.findViewById(R.id.vf);
		tv = (TextView) v.findViewById(R.id.tv);
		shopvf = (ViewFlipper) v.findViewById(R.id.shopvf);
		recommendgridview = (GridView) v.findViewById(R.id.recommendgridview);
		persongridview = (GridView) v.findViewById(R.id.persongridview);
		newshopgridview = (GridView) v.findViewById(R.id.newshopgridview);
		salepricegridview = (GridView) v.findViewById(R.id.salepricegridview);
		ad1 = new Myad1();
		ad2 = new Myad2();
		ad3 = new Myad3();
		ad4 = new Myad4();
		recommendgridview.setAdapter(ad1);
		persongridview.setAdapter(ad2);
		newshopgridview.setAdapter(ad3);
		salepricegridview.setAdapter(ad4);
	}

	 //首页广告
	private void initvfData() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("store_id", Myapplication.store_id);
		ha.put("act",UrlPath.ACT_AD);  //首页广告
		ha.put("page",Myapplication.adPage); //显示第几页的广告
		ha.put("pagesize",Myapplication.adItemNum); //显示广告的条数
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha, // 首页广告
				new Listener<JSONObject>() {
					private String Tag="act_ad";

					public void onResponse(JSONObject arg0) {
						String content = arg0.toString();
						try {
							JSONObject jb = new JSONObject(content);//test1
							if (jb.getInt("code") == 1) {
								JSONArray ja = jb.getJSONArray("list");
								Log.i(Tag, ja.toString());
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jbb = ja.getJSONObject(i);
									String path = jbb.getString("ad_image");
									Log.i(Tag, path);
									if (getActivity() == null) {
										return;
									}
									NetworkImageView nv = new NetworkImageView(
											getActivity());
									Myapplication.client
											.getImageForNetImageView(path, nv,
													R.drawable.ic_launcher);
									vf.addView(nv, ly);
								}
								if (getActivity() == null) {
									return;
								}
								vf.setInAnimation(getActivity(),
										R.anim.view_in_from_right);
								vf.setOutAnimation(getActivity(),
										R.anim.view_out_to_left);
								vf.startFlipping();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						if (getActivity() == null) {
							return;
						}
						Toast.makeText(getActivity(), "首页广告为NULL", 1).show();
					}
				});

	}

	private void initrecommendData() { // 首页推荐
		HashMap<String, String> te = new HashMap<String, String>();
		te.put("store_id", Myapplication.store_id);
		te.put("act", UrlPath.ACT_PRODUCT); 
		te.put("acttag", UrlPath.ACT_PRODUCT_SPECIAL); //特价商品
		te.put("pagesize", Myapplication.productItemNum); //显示多少个商品
		Log.i("zhoulin", Myapplication.getUrl(te));
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, te,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {

						try {
							if (response.getInt("code") == 1) {
								if (gd1ist1 == null) {
									gd1ist1 = new ArrayList<Product>();
								} else {
									gd1ist1.clear();
								}
								JSONArray ja = response.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									Product cb = new Product();
									cb.setProduct_id(jb.getString("product_id"));
									Log.e("asdfsgsdfhdfhdfh",
											jb.getString("product_id") + null);
									cb.setProduct_photo(jb
											.getString("product_photo"));
									cb.setStore_price(jb
											.getDouble("store_price"));
									cb.setProduct_name(jb
											.getString("product_name"));
									gd1ist1.add(cb);
								}
								ad1.notifyDataSetChanged();
								/*
								 * new Utility()
								 * .setListViewHeightBasedOnChildren
								 * (recommendgridview);
								 */
							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pr.setVisibility(View.GONE);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (getActivity() == null) {
							return;
						}

					}
				});

	}

	private void initpersonData() { // 人气排行
		HashMap<String, String> te = new HashMap<String, String>();
		te.put("store_id", Myapplication.store_id);
		te.put("act", UrlPath.ACT_PRODUCT); 
		te.put("acttag", UrlPath.ACT_PRODUCT_HOT); //人气商品
		te.put("pagesize", Myapplication.productItemNum); //显示多少个商品
		Log.i("zhoulin", Myapplication.getUrl(te));
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, te,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {

						try {
							if (response.getInt("code") == 1) {
								if (gd1ist2 == null) {
									gd1ist2 = new ArrayList<Product>();
								} else {
									gd1ist2.clear();
								}
								JSONArray ja = response.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									Product cb = new Product();
									cb.setProduct_id(jb.getString("product_id"));
									cb.setProduct_photo(jb
											.getString("product_photo"));
									cb.setStore_price(jb
											.getDouble("store_price"));
									cb.setProduct_name(jb
											.getString("product_name"));
									gd1ist2.add(cb);
								}
								ad2.notifyDataSetChanged();
								/*
								 * new Utility()
								 * .setListViewHeightBasedOnChildren
								 * (persongridview);
								 */
							} else {

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pr.setVisibility(View.GONE);

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (getActivity() == null) {
							return;
						}
					}
				});

	}

	private void initnewshopData() { // 新品上架
		HashMap<String, String> newProductsRequest = new HashMap<String, String>();
		newProductsRequest.put("store_id", Myapplication.store_id);
		newProductsRequest.put("act",UrlPath.ACT_PRODUCT); //首页商品
		newProductsRequest.put("acttag", UrlPath.ACT_PRODUCT_NEW); //最新商品
		newProductsRequest.put("pagesize", Myapplication.productItemNum); //显示多少个商品
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, newProductsRequest,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {

						try {
							if (response.getInt("code") == 1) {
								if (gd1ist3 == null) {
									gd1ist3 = new ArrayList<Product>();
								} else {
									gd1ist3.clear();
								}
								//获取返回的产品的集合
								JSONArray productList = response.getJSONArray("list");
								for (int i = 0; i < productList.length(); i++) {
									JSONObject productInfo = productList.getJSONObject(i);
									Product product = new Product();
									product.setProduct_id(productInfo.getString("product_id"));
									product.setProduct_photo(productInfo
											.getString("product_photo"));
									product.setStore_price(productInfo
											.getDouble("store_price"));
									product.setProduct_name(productInfo
											.getString("product_name"));
									gd1ist3.add(product);
								}
								ad3.notifyDataSetChanged();
								/*
								 * new Utility()
								 * .setListViewHeightBasedOnChildren
								 * (newshopgridview);
								 */

							} else {

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pr.setVisibility(View.GONE);

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (getActivity() == null) {
							return;
						}
					}
				});

	}

	private void inittepriceData() { // 热销特卖
		HashMap<String, String> te = new HashMap<String, String>();
		te.put("store_id", Myapplication.store_id);
		te.put("act",UrlPath.ACT_PRODUCT); //首页商品
		te.put("acttag", UrlPath.ACT_PRODUCT_HOT); //热卖商品
		te.put("pagesize", Myapplication.productItemNum); //显示多少个商品
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, te,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								if (gd1ist4 == null) {
									gd1ist4 = new ArrayList<Product>();
								} else {
									gd1ist4.clear();
								}
								JSONArray ja = response.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									Product cb = new Product();
									cb.setProduct_id(jb.getString("product_id"));
									cb.setProduct_photo(jb
											.getString("product_photo"));
									cb.setStore_price(jb
											.getDouble("store_price"));
									cb.setProduct_name(jb
											.getString("product_name"));
									gd1ist4.add(cb);
								}
								ad4.notifyDataSetChanged();
								/*
								 * new Utility()
								 * .setListViewHeightBasedOnChildren
								 * (salepricegridview);
								 */
							} else {

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pr.setVisibility(View.GONE);
					}
				}, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						if (getActivity() == null) {
							return;
						}
						Toast.makeText(getActivity(), "特价商品请求失败", 2).show();
					}
				});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void onPause() {
		super.onPause();
		Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	private class Myad1 extends BaseAdapter {

		@Override
		public int getCount() {
			if (gd1ist1 == null) {
				return 0;
			} else {
				return gd1ist1.size();
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
			Product cb = gd1ist1.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(R.layout.grid_image,
						null);
			}
			TextView price = (TextView) convertView.findViewById(R.id.price);
			NetworkImageView im = (NetworkImageView) convertView
					.findViewById(R.id.photo);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(cb.getProduct_name());
			price.setText("" + cb.getStore_price());
			im.setTag(cb.getProduct_id());
			Myapplication.client.getImageForNetImageView(cb.getProduct_photo(),
					im, R.drawable.ic_launcher);
			return convertView;
		}

	}

	private class Myad2 extends BaseAdapter {

		@Override
		public int getCount() {
			if (gd1ist2 == null) {
				return 0;
			} else {
				return gd1ist2.size();
			}
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Product cb = gd1ist2.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(R.layout.grid_image,
						null);
			}
			TextView price = (TextView) convertView.findViewById(R.id.price);
			NetworkImageView im = (NetworkImageView) convertView
					.findViewById(R.id.photo);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(cb.getProduct_name());
			price.setText("" + cb.getStore_price());
			im.setTag(cb.getProduct_id());
			Myapplication.client.getImageForNetImageView(cb.getProduct_photo(),
					im, R.drawable.ic_launcher);
			return convertView;
		}

	}

	private class Myad3 extends BaseAdapter {

		@Override
		public int getCount() {
			if (gd1ist3 == null) {
				return 0;
			} else {
				return gd1ist3.size();
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
			Product cb = gd1ist3.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(R.layout.grid_image,
						null);
			}
			TextView price = (TextView) convertView.findViewById(R.id.price);
			NetworkImageView im = (NetworkImageView) convertView
					.findViewById(R.id.photo);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(cb.getProduct_name());
			price.setText("" + cb.getStore_price());
			im.setTag(cb.getProduct_id());
			Myapplication.client.getImageForNetImageView(cb.getProduct_photo(),
					im, R.drawable.ic_launcher);
			return convertView;
		}

	}

	private class Myad4 extends BaseAdapter {

		@Override
		public int getCount() {
			if (gd1ist4 == null) {
				return 0;
			} else {
				return gd1ist4.size();
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
			Product cb = gd1ist4.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(R.layout.grid_image,
						null);
			}
			TextView price = (TextView) convertView.findViewById(R.id.price);
			NetworkImageView im = (NetworkImageView) convertView
					.findViewById(R.id.photo);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(cb.getProduct_name());
			price.setText("" + cb.getStore_price());
			im.setTag(cb.getProduct_id());
			Myapplication.client.getImageForNetImageView(cb.getProduct_photo(),
					im, R.drawable.ic_launcher);
			return convertView;
		}

	}

	/**
	 * 点击首页选项卡菜单的点击事件，设置相关的字体颜色并连接服务器取出数据并展示
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recommendtv: //点击首页推荐的事件
			sc.smoothScrollTo(0, 0);
			//点击首页推荐以后改变其他选项卡字体颜色以及底部图片
			recommendtv
					.setTextColor(getResources().getColor(R.color.textcolor));
			persontv.setTextColor(Color.parseColor("#333333"));
			newshoptv.setTextColor(Color.parseColor("#333333"));
			salepricetv.setTextColor(Color.parseColor("#333333"));
			recommendtv.setBackgroundResource(R.drawable.tv_changer);
			persontv.setBackgroundResource(R.drawable.tv_changerno);
			newshoptv.setBackgroundResource(R.drawable.tv_changerno);
			salepricetv.setBackgroundResource(R.drawable.tv_changerno);
			shopvf.setDisplayedChild(0);
			//如果gridView没有数据，就先设置进度条可见然后连接服务器并把数据取出来
			if (gd1ist1 == null) {
				pr.setVisibility(View.VISIBLE);
				initrecommendData();
			}
			tv.setText("首页推荐");
			break;
		case R.id.persontv: //人气排行
			sc.smoothScrollTo(0, 0);
			recommendtv.setTextColor(Color.parseColor("#333333"));
			persontv.setTextColor(getResources().getColor(R.color.textcolor));
			newshoptv.setTextColor(Color.parseColor("#333333"));
			salepricetv.setTextColor(Color.parseColor("#333333"));
			recommendtv.setBackgroundResource(R.drawable.tv_changerno);
			persontv.setBackgroundResource(R.drawable.tv_changer);
			newshoptv.setBackgroundResource(R.drawable.tv_changerno);
			salepricetv.setBackgroundResource(R.drawable.tv_changerno);
			shopvf.setDisplayedChild(1);
			if (gd1ist2 == null) {
				pr.setVisibility(View.VISIBLE);
				initpersonData();
			}
			tv.setText("人气排行");
			break;
		case R.id.newshoptv:  
			sc.smoothScrollTo(0, 0);
			recommendtv.setTextColor(Color.parseColor("#333333"));
			persontv.setTextColor(Color.parseColor("#333333"));
			newshoptv.setTextColor(getResources().getColor(R.color.textcolor));
			salepricetv.setTextColor(Color.parseColor("#333333"));
			recommendtv.setBackgroundResource(R.drawable.tv_changerno);
			persontv.setBackgroundResource(R.drawable.tv_changerno);
			newshoptv.setBackgroundResource(R.drawable.tv_changer);
			salepricetv.setBackgroundResource(R.drawable.tv_changerno);
			shopvf.setDisplayedChild(2);
			if (gd1ist3 == null) {
				pr.setVisibility(View.VISIBLE);
				initnewshopData();
			}
			tv.setText("新品上架");
			break;
		case R.id.salepricetv:
			sc.smoothScrollTo(0, 0);
			recommendtv.setTextColor(Color.parseColor("#333333"));
			persontv.setTextColor(Color.parseColor("#333333"));
			newshoptv.setTextColor(Color.parseColor("#333333"));
			salepricetv
					.setTextColor(getResources().getColor(R.color.textcolor));
			recommendtv.setBackgroundResource(R.drawable.tv_changerno);
			persontv.setBackgroundResource(R.drawable.tv_changerno);
			newshoptv.setBackgroundResource(R.drawable.tv_changerno);
			salepricetv.setBackgroundResource(R.drawable.tv_changer);
			shopvf.setDisplayedChild(3);
			if (gd1ist4 == null) {
				pr.setVisibility(View.VISIBLE);
				inittepriceData();
			}
			tv.setText("热销特卖");
			break;

		default:
			break;
		}

	}

	/**
	 * 点击商品图片的事件，取出图片的商品ID并使用从左到右的滑动效果跳转到Commodity.class，即商品详情页面
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		View v = arg1;
		String id = (String) v.findViewById(R.id.photo).getTag();
		Log.e("asdfsgsdfhdfhdfh", id + null);
		Intent in = new Intent(getActivity(), Commodity.class);
		in.putExtra("id", id);
		getActivity().startActivity(in);
		getActivity().overridePendingTransition(R.anim.view_in_from_right,
				R.anim.view_out_to_left);

	}

}
