package xinyuan.cn.zhoulin;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.mainfragment5size.MainFragment;
import xinyuan.cn.zhoulin.model.Product;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
//
public class Commodity extends Activity implements OnClickListener,
		OnGestureListener { // 公用Activity商品详细信息
	private String id;
	private ViewFlipper vf;
	private TextView shopnum;
	private View back;
	private Product cb;
	private TextView num;
	private RelativeLayout btmachine;
	private RelativeLayout.LayoutParams lo;
	private SlidingDrawer mDrawer;
	private View more;
	private PopupWindow pw;
	private View popuView;
	private TextView sale;
	private TextView search;
	private TextView shop;
	private TextView machin;
	private TextView about;
	private TextView brand_name;
	private TextView product_id;
	private TextView product_name;
	private View say;
	private TextView reference_price;
	private TextView store_price;
	private TextView product_inventory;
	private TextView sale_num;
	private TextView hot;
	private TextView evannums;
	private LinearLayout imglist;
	private android.widget.LinearLayout.LayoutParams ly1;
	private android.widget.LinearLayout.LayoutParams ly2;
	private RelativeLayout slidingly;
	private ImageView sliding_close;
	private NetworkImageView sliding_photo;
	private TextView sliding_name;
	private TextView sliding_price;
	private ImageView sliding_jian;
	private EditText sliding_num;
	private ImageView sliding_jia;
	private View qq;
	private Button add;
	private Button go;
	private Button confirm;
	private Boolean pay = false;
	private GestureDetector gd;
	private int photopage = 1;
	private int photototalpage;
	private String pp;
	private Button dang;
	private String detailUrl;
	private LinearLayout detailBtn;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.commodity);
		Myapplication.getInstance().addActivity(this);
		id = (String) getIntent().getExtras().get("id");
		gd = new GestureDetector(this);
		initView();
		initlistener();
		initpop();
		initData();
		initImagelist();
	}

	private void initImagelist() {
		HashMap<String, String> te = new HashMap<String, String>();
		te.put("store_id", Myapplication.store_id);
		te.put("act", UrlPath.ACT_PRODUCT); 
		te.put("acttag", UrlPath.ACT_PRODUCT_HOT); //人气商品
		te.put("pagesize", Myapplication.productItemNum); //显示多少个商品
//		te.put("store_id", Myapplication.store_id);
//		te.put("page", "1");
//		te.put("per", "20");
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, te,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						String content = response.toString();
						try {
							JSONObject jb = new JSONObject(content);
							if (jb.getInt("code") == 1) {
								JSONArray ja = jb.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jbb = (JSONObject) ja.get(i);
									LinearLayout li = (LinearLayout) Myapplication.lf
											.inflate(R.layout.sale_li, null);
									NetworkImageView nv = (NetworkImageView) li
											.findViewById(R.id.im);
									TextView price = (TextView) li
											.findViewById(R.id.price);
									nv.setTag(jbb.getString("product_id"));
									price.setTextColor(Color
											.parseColor("#f44656"));
									price.setText("￥  "
											+ jbb.getString("store_price"));
									Myapplication.client.getImageForNetImageView(
											jbb.getString("product_photo"), nv,
											R.drawable.ic_launcher);
									imglist.addView(li, ly2);
									nv.setOnClickListener(Commodity.this);
								}
							} else {
								Toast.makeText(Commodity.this, "新品商品获取失败", 2000)
										.show();
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

	private void initpop() {
		pw = new PopupWindow(popuView, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pw.setBackgroundDrawable(new BitmapDrawable());
	}

	
	//商品详细信息
	private void initData() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("store_id", Myapplication.store_id);
		ha.put("act", UrlPath.ACT_PRODUCTDETAIL);
		ha.put("product_id", id);
		Log.i("detail",id);
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						String content = response.toString();
						try {
							JSONObject jb = new JSONObject(content);
							if (jb.getInt("code") == 1) {
								Log.i("test", response.toString());
								JSONObject jc = new JSONObject(jb.getString("pinfo"));
								Log.i("detail",jc.toString());
								Log.i("detail",jc.getString("product_id"));
								product_id.setText(jc.getString("product_id"));
								product_inventory.setText(""
										+ jc.getInt("product_inventory"));
								brand_name.setText(jc.getString("brand_name"));
								product_name.setText(jc
										.getString("product_name"));
								sliding_name.setText(jc
										.getString("product_name"));
								store_price.setText("￥"
										+ jc.getString("store_price"));
								sliding_price.setText("￥"
										+ jc.getString("store_price"));
								sliding_num.setText("1");
								Myapplication.client.getImageForNetImageView(
										jb.getString("defaultImg"),
										sliding_photo, R.drawable.ic_launcher);
								reference_price.setText(""
										+ jc.getDouble("reference_price"));
								store_price.setText(""
										+ jc.getDouble("store_price"));
								product_inventory.setText(""
										+ jc.getInt("product_inventory"));
								sale_num.setText("" + jc.getInt("sale_num"));
								hot.setText("人气值：" + jc.getInt("hot"));
								evannums.setText("评价数" + "("
										+ jb.getInt("evanums") + ")"); //获取评论数		
								JSONArray ja = (JSONArray) jb.get("imglist");
								for (int i = 0; i < ja.length(); i++) {
									String m = ((JSONObject) ja.get(i)).getString("attachments_path");
									LinearLayout ll = (LinearLayout) Myapplication.lf
											.inflate(R.layout.shopphoto, null);
									NetworkImageView l = (NetworkImageView) ll
											.findViewById(R.id.product_photo);
									Myapplication.client
											.getImageForNetImageView(m, l,
													R.drawable.ic_launcher);
									vf.addView(ll, ly1);
								}
								photototalpage = ja.length();
								shopnum.setText("" + photopage + "/"
										+ photototalpage);
								cb = new Product();
								cb.setProduct_id(jc.getString("product_id"));
								cb.setProduct_name(jc.getString("product_name"));
								cb.setProduct_photo(jb
										.getString("defaultImg"));
								cb.setStore_price(jc.getDouble("store_price"));
								//获取商品详情链接
								JSONObject detailObject=response.getJSONObject("pinfo");
								
								detailUrl=detailObject.getString("content_url");
								Log.i("test", detailUrl);
							}
						} catch (JSONException e) {
							Toast.makeText(Commodity.this, "数据解析异常", 1).show();
							Log.i("detail",e.toString());
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(Commodity.this, "您的网络异常", 1).show();
					}
				});
	}

	private void initView() {
		if (ly1 == null) {
			ly1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
		}
		if (ly2 == null) {
			ly2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			ly2.setMargins(7, 3, 0, 3);
		}
		detailBtn=(LinearLayout) findViewById(R.id.product_content);
		detailBtn.setOnClickListener(this);
		dang = (Button) findViewById(R.id.dang);
		shopnum = (TextView) findViewById(R.id.shopnum);
		vf = (ViewFlipper) findViewById(R.id.vf);
		qq = findViewById(R.id.qq);
		more = findViewById(R.id.more);
		popuView = Myapplication.lf.inflate(R.layout.shoppopupwindow, null);
		sale = (TextView) popuView.findViewById(R.id.sale);
		search = (TextView) popuView.findViewById(R.id.search);
		shop = (TextView) popuView.findViewById(R.id.shop);
		machin = (TextView) popuView.findViewById(R.id.machin);
		about = (TextView) popuView.findViewById(R.id.about);
		btmachine = (RelativeLayout) findViewById(R.id.machine);
		num = (TextView) findViewById(R.id.num);
		back = findViewById(R.id.back);
		mDrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);
		product_inventory = (TextView) findViewById(R.id.product_inventory);
		product_name = (TextView) findViewById(R.id.product_name);
		reference_price = (TextView) findViewById(R.id.reference_price);
		store_price = (TextView) findViewById(R.id.store_price);
		sale_num = (TextView) findViewById(R.id.sale_num);
		hot = (TextView) findViewById(R.id.hot);
		evannums = (TextView) findViewById(R.id.evanums);
		imglist = (LinearLayout) findViewById(R.id.imglist);
		say = findViewById(R.id.say);
		brand_name = (TextView) findViewById(R.id.brand_name);
		product_id = (TextView) findViewById(R.id.product_id);
		add = (Button) findViewById(R.id.add);
		go = (Button) findViewById(R.id.go);
		confirm = (Button) findViewById(R.id.confirm);
		slidingly = (RelativeLayout) findViewById(R.id.slidingly);
		sliding_close = (ImageView) findViewById(R.id.sliding_close);
		sliding_photo = (NetworkImageView) findViewById(R.id.sliding_photo);
		sliding_name = (TextView) findViewById(R.id.sliding_name);
		sliding_price = (TextView) findViewById(R.id.sliding_price);
		sliding_jian = (ImageView) findViewById(R.id.sliding_jian);
		sliding_num = (EditText) findViewById(R.id.sliding_num);
		sliding_jia = (ImageView) findViewById(R.id.sliding_jia);
	}

	private void initlistener() {
		btmachine.setOnClickListener(this);
		back.setOnClickListener(this);
		more.setOnClickListener(this);
		sale.setOnClickListener(this);
		search.setOnClickListener(this);
		shop.setOnClickListener(this);
		machin.setOnClickListener(this);
		about.setOnClickListener(this);
		say.setOnClickListener(this);
		add.setOnClickListener(this);
		go.setOnClickListener(this);
		confirm.setOnClickListener(this);
		sliding_close.setOnClickListener(this);
		sliding_jia.setOnClickListener(this);
		sliding_jian.setOnClickListener(this);
		qq.setOnClickListener(this);
		vf.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gd.onTouchEvent(event);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			Commodity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;
		case R.id.qq: //点击商品详情拨打客服电话
			new AlertDialog.Builder(this)
			.setMessage("\t\t确定拨打客服电话\n\t\t\t400-6102-106")
		     .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
		  
		         @Override 
		         public void onClick(DialogInterface dialog, int which) { 
		         // 点击“确认”后的操作 
//		         MainFragmentActivity.this.finish(); 
		        	 Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"4006102106"));
		             Commodity.this.startActivity(intent);//内部类
		         } 
		     }) 
		     .setNegativeButton("返回", new DialogInterface.OnClickListener() { 
		  
		         @Override 
		         public void onClick(DialogInterface dialog, int which) { 
		         // 点击“返回”后的操作,这里不设置没有任何操作 
		         } 
		     }).show(); 

			break;
		case R.id.machine:
			Myapplication.tiao = true;
			Myapplication.tiaosize = 4;
			Commodity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;

		case R.id.sliding_close:
			mDrawer.toggle();
			dang.setVisibility(View.GONE);
			mDrawer.unlock();
			slidingly.setVisibility(View.GONE);
			break;
		case R.id.more:
			pw.showAsDropDown(v);
			pw.setFocusable(true);
			pw.setOutsideTouchable(true);
			pw.update();
			break;
		case R.id.sale:
			Myapplication.tiao = true;
			Myapplication.tiaosize = 1;
			Commodity.this.finish();
			break;
		case R.id.search:
			Myapplication.tiao = true;
			Myapplication.tiaosize = 2;
			Commodity.this.finish();
			break;
		case R.id.shop:
			Myapplication.tiao = true;
			Myapplication.tiaosize = 3;
			Commodity.this.finish();
			break;
		case R.id.machin:
			Myapplication.tiao = true;
			Myapplication.tiaosize = 4;
			Commodity.this.finish();
			break;
		case R.id.about:
			Myapplication.tiao = true;
			Myapplication.tiaosize = 5;
			Commodity.this.finish();
			break;
		case R.id.say:
			Intent in = new Intent(Commodity.this, ShopEvaluate.class);
			in.putExtra("id", id);
			startActivity(in);
			this.overridePendingTransition(R.anim.view_in_from_right,
					R.anim.view_out_to_left);
			break;
		case R.id.go:
			mDrawer.toggle();
			slidingly.setVisibility(View.VISIBLE);
			dang.setVisibility(View.VISIBLE);
			mDrawer.lock();
			pay = true;
			break;
		case R.id.add:
			mDrawer.toggle();
			slidingly.setVisibility(View.VISIBLE);
			dang.setVisibility(View.VISIBLE);
			mDrawer.lock();
			pay = false;
			break;
		case R.id.confirm:
			if (Myapplication.login) {
				if (!pay) {
					Myapplication.machineCachetool.add(cb);
					Toast.makeText(Commodity.this, "成功加入购物车！", 2000).show();
					Myapplication.machine_refresh = true;
					num.setText("" + Myapplication.machineCachelist.size());
					mDrawer.toggle();
					mDrawer.unlock();
					slidingly.setVisibility(View.GONE);
					dang.setVisibility(View.GONE);
				} else {
					mDrawer.toggle();
					mDrawer.unlock();
					slidingly.setVisibility(View.GONE);
					dang.setVisibility(View.GONE);
					Intent i = new Intent(Commodity.this,
							CommitOrderActivity.class);
					i.putExtra("cb", cb);
					startActivity(i);
					this.overridePendingTransition(R.anim.view_in_from_right,
							R.anim.view_out_to_left);
				}
			} else {
				startActivity(new Intent(Commodity.this, LoginActivity.class));
				this.overridePendingTransition(R.anim.view_in_from_right,
						R.anim.view_out_to_left);
			}
			break;
		case R.id.sliding_jia:
			int nu = Integer.parseInt(sliding_num.getText().toString());
			sliding_num.setText("" + (nu + 1));
			cb.setNum(nu + 1);
			break;
		case R.id.sliding_jian:
			int n = Integer.parseInt(sliding_num.getText().toString());
			if (n == 1) {

			} else {
				sliding_num.setText("" + (n - 1));
				cb.setNum(n - 1);
			}
			break;
		case R.id.product_content://商品详情链接跳转页面
			Intent intent=new Intent();
			intent.setClass(this, ProductDetailWebView.class);
			if(detailUrl==null)
			{
				Toast.makeText(this, "该商品暂无详情", 2000).show();
				return;
			}
			intent.putExtra("url", detailUrl);
			startActivity(intent);
			break;
		default:

			Intent i = new Intent(Commodity.this, Commodity.class);
			String id = (String) v.getTag();
			i.putExtra("id", id);
			startActivity(i);
			this.overridePendingTransition(R.anim.view_in_from_right,
					R.anim.view_out_to_left);

			break;
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (Myapplication.tiao) {
			Commodity.this.finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
//		num.setText("" + Myapplication.machineCachelist.size());
	}

	public void tuichu() {
		new AlertDialog.Builder(Commodity.this)
				.setMessage("商品已经加入购物车")
				.setTitle("添加成功")
				.setPositiveButton("去购物车",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Myapplication.tiao = true;
								Commodity.this.finish();
							}
						})
				.setNegativeButton("再逛逛",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Commodity.this.finish();
							}
						}).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() > e2.getX()) {
			if (photopage < photototalpage) {
				vf.setInAnimation(Commodity.this, R.anim.view_in_from_right);
				vf.setOutAnimation(Commodity.this, R.anim.view_out_to_left);
				vf.showNext();
				photopage = photopage + 1;
				shopnum.setText("" + photopage + "/" + photototalpage);
			}
		} else {
			if (photopage == 1) {

			} else {
				vf.setInAnimation(Commodity.this, R.anim.view_in_from_left);
				vf.setOutAnimation(Commodity.this, R.anim.view_out_to_right);
				vf.showPrevious();
				photopage = photopage - 1;
				shopnum.setText("" + photopage + "/" + photototalpage);
			}
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

}
