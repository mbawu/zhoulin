package xinyuan.cn.zhoulin.activitys2.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.Commodity;
import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import xinyuan.cn.zhoulin.model.CommodityBean;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

public class ShoptwoActivity extends Activity implements OnClickListener,
		OnGestureListener {
	private View back;
	private TextView title;
	private LinearLayout ly;
	private TextView num;
	private ListView lv;
	private Myad ad;
	private List<CommodityBean> lvlist;
	private String id; // 二级ID
	private String idd; // 三级ID
	private String cache_id;
	private String name;
	private String sort_type = "0"; // 排序ID
	private int page = 1;
	private int totalpage = 1;
	private LayoutParams lyg;
	private View moreview;
	private int lastitem;
	private GestureDetector gd;
	private Button pop;
	private View popuView;
	private ViewGroup populy;
	private PopupWindow pw;
	private TextView moren;
	private TextView priceup;
	private TextView pricedown;
	private TextView personhot;
	private TextView newshop;
	private Button bt;
	private Boolean jiazai = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shopfilterfragment);
		id = getIntent().getExtras().getString("id");
		name = getIntent().getExtras().getString("name");
		gd = new GestureDetector(this, this);
		moreview = Myapplication.lf.inflate(R.layout.footview, null);
		if (lvlist == null) {
			lvlist = new ArrayList<CommodityBean>();
		}
		initView();
//		initData();
		initlvData(idd, sort_type);
		initpop();
		initlistener();
	}

	private void initpop() {
		pw = new PopupWindow(popuView, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pw.setBackgroundDrawable(new BitmapDrawable());
	}

	private void initlistener() {
		pop.setOnClickListener(this);
		back.setOnClickListener(this);
		moren.setOnClickListener(this);
		pricedown.setOnClickListener(this);
		priceup.setOnClickListener(this);
		personhot.setOnClickListener(this);
		newshop.setOnClickListener(this);
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastitem == (page - 1) * 5 + 1 && jiazai) {
					jiazai = false;
					bt.setVisibility(View.VISIBLE);
					moreview.setVisibility(View.VISIBLE);
					initlvData(idd, sort_type);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastitem = totalItemCount;
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView name = (TextView) arg1.findViewById(R.id.name);
				String id = (String) name.getTag();
				Intent in = new Intent(ShoptwoActivity.this, Commodity.class);
				in.putExtra("id", id);
				startActivity(in);
				ShoptwoActivity.this.overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
		});

	}

	//显示三级商品列表
	private void initlvData(String idd, String type) {
		if (page <= totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("act", UrlPath.ACT_PRODUCTLIST);
			ha.put("category_id", id);
			ha.put("cache_id", getIntent().getStringExtra("cache_id"));
			ha.put("sort_type", type);
			ha.put("store_id", Myapplication.store_id);
			ha.put("page", "" + page);
			ha.put("per", "5");
			Log.i("test", "cache_id-->"+getIntent().getStringExtra("cache_id"));
			Log.i("test", "三级申请列表->"+Myapplication.getUrl(ha));
			Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								
								if (response.getInt("code") == 1) {
									JSONArray ja = response
											.getJSONArray("list");
									if (ja.length() == 0 && page == 1) {
										Toast.makeText(ShoptwoActivity.this,
												"抱歉，此类商品缺货中。。。", 2000).show();
									}

									for (int i = 0; i < ja.length(); i++) {
										JSONObject jb = ja.getJSONObject(i);
										CommodityBean cb = new CommodityBean();
										cb.setProduct_id(jb
												.getString("product_id"));
										cb.setProduct_name(jb
												.getString("product_name"));
										cb.setProduct_photo(jb
												.getString("product_photo"));
										cb.setStore_price(Double.parseDouble(jb
												.getString("store_price")));
										lvlist.add(cb);
									}
									ad.notifyDataSetChanged();
									moreview.setVisibility(View.GONE);
									bt.setVisibility(View.GONE);
									page = page + 1;
									totalpage = response.getInt("totalpage");
								}
							} catch (JSONException e) {
								moreview.setVisibility(View.GONE);
								bt.setVisibility(View.GONE);
								e.printStackTrace();
							}
							jiazai = true;
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							moreview.setVisibility(View.GONE);
							bt.setVisibility(View.GONE);
							jiazai = true;
						}
					});
		} else {
			moreview.setVisibility(View.GONE);
			bt.setVisibility(View.GONE);
			jiazai = true;
		}

	}

	//二级分类请求三级分类
	private void initData() {
		title.setText(name); // 初始化标题数据
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("store_id", Myapplication.store_id);
		ha.put("act", UrlPath.ACT_CATEGORY);
		ha.put("acttag", "3"); //表示请求三级商品列表
		ha.put("category_id",id); //二级分类ID
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								JSONArray ja = (JSONArray) response.get("list");
								if (ja.length() == 0) {
									Toast.makeText(ShoptwoActivity.this,
											"商品数据为空哦！", 2000).show();
									moreview.setVisibility(View.GONE);
									bt.setVisibility(View.GONE);
									return;
								}
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									TextView tv = (TextView) Myapplication.lf
											.inflate(R.layout.tv, null);
									if (i == 0) {
										tv.setTag(jb.getString("category_id"));
										tv.setTextColor(Color
												.parseColor("#62D036"));
										idd = jb.getString("category_id"); // 三级ID赋值
										cache_id=jb.getString("cache_id"); //分类附加ID
										initlvData(idd, sort_type); // listView刷新
										tv.setBackgroundResource(R.drawable.tv_changer);
										tv.setGravity(Gravity.CENTER);
										tv.setText(jb
												.getString("category_name"));
									} else {

										tv.setTag(jb.getString("category_id"));
										tv.setTextColor(Color
												.parseColor("#333333"));
										tv.setBackgroundResource(R.drawable.tv_changerno);
										tv.setGravity(Gravity.CENTER);
										tv.setText(jb
												.getString("category_name"));
									}
									tv.setOnClickListener(ShoptwoActivity.this);
									ly.addView(tv, lyg);
								}

							}
						} catch (JSONException e) {
							moreview.setVisibility(View.GONE);
							bt.setVisibility(View.GONE);
							e.printStackTrace();
						}

					}

				}, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						moreview.setVisibility(View.GONE);
						bt.setVisibility(View.GONE);
					}
				});

	}

	private void initView() {
		bt = (Button) findViewById(R.id.bt);
		popuView = Myapplication.lf.inflate(R.layout.popupwindow, null);
		populy = (ViewGroup) popuView.findViewById(R.id.populy);
		pop = (Button) findViewById(R.id.pop);
		moren = (TextView) popuView.findViewById(R.id.moren);
		priceup = (TextView) popuView.findViewById(R.id.priceup);
		pricedown = (TextView) popuView.findViewById(R.id.pricedown);
		personhot = (TextView) popuView.findViewById(R.id.personhot);
		newshop = (TextView) popuView.findViewById(R.id.newshop);
		lyg = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		back = findViewById(R.id.back);
		num = (TextView) findViewById(R.id.num);
		title = (TextView) findViewById(R.id.title);
		title.setText("商品列表");
		ly = (LinearLayout) findViewById(R.id.ly);
		lv = (ListView) findViewById(R.id.lv);
		ad = new Myad();
		lv.addFooterView(moreview);
		lv.setAdapter(ad);
	}

	@Override
	public void onPause() {
		super.onPause();
		Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
	}

	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			if (lvlist == null) {
				return 0;
			} else {
				return lvlist.size();
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
			CommodityBean cb = lvlist.get(position);
			ViewHolder vh;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = Myapplication.lf.inflate(
						R.layout.shopfilterfragment_item, null);
				vh.name = (TextView) convertView.findViewById(R.id.name);
				vh.photo = (NetworkImageView) convertView
						.findViewById(R.id.photo);
				vh.price = (TextView) convertView.findViewById(R.id.price);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.name.setText(cb.getProduct_name());
			vh.name.setTag(cb.getProduct_id());
			vh.price.setText("￥  " + cb.getStore_price());
			NetworkImageView im = vh.photo;
			vh.price.setTag(im);
			Myapplication.client.getImageForNetImageView(cb.getProduct_photo(),
					im, R.drawable.ic_launcher);
			return convertView;
		}
	}

	private class ViewHolder {
		public NetworkImageView photo;
		public TextView name;
		public TextView price;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			ShoptwoActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;
		case R.id.moren:
			Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
			lvlist.clear();
			for (int i = 0; i < populy.getChildCount(); i++) {
				populy.getChildAt(i)
						.setBackgroundResource(R.drawable.popu_item);
			}
			v.setBackgroundResource(R.drawable.popu_iteman);
			pw.dismiss();
			page = 1;
			totalpage = 1;
			bt.setVisibility(View.VISIBLE);
			moreview.setVisibility(View.VISIBLE);
			initlvData(idd, "0");
			sort_type = "0";
			break;
		case R.id.personhot:
			Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
			lvlist.clear();
			for (int i = 0; i < populy.getChildCount(); i++) {
				populy.getChildAt(i)
						.setBackgroundResource(R.drawable.popu_item);
			}
			v.setBackgroundResource(R.drawable.popu_iteman);
			pw.dismiss();
			page = 1;
			totalpage = 1;
			bt.setVisibility(View.VISIBLE);
			moreview.setVisibility(View.VISIBLE);
			initlvData(idd, "3");
			sort_type = "3";
			break;
		case R.id.pricedown:
			Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
			lvlist.clear();
			for (int i = 0; i < populy.getChildCount(); i++) {
				populy.getChildAt(i)
						.setBackgroundResource(R.drawable.popu_item);
			}
			v.setBackgroundResource(R.drawable.popu_iteman);
			pw.dismiss();
			page = 1;
			totalpage = 1;
			bt.setVisibility(View.VISIBLE);
			moreview.setVisibility(View.VISIBLE);
			initlvData(idd, "2");
			sort_type = "2";
			break;
		case R.id.priceup:
			Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
			lvlist.clear();
			for (int i = 0; i < populy.getChildCount(); i++) {
				populy.getChildAt(i)
						.setBackgroundResource(R.drawable.popu_item);
			}
			v.setBackgroundResource(R.drawable.popu_iteman);
			pw.dismiss();
			page = 1;
			totalpage = 1;
			bt.setVisibility(View.VISIBLE);
			moreview.setVisibility(View.VISIBLE);
			initlvData(idd, "1");
			sort_type = "1";
			break;
		case R.id.newshop:
			Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
			lvlist.clear();
			for (int i = 0; i < populy.getChildCount(); i++) {
				populy.getChildAt(i)
						.setBackgroundResource(R.drawable.popu_item);
			}
			v.setBackgroundResource(R.drawable.popu_iteman);
			pw.dismiss();
			page = 1;
			totalpage = 1;
			bt.setVisibility(View.VISIBLE);
			moreview.setVisibility(View.VISIBLE);
			initlvData(idd, "4");
			sort_type = "4";
			break;
		case R.id.pop:
			pw.showAsDropDown(v);
			pw.setFocusable(true);
			pw.setOutsideTouchable(true);
			pw.update();
			break;
		default:
			String idd = (String) v.getTag();
			if (idd.equals(ShoptwoActivity.this.idd)) {

			} else {
				page = 1;
				totalpage = 1;
				lvlist.clear();
				ShoptwoActivity.this.idd = idd;
				bt.setVisibility(View.VISIBLE);
				moreview.setVisibility(View.VISIBLE);
				initlvData(idd, sort_type);
				for (int i = 0; i < ly.getChildCount(); i++) {
					// ly.getChildAt(i).setBackgroundResource(0);
					TextView tv = (TextView) ly.getChildAt(i);
					tv.setTextColor(Color.parseColor("#333333"));
					tv.setBackgroundResource(R.drawable.tv_changerno);
				}
				TextView vvv = (TextView) v;
				vvv.setTextColor(Color.parseColor("#62D036"));
				vvv.setBackgroundResource(R.drawable.tv_changer);
				// v.setBackgroundResource(R.drawable.shop_ly_btt);
			}

			break;
		}

	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e2.getX() - e1.getX() > 100 && e1.getY() - e2.getY() < 20) {
			ShoptwoActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK) { // KEYCODE_BACK=4;
			ShoptwoActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			return true;
		}
		return true;
	}

	protected void onRestart() {
		super.onRestart();
		if (Myapplication.tiao) {
			ShoptwoActivity.this.finish();
		}
	}

}
