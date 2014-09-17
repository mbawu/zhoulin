package xinyuan.cn.zhoulin.activitys2.search;

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
import xinyuan.cn.zhoulin.model.Product;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

public class SearChResoutListActivity extends Activity implements
		OnClickListener {
	private View back;
	private TextView tv;
	private ListView lv;
	private List<Product> li;
	private Myad ad;
	private String name;
	private int page = 1;
	private int totalpage = 1;
	private int lastitem;
	private String type = "0";
	private PopupWindow pw;
	private View popuView;
	private ViewGroup populy;
	private LayoutInflater ly;
	private Button pop;
	private TextView moren;
	private TextView priceup;
	private TextView pricedown;
	private TextView personhot;
	private TextView newshop;
	private Button im;
	private View footView;
	private Boolean jiazai = true;
	private TextView noProductView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchlist);
		Myapplication.getInstance().addActivity(this);
		name = getIntent().getExtras().getString("name");
		initView();
		initpop();
		initData(type);
		initlistener();
	}

	private void initpop() {
		pw = new PopupWindow(popuView, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pw.setBackgroundDrawable(new BitmapDrawable());
	}

	private void initlistener() {
		back.setOnClickListener(this);
		pop.setOnClickListener(this);
		moren.setOnClickListener(this);
		pricedown.setOnClickListener(this);
		priceup.setOnClickListener(this);
		personhot.setOnClickListener(this);
		newshop.setOnClickListener(this);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView name = (TextView) arg1.findViewById(R.id.name);
				String id = (String) name.getTag();
				Intent in = new Intent(SearChResoutListActivity.this,
						Commodity.class);
				in.putExtra("id", id);
				startActivity(in);
				SearChResoutListActivity.this.overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
		});
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastitem == (page - 1) * 6 + 1 && jiazai) {
					jiazai = false;
					im.setVisibility(View.VISIBLE);
					footView.setVisibility(View.VISIBLE);
					initData(type);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastitem = totalItemCount;
			}
		});

	}

	private void initData(String type) {
		tv.setText(name);
		if (page <= totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("act", UrlPath.ACT_PRODUCTLIST);
			ha.put("store_id", Myapplication.store_id);
			ha.put("keyname", name);
			ha.put("page", "" + page);
			ha.put("sort_type", type);
			ha.put("per", "6");
			Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
					new Listener<JSONObject>() {
						public void onResponse(JSONObject response) {
							String content = response.toString();
							try {
								JSONObject jb = new JSONObject(content);
								if (jb.getInt("code") == 1) {
									page = page + 1;
									totalpage = jb.getInt("totalpage");
									JSONArray ja = jb.getJSONArray("list");
									if(ja.length()==0)
									{
										noProductView.setText("对不起，没有搜索到任何商品！");
										noProductView.setVisibility(View.VISIBLE);
										return;
									}
									for (int i = 0; i < ja.length(); i++) {
										JSONObject jbb = (JSONObject) ja.get(i);
										Product cb = new Product();
										cb.setProduct_id(jbb
												.getString("product_id"));
										cb.setProduct_name(jbb
												.getString("product_name"));
										cb.setProduct_photo(jbb
												.getString("product_photo"));
										cb.setStore_price(Double.parseDouble(jbb
												.getString("store_price")));
										li.add(cb);
									}
									ad.notifyDataSetChanged();
									footView.setVisibility(View.GONE);
									im.setVisibility(View.GONE);
								}
							} catch (JSONException e) {
								footView.setVisibility(View.GONE);
								e.printStackTrace();
							}
							jiazai = true;
						}
					}, new ErrorListener() {
						public void onErrorResponse(VolleyError error) {
							footView.setVisibility(View.GONE);
							jiazai = true;
						}
					});
		} else {
			jiazai = true;
		}

	}

	private void initView() {
		footView = Myapplication.lf.inflate(R.layout.footview, null);
		im = (Button) findViewById(R.id.im);
		ly = LayoutInflater.from(SearChResoutListActivity.this);
		popuView = ly.inflate(R.layout.popupwindow, null);
		populy = (ViewGroup) popuView.findViewById(R.id.populy);
		pop = (Button) findViewById(R.id.pop);
		back = findViewById(R.id.back);
		moren = (TextView) popuView.findViewById(R.id.moren);
		priceup = (TextView) popuView.findViewById(R.id.priceup);
		pricedown = (TextView) popuView.findViewById(R.id.pricedown);
		personhot = (TextView) popuView.findViewById(R.id.personhot);
		newshop = (TextView) popuView.findViewById(R.id.newshop);
		tv = (TextView) findViewById(R.id.tv);
		lv = (ListView) findViewById(R.id.lv);
		ad = new Myad();
		li = new ArrayList<Product>();
		lv.addFooterView(footView);
		lv.setAdapter(ad);
		noProductView=(TextView) findViewById(R.id.noProductView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back: //返回按钮
			SearChResoutListActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;
		case R.id.moren: //默认排序
			Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
			li.clear();
			for (int i = 0; i < populy.getChildCount(); i++) {
				populy.getChildAt(i)
						.setBackgroundResource(R.drawable.popu_item);
			}
			v.setBackgroundResource(R.drawable.popu_iteman);
			pw.dismiss();
			page = 1;
			totalpage = 1;
			im.setVisibility(View.VISIBLE);
			footView.setVisibility(View.VISIBLE);
			initData("0");
			type = "0";
			break;
		case R.id.personhot: //按人气热度排序
			Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
			li.clear();
			for (int i = 0; i < populy.getChildCount(); i++) {
				populy.getChildAt(i)
						.setBackgroundResource(R.drawable.popu_item);
			}
			v.setBackgroundResource(R.drawable.popu_iteman);
			pw.dismiss();
			page = 1;
			totalpage = 1;
			im.setVisibility(View.VISIBLE);
			footView.setVisibility(View.VISIBLE);
			initData("3");
			type = "3";
			break;
		case R.id.pricedown: //价格降序排序
			Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
			li.clear();
			for (int i = 0; i < populy.getChildCount(); i++) {
				populy.getChildAt(i)
						.setBackgroundResource(R.drawable.popu_item);
			}
			v.setBackgroundResource(R.drawable.popu_iteman);
			pw.dismiss();
			page = 1;
			totalpage = 1;
			im.setVisibility(View.VISIBLE);
			footView.setVisibility(View.VISIBLE);
			initData("2");
			type = "2";
			break;
		case R.id.priceup: //价格升序排序
			Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
			li.clear();
			for (int i = 0; i < populy.getChildCount(); i++) {
				populy.getChildAt(i)
						.setBackgroundResource(R.drawable.popu_item);
			}
			v.setBackgroundResource(R.drawable.popu_iteman);
			pw.dismiss();
			page = 1;
			totalpage = 1;
			im.setVisibility(View.VISIBLE);
			footView.setVisibility(View.VISIBLE);
			initData("1");
			type = "1";
			break;
		case R.id.newshop: //按新品排序
			Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
			li.clear();
			for (int i = 0; i < populy.getChildCount(); i++) {
				populy.getChildAt(i)
						.setBackgroundResource(R.drawable.popu_item);
			}
			v.setBackgroundResource(R.drawable.popu_iteman);
			pw.dismiss();
			page = 1;
			totalpage = 1;
			im.setVisibility(View.VISIBLE);
			footView.setVisibility(View.VISIBLE);
			initData("4");
			type = "4";
			break;
		case R.id.pop: //排序按钮的点击事件
			pw.showAsDropDown(v);
			pw.setFocusable(true);
			pw.setOutsideTouchable(true);
			pw.update();
			break;
		}
	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK) { // KEYCODE_BACK=4;
			SearChResoutListActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);

			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Myapplication.tiao) {
			SearChResoutListActivity.this.finish();
		}

	}

	/**
	 * 显示查询结果的adapter
	 * @author Administrator
	 *
	 */
	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			if (li == null) {
				return 0;
			} else {
				return li.size();
			}

		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(
						R.layout.searchlist_item, null);
			}

			Product cb = li.get(position);
			final NetworkImageView photo = (NetworkImageView) convertView
					.findViewById(R.id.photo);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView id = (TextView) convertView.findViewById(R.id.id);
			TextView price = (TextView) convertView.findViewById(R.id.price);
			Myapplication.client.getImageForNetImageView(cb.getProduct_photo(),
					photo, R.drawable.ic_launcher);
			name.setText(cb.getProduct_name());
			name.setTag(cb.getProduct_id());
			price.setText("￥  " + cb.getStore_price());
			return convertView;
		}
	}

	protected void onPause() {
		super.onPause();
		Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
	}

}
