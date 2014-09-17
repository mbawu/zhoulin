package xinyuan.cn.zhoulin;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.model.ShopEvaluateBean;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
/**
 * 
 */
public class ShopEvaluate extends Activity implements OnClickListener {
	private View back;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private ViewFlipper vf;
	private ListView lv1;
	private ListView lv2;
	private ListView lv3;
	private ArrayList<ShopEvaluateBean> lv1list;
	private ArrayList<ShopEvaluateBean> lv2list;
	private ArrayList<ShopEvaluateBean> lv3list;
	private Lv1Adapter ad1;
	private Lv2Adapter ad2;
	private Lv3Adapter ad3;
	private int lv1page = 1;
	private int lv2page = 1;
	private int lv3page = 1;
	private int lv1totalpage = 1;
	private int lv2totalpage = 1;
	private int lv3totalpage = 1;
	private String id;
	private View footview1;
	private View footview2;
	private View footview3;
	private int lv1lastitem;
	private int lv2lastitem;
	private int lv3lastitem;
	private LinearLayout ly;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shopevaluate);
		id = getIntent().getExtras().getString("id");
		initView();
		initdata();
		initlistener();
	}
	/**
	 * 
	 */
	private void initdata() {
		lv1list = new ArrayList<ShopEvaluateBean>();
		lv2list = new ArrayList<ShopEvaluateBean>();
		lv3list = new ArrayList<ShopEvaluateBean>();
		loadlv1();
		loadlv2();
		loadlv3();

	}
	/**
	 * 
	 */
	private void loadlv3() {
		if (lv3page <= lv3totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("act",UrlPath.ACT_COMMENT);
			ha.put("acttag","list");
			ha.put("store_id", Myapplication.store_id);
			ha.put("uid", Myapplication.sp.getString("uid", ""));
			ha.put("seskey", Myapplication.seskey);
			ha.put("product_id", id);
//			ha.put("star", "1");
			ha.put("page", "" + lv3page);
			ha.put("pagesize", "20");
			Log.i("test", "getComments--->"+Myapplication.getUrl(ha));
			Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								int count=0;
								if (response.getInt("code") == 1) {
									lv3page = lv3page + 1;
									lv3totalpage = response.getInt("totalpage");
									JSONArray ja = response
											.getJSONArray("list");
									for (int i = 0; i < ja.length(); i++) {
										JSONObject jb = ja.getJSONObject(i);
										ShopEvaluateBean sb = new ShopEvaluateBean();
										String getStar=jb.getString("comment_star");
										
										if(getStar.equals("0") || getStar.equals("1"))
										{
										sb.setContent(jb.getString("comment_content"));
										sb.setCreatetime(jb
												.getString("createtime"));
										sb.setStar(jb.getString("comment_star"));
										sb.setUsername(jb.getString("username"));
										count=count+1;
										lv3list.add(sb);
										}
									}
									if (lv3list.size() == 0) {
//										Toast.makeText(ShopEvaluate.this,
//												"暂无差评", 2000).show();
									}
									tv3.setText("差评" + "("
											+ count
											+ ")");
								} else {
									lv3.removeFooterView(footview3);
									return;
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
							lv3.removeFooterView(footview3);
							ad3.notifyDataSetChanged();

						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							lv3.removeFooterView(footview3);

						}
					});
		}
	}
	/**
	 * 
	 */
	private void loadlv2() {
		if (lv2page <= lv2totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("act",UrlPath.ACT_COMMENT);
			ha.put("acttag","list");
			ha.put("store_id", Myapplication.store_id);
			ha.put("uid", Myapplication.sp.getString("uid", ""));
			ha.put("seskey", Myapplication.seskey);
			ha.put("product_id", id);
			ha.put("star", "2");
			ha.put("page", "" + lv2page);
			ha.put("pagesize", "20");
			Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								int count=0;
								if (response.getInt("code") == 1) {
									lv2page = lv2page + 1;
									lv2totalpage = response.getInt("totalpage");
									JSONArray ja = response
											.getJSONArray("list");
									for (int i = 0; i < ja.length(); i++) {
										JSONObject jb = ja.getJSONObject(i);
										ShopEvaluateBean sb = new ShopEvaluateBean();
										String getStar=jb.getString("comment_star");
										if(getStar.equals("3") || getStar.equals("2"))
										{
										sb.setContent(jb.getString("comment_content"));
										sb.setCreatetime(jb
												.getString("createtime"));
										sb.setStar(jb.getString("comment_star"));
										sb.setUsername(jb.getString("username"));
										count=count+1;
										lv2list.add(sb);
										}
									}
									if (lv2list.size() == 0) {
//										Toast.makeText(ShopEvaluate.this,
//												"暂无中评", 2000).show();
									}
									tv2.setText("中评" + "("
											+count
											+ ")");

								} else {
									lv2.removeFooterView(footview2);
									return;
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
							lv2.removeFooterView(footview2);
							ad2.notifyDataSetChanged();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							lv2.removeFooterView(footview2);
						}
					});
		}

	}
	/**
	 * 
	 */
	private void loadlv1() {
		if (lv1page <= lv1totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("act",UrlPath.ACT_COMMENT);
			ha.put("acttag","list");
			ha.put("store_id", Myapplication.store_id);
			ha.put("uid", Myapplication.sp.getString("uid", ""));
			ha.put("seskey", Myapplication.seskey);
			ha.put("product_id", id);
//			ha.put("star", "5");
			ha.put("page", "" + lv1page);
			ha.put("pagesize", "20");
			Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								int count=0;
								if (response.getInt("code") == 1) {
									lv1page = lv1page + 1;
									lv1totalpage = response.getInt("totalpage");
									JSONArray ja = response
											.getJSONArray("list");
									for (int i = 0; i < ja.length(); i++) {
										JSONObject jb = ja.getJSONObject(i);
										ShopEvaluateBean sb = new ShopEvaluateBean();
										String getStar=jb.getString("comment_star");
										Log.i("test", "getStar-->"+getStar);
										if(getStar.equals("5") || getStar.equals("4"))
										{
										sb.setContent(jb.getString("comment_content"));
										sb.setCreatetime(jb
												.getString("createtime"));
										sb.setStar(jb.getString("comment_star"));
										sb.setUsername(jb.getString("username"));
										count=count+1;
										lv1list.add(sb);
										}
									}
									if (lv1list.size() == 0) {
//										Toast.makeText(ShopEvaluate.this,
//												"暂无好评", 2000).show();
									}
									tv1.setText("好评" + "("
											+ count
											+ ")");
								} else {
									lv1.removeFooterView(footview1);
									return;
								}

							} catch (JSONException e) {
								e.printStackTrace();
								Log.i("test", "error-->"+e.getMessage());
							}
							lv1.removeFooterView(footview1);
							ad1.notifyDataSetChanged();

						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							lv1.removeFooterView(footview1);
						}
					});
		}
	}
	/**
	 * 
	 */
	private void initView() {
		footview1 = Myapplication.lf.inflate(R.layout.footview, null);
		footview2 = Myapplication.lf.inflate(R.layout.footview, null);
		footview3 = Myapplication.lf.inflate(R.layout.footview, null);
		back = findViewById(R.id.back);
		ly = (LinearLayout) findViewById(R.id.ly);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		changecolor(tv1, ly, R.drawable.tv_changerno, R.drawable.tv_changer);
		vf = (ViewFlipper) findViewById(R.id.vf);
		lv1 = (ListView) findViewById(R.id.lv1);
		lv2 = (ListView) findViewById(R.id.lv2);
		lv3 = (ListView) findViewById(R.id.lv3);
		if (ad1 == null) {
			ad1 = new Lv1Adapter();
		}
		lv1.addFooterView(footview1);
		lv1.setAdapter(ad1);
		if (ad2 == null) {
			ad2 = new Lv2Adapter();
		}
		lv2.addFooterView(footview2);
		lv2.setAdapter(ad2);
		if (ad3 == null) {
			ad3 = new Lv3Adapter();
		}
		lv3.addFooterView(footview3);
		lv3.setAdapter(ad3);
	}
	/**
	 * 
	 */
	private void initlistener() {
		back.setOnClickListener(this);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		lv3.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lv3lastitem == (lv3page - 1) * 20) {
					if (footview3.isShown()) {
					} else {
						lv3.addFooterView(footview3);
					}
					loadlv3();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lv3lastitem = totalItemCount;

			}
		});
		lv2.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lv2lastitem == (lv2page - 1) * 20) {
					if (footview2.isShown()) {
					} else {
						lv2.addFooterView(footview2);
					}
					loadlv2();
					// moreview.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lv2lastitem = totalItemCount;

			}
		});
		lv1.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lv1lastitem == (lv1page - 1) * 20) {
					if (footview1.isShown()) {
					} else {
						lv1.addFooterView(footview1);
					}
					loadlv1();
					// moreview.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lv1lastitem = totalItemCount;

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			ShopEvaluate.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;
		case R.id.tv1:
			vf.setDisplayedChild(0);
			if (tv1.getText().toString().equals("好评")) {
				loadlv1();
			}
			changecolor(v, ly, R.drawable.tv_changerno, R.drawable.tv_changer);
			break;

		case R.id.tv2:
			vf.setDisplayedChild(1);
			if (tv2.getText().toString().equals("中评")) {
				loadlv2();
			}
			changecolor(v, ly, R.drawable.tv_changerno, R.drawable.tv_changer);
			break;

		case R.id.tv3:
			vf.setDisplayedChild(2);
			if (tv3.getText().toString().equals("差评")) {
				loadlv3();
			}
			changecolor(v, ly, R.drawable.tv_changerno, R.drawable.tv_changer);
			break;

		default:
			break;
		}

	}
	/**
	 * 
	 */
	private class Lv1Adapter extends BaseAdapter {

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
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ShopEvaluateBean sb = lv1list.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(
						R.layout.shopevaluate_lv1item, null);
			}
			TextView name = (TextView) convertView.findViewById(R.id.name);
			ImageView xing = (ImageView) convertView
					.findViewById(R.id.xingxing);
			TextView createtime = (TextView) convertView
					.findViewById(R.id.createtime);
			TextView content = (TextView) convertView
					.findViewById(R.id.content);
			name.setText(sb.getUsername() + ":");
			createtime.setText(sb.getCreatetime());
			content.setText(sb.getContent());
			switch (Integer.parseInt(sb.getStar())) {
			case 1:
				xing.setBackgroundResource(R.drawable.xing1);
				break;
			case 2:
				xing.setBackgroundResource(R.drawable.xing2);
				break;
			case 3:
				xing.setBackgroundResource(R.drawable.xing3);
				break;
			case 4:
				xing.setBackgroundResource(R.drawable.xing4);
				break;
			case 5:
				xing.setBackgroundResource(R.drawable.xing5);
				break;

			default:
				break;
			}
			return convertView;
		}
	}
	/**
	 * 
	 */
	private class Lv2Adapter extends BaseAdapter {

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
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ShopEvaluateBean sb = lv2list.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(
						R.layout.shopevaluate_lv1item, null);
			}
			TextView name = (TextView) convertView.findViewById(R.id.name);
			ImageView xing = (ImageView) convertView
					.findViewById(R.id.xingxing);
			TextView createtime = (TextView) convertView
					.findViewById(R.id.createtime);
			TextView content = (TextView) convertView
					.findViewById(R.id.content);
			name.setText(sb.getUsername() + ":");
			createtime.setText(sb.getCreatetime());
			content.setText(sb.getContent());
			switch (Integer.parseInt(sb.getStar())) {
			case 1:
				xing.setBackgroundResource(R.drawable.xing1);
				break;
			case 2:
				xing.setBackgroundResource(R.drawable.xing2);
				break;
			case 3:
				xing.setBackgroundResource(R.drawable.xing3);
				break;
			case 4:
				xing.setBackgroundResource(R.drawable.xing4);
				break;
			case 5:
				xing.setBackgroundResource(R.drawable.xing5);
				break;

			default:
				break;
			}
			return convertView;
		}

	}
	/**
	 * 
	 */
	private class Lv3Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (lv3list == null) {
				return 0;
			} else {
				return lv3list.size();
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
			ShopEvaluateBean sb = lv3list.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(
						R.layout.shopevaluate_lv1item, null);
			}
			TextView name = (TextView) convertView.findViewById(R.id.name);
			ImageView xing = (ImageView) convertView
					.findViewById(R.id.xingxing);
			TextView createtime = (TextView) convertView
					.findViewById(R.id.createtime);
			TextView content = (TextView) convertView
					.findViewById(R.id.content);
			name.setText(sb.getUsername() + ":");
			createtime.setText(sb.getCreatetime());
			content.setText(sb.getContent());
			switch (Integer.parseInt(sb.getStar())) {
			case 1:
				xing.setBackgroundResource(R.drawable.xing1);
				break;
			case 2:
				xing.setBackgroundResource(R.drawable.xing2);
				break;
			case 3:
				xing.setBackgroundResource(R.drawable.xing3);
				break;
			case 4:
				xing.setBackgroundResource(R.drawable.xing4);
				break;
			case 5:
				xing.setBackgroundResource(R.drawable.xing5);
				break;

			default:
				break;
			}
			return convertView;
		}

	}
	/**
	 * 
	 */
	private void changecolor(View v, ViewGroup vg, int defaul, int change) { // 背景切换
		for (int i = 0; i < vg.getChildCount(); i++) {
			TextView vv = (TextView) vg.getChildAt(i);
			vv.setBackgroundResource(defaul);
			vv.setTextColor(Color.parseColor("#333333"));
		}
		TextView tv = (TextView) v;
		tv.setBackgroundResource(change);
		tv.setTextColor(Color.parseColor("#62D036"));
	}
}
