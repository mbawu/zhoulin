package xinyuan.cn.zhoulin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.address.BuildAddress;
import xinyuan.cn.zhoulin.model.AddressBean;
import xinyuan.cn.zhoulin.model.Product;
import xinyuan.cn.zhoulin.model.MyDilog;
import xinyuan.cn.zhoulin.tools.OrderMake;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

/**
 * 
 */
public class MachineCommitOrderActivity extends Activity implements
		OnClickListener {
	private Myad myad;
	private Myshopad shopad;
	private ArrayList<AddressBean> al;
	private ArrayList<Product> li;
	private TextView buildaddress;
	private ListView lv;
	private ListView shoplv;
	private EditText note;
	private TextView price;
	private TextView freightprice;
	private TextView totalprice;
	private Button pay;
	private Button back;
	private String type = "-1";
	private int m;
	private LinearLayout pay1;
	private LinearLayout pay2;
	private ImageView payim1;
	private ImageView payim2;
	private String paytype = "0";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.machinecommitorder);
		li = (ArrayList<Product>) getIntent().getExtras().get("list");
		for (int i = 0; i < li.size(); i++) {
			m = m + (int) (li.get(i).getNum() * li.get(i).getStore_price());
		}
		Myapplication.getInstance().addActivity(this);
		initView();
		initData();
		initlistener();
		getFreight();
	}

	
	
	/**
	 * 
	 */
	private void initData() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("act", UrlPath.ACT_ADDRESS);
		ha.put("acttag", "list");
		ha.put("uid", Myapplication.sp.getString("uid", ""));
		ha.put("username", Myapplication.sp.getString("username", ""));
		ha.put("seskey", Myapplication.seskey);
		ha.put("store_id", Myapplication.store_id);
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {

					@Override
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

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

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

	private void getFreight()
	{
		Log.i("test", "start");
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("act", "freight");
		ha.put("order_price",  ""+m);
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
							String newTotalPrice=String.valueOf(Double.valueOf(m)+Double.valueOf(freight));
							totalprice.setText("￥"+newTotalPrice);
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
		shoplv = (ListView) findViewById(R.id.shoplv);
		shopad = new Myshopad();
		shoplv.setAdapter(shopad);
		new Utility().setListViewHeightBasedOnChildren(shoplv);
		note = (EditText) findViewById(R.id.note);
		price = (TextView) findViewById(R.id.price);
		totalprice = (TextView) findViewById(R.id.totalprice);
		pay = (Button) findViewById(R.id.pay);
		freightprice= (TextView) findViewById(R.id.freightprice);
		price.setText("￥ " + m);
		totalprice.setText("￥ " + m);
	}

	/**
	 * 
	 */
	protected void onPause() {
		super.onPause();
		Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay1:
			paytype = "0";
			payim1.setBackgroundResource(R.drawable.select);
			payim2.setBackgroundResource(R.drawable.selectno);
			break;
		case R.id.pay2:
			paytype = "1";
			payim1.setBackgroundResource(R.drawable.selectno);
			payim2.setBackgroundResource(R.drawable.select);
			break;
		case R.id.back:
			MachineCommitOrderActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;
		case R.id.buildaddress:
			startActivity(new Intent(MachineCommitOrderActivity.this,
					BuildAddress.class));
			this.overridePendingTransition(R.anim.view_in_from_right,
					R.anim.view_out_to_left);
			break;
		case R.id.pay:
			if (!type.equals("-1")) {
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("act",UrlPath.ACT_ORDER);
				ha.put("acttag","add");
				ha.put("uid", Myapplication.sp.getString("uid", ""));
				ha.put("seskey", Myapplication.seskey);
				Date date=new Date();
				ha.put("order_subject", "[订单]"+date.getTime());
				ha.put("address_id", "" + type);
				ha.put("timeid", "" + 0);
				ha.put("payway", paytype);
				ha.put("note", note.getText().toString());
				ha.put("store_id", Myapplication.store_id);
				ha.put("orderlist", new OrderMake().makeOrder(li));
//				ha.put("act",UrlPath.ACT_ORDER);
//				ha.put("acttag","add");
//				Log.i("test", "运行到这里了2！");
//				ha.put("uid", Myapplication.sp.getString("uid", ""));
//				Log.i("test", "运行到这里了3！");
////				ha.put("username", Myapplication.sp.getString("username", ""));
//				ha.put("seskey", Myapplication.seskey);
//				ha.put("order_subject", "android");
//				ha.put("address_id", "" + type);
//				ha.put("payway", paytype);
//				ha.put("note", note.getText().toString());
//				ha.put("store_id", Myapplication.store_id);
//				ha.put("orderlist", new OrderMake().makeOrder(li));
				//获取用户订单数据
				Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject arg0) {
								try {
									switch (arg0.getInt("code")) {
									case 1:
//										JSONArray ja = arg0
//												.getJSONArray("list");
//										if (ja.length() > 0) {
//											ovv(ja);
//											Myapplication.machine_refresh = true;
//											Myapplication.orderneedflash = true;
//											Myapplication.machineCachetool
//													.refresh();
//											try {
//												Myapplication.machineCachetool
//														.saveCache(Myapplication.machineCachelist);
//											} catch (IOException e) {
//												e.printStackTrace();
//											}
//										} else {
											Toast.makeText(
													MachineCommitOrderActivity.this,
													"提交成功", 2000).show();
											Myapplication.machine_refresh = true;
											Myapplication.orderneedflash = true;
											Myapplication.machineCachetool
													.refresh();
											try {
												Myapplication.machineCachetool
														.saveCache(Myapplication.machineCachelist);
											} catch (IOException e) {
												e.printStackTrace();
											}
											MachineCommitOrderActivity.this
													.finish();
											MachineCommitOrderActivity.this
													.overridePendingTransition(
															R.anim.view_in_from_left,
															R.anim.view_out_to_right);
//										}

										break;
									case 2:
										Toast.makeText(
												MachineCommitOrderActivity.this,
												"提交参数有误", 2000).show();
										break;
									case 4:
										Toast.makeText(
												MachineCommitOrderActivity.this,
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
								Toast.makeText(MachineCommitOrderActivity.this,
										"服务器未响应", 2000).show();

							}
						});
			} else {
				Toast.makeText(MachineCommitOrderActivity.this, "请选择收货地址", 2000)
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
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK) { // KEYCODE_BACK=4;
			MachineCommitOrderActivity.this.finish();
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

	/**
	 * 
	 */
	private class Myshopad extends BaseAdapter {

		@Override
		public int getCount() {
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
			Product cb = li.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(
						R.layout.orderdetails_item, null);
			}
			NetworkImageView im = (NetworkImageView) convertView
					.findViewById(R.id.im);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView price = (TextView) convertView.findViewById(R.id.price);
			TextView num = (TextView) convertView.findViewById(R.id.num);
			name.setText(cb.getProduct_name());
			price.setText("" + cb.getStore_price());
			num.setText("数量 ：" + cb.getNum());
			Myapplication.client.getImageForNetImageView(cb.getProduct_photo(),
					im, R.drawable.ic_launcher);
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
	private void ovv(JSONArray ja) {
		final Dialog dialog = new MyDilog(MachineCommitOrderActivity.this);
		dialog.show();
		dialog.getWindow().findViewById(R.id.queding)
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dialog.cancel();
						MachineCommitOrderActivity.this.finish();
						MachineCommitOrderActivity.this
								.overridePendingTransition(
										R.anim.view_in_from_left,
										R.anim.view_out_to_right);
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
}
