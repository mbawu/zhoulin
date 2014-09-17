package xinyuan.cn.zhoulin.views.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import xinyuan.cn.zhoulin.model.OrderEvaluateBean;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
/**
 * 
 */
public class OrderEvaluate extends Activity implements OnClickListener,
		OnTouchListener {
	private Button back;
	private ListView lv;
	private Myad ad;
	private List<OrderEvaluateBean> li;
	private WindowManager wm;
	private WindowManager.LayoutParams wmParams;
	private String order_code;
	private String product_id;
	private View vv;
	private ImageView im1;
	private ImageView im2;
	private ImageView im3;
	private ImageView im4;
	private ImageView im5;
	private EditText content;
	private Button commit;
	private Button cancel;
	private String evaluation_star = "5";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.orderevaluate);
//		setContentView(R.layout.say);
		initview();
//		initdata();
		initlistener();
	}

	private void initlistener() {
//		Log.i("test", "发表评论："+"运行到这里了");
		back.setOnClickListener(this);
		im1.setOnTouchListener(this);
		im2.setOnTouchListener(this);
		im3.setOnTouchListener(this);
		im4.setOnTouchListener(this);
		im5.setOnTouchListener(this);
		commit.setOnClickListener(this);
		cancel.setOnClickListener(this);

	}
	/**
	 *  获取订单商品評價列表
	 */
	private void initdata() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("act",UrlPath.ACT_COMMENT);
		ha.put("acttag","list");
		ha.put("store_id", Myapplication.store_id);
		ha.put("uid", Myapplication.sp.getString("uid", ""));
//		ha.put("username", Myapplication.sp.getString("username", "")); 
//		ha.put("realname","");
		ha.put("seskey", Myapplication.seskey);
//		ha.put("order_code", order_code);
		ha.put("product_id","223");
		ha.put("page","1");
		ha.put("pagesize","5");
		Log.i("test", "ordershoplist--->"+Myapplication.getUrl(ha));
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								JSONArray ja = response.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									OrderEvaluateBean ob = new OrderEvaluateBean();
									Log.i("test", "评价列表："+jb.toString());
									
									ob.setEvaluation_flag(jb
											.getString("evaluation_flag"));
									ob.setProduct_id(jb.getString("product_id"));
									ob.setProduct_name(jb
											.getString("product_name"));
									ob.setProduct_photo(jb
											.getString("product_photo"));
									ob.setProduct_num(jb
											.getString("product_num"));
									ob.setProduct_price(jb
											.getString("product_price"));
									li.add(ob);
								}
								ad.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {

					}
				});

	}
	/**
	 * 
	 */
	private void initview() {
		vv = Myapplication.lf.inflate(R.layout.say, null);
		im1 = (ImageView) vv.findViewById(R.id.im1);
		im2 = (ImageView) vv.findViewById(R.id.im2);
		im3 = (ImageView) vv.findViewById(R.id.im3);
		im4 = (ImageView) vv.findViewById(R.id.im4);
		im5 = (ImageView) vv.findViewById(R.id.im5);
		content = (EditText) vv.findViewById(R.id.content);
		commit = (Button) vv.findViewById(R.id.commit);
		cancel = (Button) vv.findViewById(R.id.cancel);
		order_code = getIntent().getExtras().getString("id");
		Toast.makeText(OrderEvaluate.this, order_code, 2000).show();
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		wmParams = new WindowManager.LayoutParams();
		wmParams.gravity = Gravity.CENTER;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		back = (Button) findViewById(R.id.back);
		lv = (ListView) findViewById(R.id.lv);
		li = new ArrayList<OrderEvaluateBean>();
		ad = new Myad();
//		lv.setAdapter(ad);
	}
	/**
	 * 
	 */
	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			return li.size();
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
			OrderEvaluateBean oe = li.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(
						R.layout.orderevaluate_item, null);
			}
			NetworkImageView im = (NetworkImageView) convertView
					.findViewById(R.id.im);
			TextView name = (TextView) convertView.findViewById(R.id.name);
//			TextView show = (TextView) convertView.findViewById(R.id.show);
			TextView price = (TextView) convertView.findViewById(R.id.price);
			TextView num = (TextView) convertView.findViewById(R.id.num);

			Myapplication.client.getImageForNetImageView(oe.getProduct_photo(),
					im, R.drawable.ic_launcher);
			name.setText(oe.getProduct_name());
//			show.setTag(oe.getProduct_id());
			price.setText(oe.getProduct_price());
			num.setText(oe.getProduct_num());
			if (oe.getEvaluation_flag().equals("1")) {
//				show.setText("已评论");
			} else if (oe.getEvaluation_flag().equals("0")) {
//				show.setText("未评论");
			} else {
//				show.setText("无状态");
			}
//			show.setOnClickListener(OrderEvaluate.this);

			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.show:
//			TextView bb = (TextView) v;
//			if (bb.getText().equals("已评论")) {
//
//			} else {
//				if (vv.getParent() == null) {
//					clear();
//					product_id = (String) bb.getTag();
//					wm.addView(vv, wmParams);
//				}
//
//			}
//
//			break;
		case R.id.back:
			OrderEvaluate.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;
		case R.id.commit:
			shopsay();
			break;
		case R.id.cancel:
			if (vv.getParent() != null) {
				wm.removeView(vv);
			}
			break;

		default:
			break;
		}

	}
	/**
	 * 发表评价
	 */
	private void shopsay() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("act",UrlPath.ACT_COMMENT);
		ha.put("acttag","add");
		ha.put("store_id", Myapplication.store_id);
		ha.put("uid", Myapplication.sp.getString("uid", ""));
		ha.put("username", Myapplication.sp.getString("username", ""));
		ha.put("realname", "");
		ha.put("seskey", Myapplication.seskey);
//		ha.put("order_code", OrderEvaluate.this.order_code);
		ha.put("orderlist_id", OrderEvaluate.this.order_code);
		ha.put("product_id", OrderEvaluate.this.product_id);
		Toast.makeText(OrderEvaluate.this,
				OrderEvaluate.this.product_id + OrderEvaluate.this.order_code,
				2000).show();
		ha.put("evaluation_star", evaluation_star);
		ha.put("evaluation_content", content.getText().toString());
		Myapplication.client.postWithURL(UrlPath.shopsay, ha,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								Toast.makeText(OrderEvaluate.this, "评论成功", 2000)
										.show();
								clear();
								if (vv.getParent() != null) {
									wm.removeView(vv);
								}
								li.clear();
								initdata();
							} else {
								Toast.makeText(OrderEvaluate.this, "评论失败", 2000)
										.show();
								clear();
								if (vv.getParent() != null) {
									wm.removeView(vv);
								}
							}
						} catch (JSONException e) {
							Toast.makeText(OrderEvaluate.this, "数据异常", 2000)
									.show();
							clear();
							if (vv.getParent() != null) {
								wm.removeView(vv);
							}
							e.printStackTrace();
						}

					}

				}, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(OrderEvaluate.this, "您的网络异常", 2000)
								.show();
						clear();
						if (vv.getParent() != null) {
							wm.removeView(vv);
						}
					}
				});

	}
	/**
	 * 
	 */
	private void clear() {
		evaluation_star = "5";
		im1.setBackgroundResource(R.drawable.xingxing);
		im2.setBackgroundResource(R.drawable.xingxing);
		im3.setBackgroundResource(R.drawable.xingxing);
		im4.setBackgroundResource(R.drawable.xingxing);
		im5.setBackgroundResource(R.drawable.xingxing);
		content.setText("");
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.im1:
			im1.setBackgroundResource(R.drawable.xingxing);
			im2.setBackgroundResource(R.drawable.xingxingan);
			im3.setBackgroundResource(R.drawable.xingxingan);
			im4.setBackgroundResource(R.drawable.xingxingan);
			im5.setBackgroundResource(R.drawable.xingxingan);
			evaluation_star = "1";

			break;
		case R.id.im2:
			im1.setBackgroundResource(R.drawable.xingxing);
			im2.setBackgroundResource(R.drawable.xingxing);
			im3.setBackgroundResource(R.drawable.xingxingan);
			im4.setBackgroundResource(R.drawable.xingxingan);
			im5.setBackgroundResource(R.drawable.xingxingan);
			evaluation_star = "2";
			break;
		case R.id.im3:
			im1.setBackgroundResource(R.drawable.xingxing);
			im2.setBackgroundResource(R.drawable.xingxing);
			im3.setBackgroundResource(R.drawable.xingxing);
			im4.setBackgroundResource(R.drawable.xingxingan);
			im5.setBackgroundResource(R.drawable.xingxingan);
			evaluation_star = "3";
			break;
		case R.id.im4:
			im1.setBackgroundResource(R.drawable.xingxing);
			im2.setBackgroundResource(R.drawable.xingxing);
			im3.setBackgroundResource(R.drawable.xingxing);
			im4.setBackgroundResource(R.drawable.xingxing);
			im5.setBackgroundResource(R.drawable.xingxingan);
			evaluation_star = "4";
			break;
		case R.id.im5:
			im1.setBackgroundResource(R.drawable.xingxing);
			im2.setBackgroundResource(R.drawable.xingxing);
			im3.setBackgroundResource(R.drawable.xingxing);
			im4.setBackgroundResource(R.drawable.xingxing);
			im5.setBackgroundResource(R.drawable.xingxing);
			evaluation_star = "5";
			break;

		default:
			break;
		}
		return false;
	}
}
