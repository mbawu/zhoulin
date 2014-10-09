package xinyuan.cn.zhoulin.views.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.MainActivity;
import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import xinyuan.cn.zhoulin.mainfragment5size.MachineFragment;
import xinyuan.cn.zhoulin.model.OrderBean;
import xinyuan.cn.zhoulin.model.OrderDetailsBean;
import xinyuan.cn.zhoulin.model.Product;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
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
public class OrderDetails extends Activity implements OnClickListener {
	public static final String INTENT_STEP1 = "xinyuan.cn.zhoulin.views.order.OrderDetails";  
	private static final int STEP1 = 1;
	private TextView order_code;
	private TextView flag;
	private TextView createtime;
	private TextView realname;
	private TextView mobile;
	private TextView address;
	private TextView releasetime;
	private TextView express_enname;
	private TextView express_code;
	private TextView note;
	private TextView freight;
	private TextView toprice;
	private ListView lv;
	private List<OrderDetailsBean> li;
	private Myad ad;
	private View back;
	private TextView totalprice;
	private Button bt;
	private OrderBean ob;
	private TextView payway;
	private HashMap<String,String> statu;
	private ArrayList<Product> products;
	private boolean alreadyComment=false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.orderdetails);
		inintView();
		initData();
		initlistener();
	}

	private void initlistener() {
		back.setOnClickListener(OrderDetails.this);
//		if(!alreadyComment) //change
		bt.setOnClickListener(this);
	}
	/**
	 * 
	 */
	private void initData() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("act",UrlPath.ACT_ORDER);
		ha.put("acttag","detail");
		ha.put("uid", Myapplication.sp.getString("uid", ""));
		ha.put("username", Myapplication.sp.getString("username", ""));
		ha.put("seskey", Myapplication.seskey);
		ha.put("order_id", ob.getOrder_id());
		Log.i("test", "order_code--->"+ob.getOrder_code());
		ha.put("order_code", ob.getOrder_code());
		ha.put("store_id", Myapplication.store_id);
		Log.i("test", "orderList--->"+Myapplication.getUrl(ha));
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							int code = response.getInt("code");
							
							switch (code) {
							case 1:
								if (li == null) {
									li = new ArrayList<OrderDetailsBean>();
								}
								order_code.setText(response
										.getString("order_code"));
								//封装需要到评价订单的页面取出来的产品的信息
								products=new ArrayList<Product>();
								int flagComment=Integer.valueOf(response.getString("comflag"));
								Log.i("test", "flagComment-->"+flagComment);
								if(flagComment==1)
									alreadyComment=true;
								else
									alreadyComment=false;
								JSONArray productList=response.getJSONArray("list");
								for (int i = 0; i < productList.length(); i++) {
									JSONObject jsonProduct = productList.getJSONObject(i);
									Product product=new Product();
									product.setProduct_name(jsonProduct.getString("product_name"));
									product.setProduct_photo(jsonProduct.getString("product_photo"));
									product.setStore_price(Double.valueOf(jsonProduct.getString("store_price")));
									product.setOrderlist_id(jsonProduct.getString("orderlist_id"));
									product.setProduct_id(jsonProduct.getString("product_id"));
									products.add(product);
								}
//								Log.i("test", "ob.getOrder_code()-->"+ob.getOrder_code());
//								Log.i("test", "statu-->"+statu.get(ob.getOrder_code()));
								flag.setText(statu.get(ob.getOrder_code()));
								Log.i("test", "getOrder_flag-->"+ob.getOrder_flag());
//								switch (Integer.parseInt(ob.getOrder_flag())) {
//								case 0:
//									Log.i("test", "进入switch！"+response.get("flag"));
//									if (response.get("flag").equals("1")) {
//										flag.setText("已下单");
//									} else if (response.get("flag").equals("2")) {
//										Log.i("test", "判断flag为2");
//										flag.setText("已取消");
//										Log.i("test", "设置文字完毕");
//									}
//									break;
//								case 1:
//									 if (ob.getFlag().equals("2")) {
//										flag.setText("已取消");
//									} 
//									else if (ob.getFlag().equals("1")&&ob.getIspay().equals("0")) {
//										flag.setText("待付款");
//									} 
//									else if (ob.getFlag().equals("1")&&ob.getIspay().equals("1")) {
//										flag.setText("已付款");
//									} 
//									break;
//								case 2:
//									flag.setText("派送中");
//									break;
//								case 3:
//									if (response.get("flag").equals("4")) {
//										flag.setText("货物拒收");
//									} else {
//										flag.setText("交易完成");
//									}
//
//									break;
//								default:
//									break;
//								}
								if (response.getString("payway").equals("1")) {
									Log.i("test", "if1");
									payway.setText("在线支付（支付宝）");
								} else {
									Log.i("test", "else");
									payway.setText("货到付款（仅限北京市五环内）");
								}
								createtime.setText(response
										.getString("createtime"));
								realname.setText(response.getString("realname"));
								mobile.setText(response.getString("mobile"));
								address.setText(response
										.getString("address"));
								note.setText(response.getString("note"));
								releasetime.setText(ob.getReleasetime());
								express_code.setText(ob.getExpress_code());
								express_enname.setText(ob.getExpress_enname());
								totalprice.setText(ob.getAllprice());
								toprice.setText(ob.getPrice());
//								Log.i("test", "flag.getText()-->"+flag.getText().toString());
								if (ob.getOrder_flag().equals("5")) {
									bt.setVisibility(View.VISIBLE);
									if(alreadyComment)
										bt.setText("双方已评");
									else
										bt.setText("评价/晒单");
								} 
								else if(ob.getOrder_flag().equals("3"))
								{
									bt.setVisibility(View.VISIBLE);
									bt.setText("确认收货");
								}
								else
								{
									bt.setVisibility(View.GONE);
								}
////								test的时候用
//								//need
//								bt.setVisibility(View.VISIBLE);
//								if(!alreadyComment)
//									bt.setText("评价/晒单");
//								else
//									bt.setText("双方已评");
//								bt.setText("评价/晒单"); 
								JSONArray ja = response.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									OrderDetailsBean ob = new OrderDetailsBean();
									ob.setOrder_id(jb.getString("orderlist_id"));
									ob.setProduct_name(jb
											.getString("product_name"));
									ob.setProduct_num(jb
											.getString("product_num"));
									ob.setProduct_photo(jb
											.getString("product_photo"));
									ob.setProduct_price(Double.parseDouble(jb
											.getString("store_price")));
									li.add(ob);
								}
								ad.notifyDataSetChanged();
								new Utility()
										.setListViewHeightBasedOnChildren(lv);
								break;
							case 2:
								Toast.makeText(OrderDetails.this, "您提交的参数有误",
										2000).show();
								break;
							case 3:
								Toast.makeText(OrderDetails.this, "身份认证出错",
										2000).show();
								break;
							case 4:
								Toast.makeText(OrderDetails.this,
										"此订单数据异常，有可能不是您的订单", 2000).show();
								break;

							default:
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(OrderDetails.this, "获取详细信息失败", 2000)
								.show();
					}
				});

	}
	/**
	 * 
	 */
	private void inintView() {
		freight= (TextView) findViewById(R.id.freight);
		
		payway = (TextView) findViewById(R.id.payway);
		toprice = (TextView) findViewById(R.id.toprice);
		ob = (OrderBean) getIntent().getExtras().get("ob");
		freight.setText("￥"+ob.getFreight());
		statu=(HashMap<String, String>) getIntent().getExtras().get("statu");
		totalprice = (TextView) findViewById(R.id.totalprice);
		back = findViewById(R.id.back);
		lv = (ListView) findViewById(R.id.lv);
		ad = new Myad();
		lv.setAdapter(ad);
		bt = (Button) findViewById(R.id.bt);
		order_code = (TextView) findViewById(R.id.order_code);
		flag = (TextView) findViewById(R.id.flag);
		createtime = (TextView) findViewById(R.id.createtime);
		realname = (TextView) findViewById(R.id.realname);
		mobile = (TextView) findViewById(R.id.mobile);
		address = (TextView) findViewById(R.id.address);
		note = (TextView) findViewById(R.id.note);
		releasetime = (TextView) findViewById(R.id.releasetime);
		express_code = (TextView) findViewById(R.id.express_code);
		express_enname = (TextView) findViewById(R.id.express_enname);
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	/**
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
			OrderDetailsBean ob = li.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(
						R.layout.orderdetails_it, null);
			}
			NetworkImageView nw = (NetworkImageView) convertView
					.findViewById(R.id.im);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView num = (TextView) convertView.findViewById(R.id.num);
			TextView price = (TextView) convertView.findViewById(R.id.price);
			name.setText(ob.getProduct_name());
			num.setText(ob.getProduct_num() + "份    ");
			price.setText("￥：" + ob.getProduct_price());
			Myapplication.client.getImageForNetImageView(ob.getProduct_photo(),
					nw, R.drawable.ic_launcher);
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;
		case R.id.bt:
//			if(alreadyComment) //change
//				break;
			Button bb = (Button) v;
			if (bb.getText().toString().equals("评价/晒单")) {
//				Intent in = new Intent(OrderDetails.this, OrderEvaluate.class);
//				in.putExtra("id", ob.getOrder_code());
				Intent in = new Intent(OrderDetails.this, OrderProductEvaluate.class);
//				in.putExtra("Order_code", ob.getOrder_code());
//				in.putExtra("Order_id", ob.getOrder_id());
				Log.i("test", "传递前--->"+products.toString());
				in.putExtra("products",products);
				Myapplication.getInstance().addActivity(this);
				
				startActivityForResult(in, STEP1);
				this.overridePendingTransition(R.anim.view_in_from_right,
						R.anim.view_out_to_left);
			} 
			else if(bb.getText().toString().equals("双方已评"))
			{
				//noting
			}
			else {
				queren();

			}

			break;
		}
	}
	
	//返回评论结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("test", "comment-->"+data.getStringExtra("comment"));
		if(data.getStringExtra("comment").equals("suc"))
		{
			Toast.makeText(this, "评价成功！", 2000).show();
			bt.setText("双方已评");
		}
		else if(data.getStringExtra("comment").equals("fail"))
		{
			if(data.getStringExtra("orderlist_id").equals("0"))
			{
				Toast.makeText(this, "评价失败！", 2000).show();
				bt.setText("评价/晒单");
			}
			else
			{
				Toast.makeText(this, "评价成功！", 2000).show();
				bt.setText("双方已评");
			}
		}
		
	}
	/**
	 * 
	 */
	public class Utility {
		public void setListViewHeightBasedOnChildren(ListView listView) {
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null) {
				return;
			}
			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}

			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
		}
	}
	/**
	 * 
	 */
	public void queren() {
		new AlertDialog.Builder(OrderDetails.this)
				.setMessage("确认收到商品？ 温馨提示：请慎重操作！！！")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						HashMap<String, String> h = new HashMap<String, String>();
						h.put("act",UrlPath.ACT_ORDER);
						h.put("acttag","receipt");
						h.put("uid", Myapplication.sp.getString("uid", ""));
						h.put("username",
								Myapplication.sp.getString("username", ""));
						h.put("seskey", Myapplication.seskey);
						h.put("store_id", Myapplication.store_id);
						h.put("order_id", ob.getOrder_id());
						Myapplication.client.postWithURL(UrlPath.SERVER_URL, h,
								new Listener<JSONObject>() {
									@Override
									public void onResponse(JSONObject response) {

										try {
											int code = response.getInt("code");
											switch (code) {
											case 1:
												Toast.makeText(
														OrderDetails.this,
														"提交成功", 2000).show();
												OrderDetails.this.finish();
//												flag.setText("交易完成");
//												bt.setText("评价/晒单");
												break;

											default:
												Toast.makeText(
														OrderDetails.this,
														"提交失败", 2000).show();
												break;
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}

									}

								}, new ErrorListener() {

									@Override
									public void onErrorResponse(
											VolleyError error) {
										Toast.makeText(OrderDetails.this,
												"网络异常", 2000).show();
									}
								});

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}
}
