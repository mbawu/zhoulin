package xinyuan.cn.zhoulin;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.address.BuildAddress;
import xinyuan.cn.zhoulin.model.AddressBean;
import xinyuan.cn.zhoulin.model.Product;
import xinyuan.cn.zhoulin.model.MyDilog;
import xinyuan.cn.zhoulin.pay.Keys;
import xinyuan.cn.zhoulin.pay.PayMethod;
import xinyuan.cn.zhoulin.pay.Result;
import xinyuan.cn.zhoulin.pay.Rsa;
import xinyuan.cn.zhoulin.tools.OrderMake;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

public class CommitOrderActivity extends Activity implements OnClickListener {
	private Myad myad;
	private Product cb;
	private ArrayList<AddressBean> al;
	private ArrayList<Product> li;
	private TextView buildaddress;
	private ListView lv;
	private EditText note;
	private NetworkImageView shopimage;
	private TextView shopname;
	private TextView shopnum;
	private TextView shopprice;
	private TextView price;
	private TextView freightprice;
	private TextView totalpriceTxt;
	private String totalprice;
	private Button pay;
	private Button back;
	private String type = "-1";
	private LinearLayout pay1;
	private LinearLayout pay2;
	private ImageView payim1;
	private ImageView payim2;
	private String paytype = "1";
	private String subMsg="再逛逛";
	private HashMap<String,String> getPayInfo;
	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.commitorder);
		cb = (Product) getIntent().getExtras().get("cb");
		li = new ArrayList<Product>();
		li.add(cb);
		Myapplication.getInstance().addActivity(this);
		initView();
		initData();
		initlistener();
		getFreight();
	}
	
	private void getFreight()
	{
		Log.i("test", "start");
		Log.i("test", cb.getStore_price() * cb.getNum()+"");
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("act", "freight");
		ha.put("order_price",  ""+cb.getStore_price() * cb.getNum());
		ha.put("sid", Myapplication.store_id);
		Log.i("test", Myapplication.getUrl(ha));
		Myapplication.client.postWithURL("http://api2.xinlingmingdeng.com/order/", ha,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
							String freight=response.getString("freight");
							Log.i("test", "freight->"+freight);
							freightprice.setText("￥"+freight);
							String newTotalPrice=String.valueOf(Double.valueOf(totalprice)+Double.valueOf(freight));
							totalpriceTxt.setText("￥"+newTotalPrice);
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
	private void initData() {
		Myapplication.client.getImageForNetImageView(cb.getProduct_photo(),
				shopimage, R.drawable.ic_launcher);
		price.setText("￥ " + cb.getStore_price() * cb.getNum());
		shopprice.setText("" + cb.getStore_price());
		shopnum.setText("数量 ：  " + cb.getNum());
		shopname.setText(cb.getProduct_name());
		totalpriceTxt.setText("￥ " + cb.getStore_price() * cb.getNum());
		totalprice=""+cb.getStore_price() * cb.getNum();
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("act", UrlPath.ACT_ADDRESS);
		ha.put("acttag", "list");
		ha.put("uid", Myapplication.sp.getString("uid", ""));
		ha.put("username", Myapplication.sp.getString("username", ""));
		ha.put("seskey", Myapplication.seskey);
		ha.put("store_id", Myapplication.store_id);
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								JSONArray ja = response.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									AddressBean ab = new AddressBean();
									ab.setAddress(jb.getString("address"));
									ab.setAddress_id(jb.getString("address_id"));
									ab.setMobile(jb.getString("mobile"));
									ab.setRealname(jb.getString("realname"));
									ab.setCity_id(jb.getString("city_id"));
									ab.setCity_name(jb.getString("city_name"));
									ab.setCounty_id(jb.getString("county_id"));
									ab.setCounty_name(jb
											.getString("county_name"));
									ab.setProvince_id(jb
											.getString("province_id"));
									ab.setProvince_name(jb
											.getString("province_name"));
									al.add(ab);
								}
								new Utility()
										.setListViewHeightBasedOnChildren(lv);
								myad.notifyDataSetChanged();
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
	private void initlistener() {
		buildaddress.setOnClickListener(this);
		pay.setOnClickListener(this);
		back.setOnClickListener(this);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String s = (String) arg1.findViewById(R.id.type).getTag();
				String[] i = s.split(",");
				type = i[0];
				for (int m = 0; m < al.size(); m++) {
					al.get(m).setChecked(false);
				}
				al.get(Integer.parseInt(i[1])).setChecked(true);
				myad.notifyDataSetChanged();
			}
		});
		pay1.setOnClickListener(this);
		pay2.setOnClickListener(this);

	}
	/**
	 * 
	 */
	private void initView() {
		pay1 = (LinearLayout) findViewById(R.id.pay1);
		pay2 = (LinearLayout) findViewById(R.id.pay2);
		payim1 = (ImageView) findViewById(R.id.payim1);
		payim2 = (ImageView) findViewById(R.id.payim2);
		back = (Button) findViewById(R.id.back);
		buildaddress = (TextView) findViewById(R.id.buildaddress);
		lv = (ListView) findViewById(R.id.lv);
		myad = new Myad();
		al = new ArrayList<AddressBean>();
		lv.setAdapter(myad);
		note = (EditText) findViewById(R.id.note);
		shopimage = (NetworkImageView) findViewById(R.id.shopimage);
		shopname = (TextView) findViewById(R.id.shopname);
		shopnum = (TextView) findViewById(R.id.shopnum);
		shopprice = (TextView) findViewById(R.id.shopprice);
		price = (TextView) findViewById(R.id.price);
		freightprice= (TextView) findViewById(R.id.freightprice);
		totalpriceTxt = (TextView) findViewById(R.id.totalprice);
		pay = (Button) findViewById(R.id.pay);
	}

	protected void onPause() {
		super.onPause();
		Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			CommitOrderActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;
		case R.id.buildaddress:
			startActivity(new Intent(CommitOrderActivity.this,
					BuildAddress.class));
			this.overridePendingTransition(R.anim.view_in_from_right,
					R.anim.view_out_to_left);
			break;
		case R.id.pay1:
			paytype = "1";
			payim1.setBackgroundResource(R.drawable.select);
			payim2.setBackgroundResource(R.drawable.selectno);
			break;
		case R.id.pay2:
			paytype = "2";
			payim1.setBackgroundResource(R.drawable.selectno);
			payim2.setBackgroundResource(R.drawable.select);
			break;
		case R.id.pay:
			if(paytype.equals("1"))
				subMsg="去付款";
			if (!type.equals("-1")) {
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("act",UrlPath.ACT_ORDER);
				ha.put("acttag","add");
				ha.put("uid", Myapplication.sp.getString("uid", ""));
				ha.put("seskey", Myapplication.seskey);
				ha.put("order_subject", li.get(0).getProduct_name());
				ha.put("address_id", "" + type);
				ha.put("timeid", "" + 0);
				ha.put("payway", paytype);
				ha.put("note", note.getText().toString());
				ha.put("store_id", Myapplication.store_id);
				Log.i("test", "OrderJSON-->"+new OrderMake().makeOrder(li));
				ha.put("orderlist", new OrderMake().makeOrder(li));
				Log.i("test", "addOrder-->"+Myapplication.getUrl(ha));
				Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
						new Listener<JSONObject>() {
							public void onResponse(JSONObject arg0) {
								try {
									Log.i("test", "arg0->"+arg0.toString());
									switch (arg0.getInt("code")) {
									case 1:
//										JSONArray ja = arg0
//												.getJSONArray("list");
//										Log.i("test", "运行到这里了4！"+arg0.toString());
//										if (ja.length() > 0) {
//											ovv(ja);
//										} else {
										getPayInfo=new HashMap<String, String>();
										getPayInfo.put("oid", arg0.getString("oid"));
										getPayInfo.put("subject", arg0.getString("subject"));
										getPayInfo.put("price", arg0.getString("price"));
										Log.i("test", "backprice->"+arg0.getString("price"));
										getPayInfo.put("hc", arg0.getString("hc"));
										
											ov();
//										}
//										Toast.makeText(
//												CommitOrderActivity.this,
//												arg0.getString("msg").toString(), 2000).show();
										break;
									case 2:
										Toast.makeText(
												CommitOrderActivity.this,
												"提交参数有误", 2000).show();
										break;
									case 4:
										Toast.makeText(
												CommitOrderActivity.this,
												"商品信息有误", 2000).show();
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
							public void onErrorResponse(VolleyError arg0) {
								Log.i("test", "运行到这里了3！"+arg0.getMessage());
//								Toast.makeText(CommitOrderActivity.this,
//										"服务器未响应", 2000).show();

							}
						});
			} else {
				Toast.makeText(CommitOrderActivity.this, "请选择收货地址", 2000)
						.show();
			}
			break;

		default:
			break;
		}

	}
	
	
	/**
	 * 
	 */
	private void ovv(JSONArray ja) {
		final Dialog dialog = new MyDilog(CommitOrderActivity.this);
		dialog.show();
		dialog.getWindow().findViewById(R.id.queding)
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dialog.cancel();
					}
				});

		final LinearLayout ly = (LinearLayout) dialog.getWindow().findViewById(
				R.id.ly);
		ly.removeAllViews();
		for (int i = 0; i < ja.length(); i++) {
			try {
				JSONObject jb = (JSONObject) ja.get(i);
				String id = jb.getString("product_id");
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("act", UrlPath.ACT_PRODUCTDETAIL);
				ha.put("store_id", Myapplication.store_id);
				ha.put("product_id", id);
				Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
						new Listener<JSONObject>() {
							public void onResponse(JSONObject response) {
								try {
									if (response.getInt("code") == 1) {
										LinearLayout li = (LinearLayout) Myapplication.lf
												.inflate(R.layout.dialog_item,
														null);
										NetworkImageView ph = (NetworkImageView) li
												.findViewById(R.id.photo);
										TextView nm = (TextView) li
												.findViewById(R.id.product_name);
										TextView pr = (TextView) li
												.findViewById(R.id.product_price);
										Myapplication.client.getImageForNetImageView(
												response.getString("product_photo"),
												ph, R.drawable.ic_launcher);
										nm.setText(response
												.getString("product_name"));
										pr.setText(""
												+ response
														.getDouble("store_price"));
										ly.addView(li);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
						}, new ErrorListener() {
							public void onErrorResponse(VolleyError error) {

							}
						});

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}
	/**
	 * 
	 */
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK) { // KEYCODE_BACK=4;
			CommitOrderActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}
	/**
	 * 
	 */
	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			if (al == null) {
				return 0;
			} else {
				return al.size();
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
			AddressBean ob = al.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(
						R.layout.payaddress_item, null);
			}
			View type = (View) convertView.findViewById(R.id.type);
			ImageView im = (ImageView) convertView.findViewById(R.id.im);
			if (ob.getChecked()) {
				im.setBackgroundResource(R.drawable.select);
			} else {
				im.setBackgroundResource(R.drawable.selectno);
			}
			TextView realname = (TextView) convertView
					.findViewById(R.id.realname);
			TextView address = (TextView) convertView
					.findViewById(R.id.address);
			TextView mobile = (TextView) convertView.findViewById(R.id.mobile);
			realname.setText(ob.getRealname());
			address.setText(ob.getProvince_name() + ob.getCity_name()
					+ ob.getCounty_name() + ob.getAddress());
			mobile.setText(ob.getMobile());
			type.setTag(ob.getAddress_id() + "," + position);
			return convertView;
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (Myapplication.addressneedflash) {
			type = "-1";
			al.clear();
			initData();
			Myapplication.addressneedflash = false;
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
	public void ov() {
		new AlertDialog.Builder(CommitOrderActivity.this)
				.setMessage("提交订单成功")
				.setPositiveButton("查看订单",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Myapplication.tiaosize = 5;
								Myapplication.tiao = true;
								Myapplication.orderneedflash = true;
								CommitOrderActivity.this.finish();
								CommitOrderActivity.this
										.overridePendingTransition(
												R.anim.view_in_from_left,
												R.anim.view_out_to_right);
							}
						})

						.setNegativeButton(subMsg,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										Myapplication.tiaosize = 0;
										Myapplication.tiao = true;
										if(paytype.equals("1"))//跳转到支付页面
										{
											if(getPayInfo.size()>0)
											{
												Intent intent=new Intent();
												intent.setClass(CommitOrderActivity.this,PayMethod.class);
												intent.putExtra("subject", getPayInfo.get("subject"));
												intent.putExtra("oid", getPayInfo.get("oid"));
												intent.putExtra("price", getPayInfo.get("price"));
												intent.putExtra("hc", getPayInfo.get("hc"));
												startActivity(intent);
											}
											
										}
										else if(paytype.equals("2"))//跳转到再逛逛页面
										{
											CommitOrderActivity.this
													.overridePendingTransition(
															R.anim.view_in_from_left,
															R.anim.view_out_to_right);
										}
										CommitOrderActivity.this.finish();
									}
								}).show();
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);

			switch (msg.what) {
			case RQF_PAY:
			case RQF_LOGIN: {
				Toast.makeText(CommitOrderActivity.this, result.getErrorResult(),
						Toast.LENGTH_SHORT).show();

			}
				break;
			default:
				break;
			}
		};
	};
	
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		return key;
	}
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
