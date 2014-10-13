package xinyuan.cn.zhoulin.views.order;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import xinyuan.cn.zhoulin.address.ManageAddress;
import xinyuan.cn.zhoulin.loginchanger.CenterLoginActivity;
import xinyuan.cn.zhoulin.loginchanger.ExitLoginActivity;
import xinyuan.cn.zhoulin.model.OrderBean;
import xinyuan.cn.zhoulin.model.OrderDetailsBean;
import xinyuan.cn.zhoulin.model.Product;
import xinyuan.cn.zhoulin.pay.Keys;
import xinyuan.cn.zhoulin.pay.PayMethod;
import xinyuan.cn.zhoulin.pay.Result;
import xinyuan.cn.zhoulin.pay.Rsa;
import xinyuan.cn.zhoulin.views.order.OrderDetails.Utility;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

/**
 * 
 */
public class PersonCenter extends Fragment implements OnClickListener {
	private View v;
	public Boolean gc = false;
	private ImageView personphoto;
	private LinearLayout tvly;
	private TextView username, tv1, tv2, tv3, tv4, tishi;
	private LinearLayout address;
	private ListView lv;
	private List<OrderBean> li;
	private Myad ad;
	private int page = 1;
	private int totalpage = 1;
	private String tab_flag = "0";
	private String order_flag="1";
	private View footView;
	private View loginOut;
	private LinearLayout.LayoutParams ll;
	private Button intercept;
	private Boolean jiazai = true;
	private int lastitem;
	private Boolean onece = true;
	private String ispay = "0";
	private HashMap<String, String> statu;
	private String payway="0";

	 private static String mMode = "01";//设置测试模式:01为测试 00为正式环境
	 private static final String TN_URL_01 = "http://www.zhoulinjk.com/unionpay_app/instance/tn.php";//

	private static final int BANK_PAY=3;//银联卡支付
	
	private static final int RQF_PAY = 1;//支付宝支付

	private static final int RQF_LOGIN = 2;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 
	 */
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		gc = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.orderfragment, container, false);
			inintView();
			initlistener();
		}
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}
		return v;
	}

	/**
	 * 
	 */
	private void initData() {

		if (Myapplication.login) {
			username.setText(Myapplication.sp.getString("username", ""));
			tishi.setVisibility(View.GONE);
			lv.setVisibility(View.VISIBLE);
			initTextColor(tab_flag);
			intercept.setVisibility(View.VISIBLE);
			footView.setVisibility(View.VISIBLE);
			initlvdata(PersonCenter.this.tab_flag, ispay,payway);
		} else {
			username.setText("未登录");
			tishi.setVisibility(View.VISIBLE);
			intercept.setVisibility(View.GONE);
			lv.setVisibility(View.GONE);
		}

	}

	/**
	 * 
	 */
	private void initlvdata(final String tab_flag, String ispay,String payway) {
		if (page <= totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("act", UrlPath.ACT_ORDER);
			ha.put("acttag", "list"); // 获取订单列表
			ha.put("store_id", Myapplication.store_id);
			ha.put("uid", Myapplication.sp.getString("uid", ""));
			ha.put("username", Myapplication.sp.getString("username", ""));
			ha.put("seskey", Myapplication.seskey);
			ha.put("page", "" + page);
			ha.put("per", "5");
			ha.put("order_flag", tab_flag);
			ha.put("ispay", ispay);
			ha.put("payway", payway);
			// ha.put("store_id", Myapplication.store_id);
			// ha.put("uid", Myapplication.sp.getString("uid", ""));
			// ha.put("username", Myapplication.sp.getString("username", ""));
			// ha.put("seskey", Myapplication.seskey);
			// ha.put("page", "" + page);
			// ha.put("per", "5");
			// ha.put("order_flag", tab_flag);
			Log.e("test", "orderList-->" + Myapplication.getUrl(ha));
			Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
					new Listener<JSONObject>() {
						public void onResponse(JSONObject response) {
							try {
								Log.i("test", "order->response:"+response.toString());
								if (response.getInt("code") == 1) {
									JSONArray ja = response
											.getJSONArray("list");
									page = response.getInt("page") + 1;
									totalpage = response.getInt("totalpage");
									for (int i = 0; i < ja.length(); i++) {

										JSONObject jb = ja.getJSONObject(i);
										OrderBean ob = new OrderBean();
										ob.setAddress(jb.getString("address"));
										ob.setOrderHc(jb.getString("hc"));
										ob.setOrder_subject(jb.getString("order_subject"));
										ob.setAddress(jb
												.getString("order_subject"));
										ob.setFreight(jb.getString("freight"));
										double freight=jb.getDouble("freight");
										String allPrice=String.valueOf( jb.getDouble("total_price")+freight);
										ob.setPrice(jb.getString("total_price"));
										ob.setAllprice(allPrice);
										ob.setCreatetime(jb
												.getString("createtime"));
										ob.setExpress_code(jb
												.getString("express_code"));
										ob.setExpress_enname(jb
												.getString("express_enname"));
										ob.setReleasetime("releasetime");
										ob.setPayway(jb.getString("payway"));
										ob.setFlag(jb.getString("orderflag"));
										ob.setIspay(jb.getString("ispay"));
										ob.setOrder_flag(jb.getString("orderflag"));
										JSONArray jaa = jb.getJSONArray("list");
										ArrayList<Product> al = new ArrayList<Product>();
										for (int m = 0; m < jaa.length(); m++) {
											JSONObject jj = jaa
													.getJSONObject(m);
											Product cb = new Product();
											cb.setProduct_id(jj
													.getString("product_id"));
											cb.setProduct_name(jj
													.getString("product_name"));
											cb.setProduct_photo(jj
													.getString("product_photo"));
											cb.setStore_price(jj
													.getDouble("store_price"));
											al.add(cb);
										}
										ob.setLi(al);
										ob.setMobile(jb.getString("mobile"));
										ob.setOrder_code(jb
												.getString("order_code"));
										ob.setOrder_id(jb.getString("order_id"));
										ob.setRealname(jb.getString("realname"));
										li.add(ob);
										Log.e("sdghddfhdfh", "ddddddddddddd");
									}
									ad.notifyDataSetChanged();
									footView.setVisibility(View.GONE);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							footView.setVisibility(View.GONE);
							intercept.setVisibility(View.GONE);
							jiazai = true;

						}

					}, new ErrorListener() {
						public void onErrorResponse(VolleyError error) {
							footView.setVisibility(View.GONE);
							intercept.setVisibility(View.GONE);
							jiazai = true;
						}
					});
		} else {
			footView.setVisibility(View.GONE);
			intercept.setVisibility(View.GONE);
			jiazai = true;

		}

	}

	/**
	 * 
	 */
	private void initTextColor(String order_flag) {
		int m = Integer.parseInt(order_flag);
		for (int i = 0; i < tvly.getChildCount(); i++) {
			TextView tv = (TextView) tvly.getChildAt(i);
			if (i == m) {
				tv.setBackgroundResource(R.drawable.order_an);
				tv.setTextColor(Color.parseColor("#62D036"));
			} else {
				tv.setBackgroundResource(R.drawable.order_ming);
				tv.setTextColor(Color.parseColor("#808080"));
			}

		}

	}

	/**
	 * 
	 */
	private void initlistener() {
		Log.e("asdffasfgsd", "-----------");
		personphoto.setOnClickListener(PersonCenter.this);
		address.setOnClickListener(PersonCenter.this);
		tv1.setOnClickListener(PersonCenter.this);
		tv2.setOnClickListener(PersonCenter.this);
		tv3.setOnClickListener(PersonCenter.this);
		tv4.setOnClickListener(PersonCenter.this);
		tishi.setOnClickListener(PersonCenter.this);
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastitem == (page - 1) * 5 + 1 && jiazai) {
					jiazai = false;
					intercept.setVisibility(View.VISIBLE);
					footView.setVisibility(View.VISIBLE);
					initlvdata(PersonCenter.this.tab_flag, ispay,payway);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastitem = totalItemCount;
			}
		});
	}

	/**
	 * 
	 */
	private void inintView() {
		ll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		ll.setMargins(0, 5, 7, 5);
		footView = Myapplication.lf.inflate(R.layout.footview, null);
		intercept = (Button) v.findViewById(R.id.intercept);
		tvly = (LinearLayout) v.findViewById(R.id.tvly);
		personphoto = (ImageView) v.findViewById(R.id.personphoto);
		username = (TextView) v.findViewById(R.id.username);
		address = (LinearLayout) v.findViewById(R.id.address);
		tv1 = (TextView) v.findViewById(R.id.tv1);
		tv2 = (TextView) v.findViewById(R.id.tv2);
		tv3 = (TextView) v.findViewById(R.id.tv3);
		tv4 = (TextView) v.findViewById(R.id.tv4);
		tishi = (TextView) v.findViewById(R.id.tishi);
		// loginOut= v.findViewById(R.id.loginOut);
		lv = (ListView) v.findViewById(R.id.lv);
		ad = new Myad();
		li = new ArrayList<OrderBean>();
		lv.addFooterView(footView);
		lv.setAdapter(ad);
		statu = new HashMap<String, String>();
//		ViewParent vp = v.getParent();
//		Log.i("test", "ViewParent"+vp.toString());
//		loginOut=v.getParent().getParent().findViewById(R.id.loginOut);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personphoto:
			Log.e("asdffasfgsd", "-----------");
			if (Myapplication.login) {
//				loginOut.setVisibility(View.VISIBLE);
				getActivity().startActivity(
						new Intent(getActivity(), ExitLoginActivity.class));
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			} else {
				getActivity().startActivity(
						new Intent(getActivity(), CenterLoginActivity.class));
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
			break;
		case R.id.address:
			if (Myapplication.login) {
				getActivity().startActivity(
						new Intent(getActivity(), ManageAddress.class));
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			} else {
				getActivity().startActivity(
						new Intent(getActivity(), CenterLoginActivity.class));
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
			break;
		case R.id.tv1:
			if (Myapplication.login) {
				initTextColor("0");
				clear();
				PersonCenter.this.tab_flag = "0";
				order_flag="100";
				ispay = "0";
				payway="1";
				intercept.setVisibility(View.VISIBLE);
				footView.setVisibility(View.VISIBLE);
				initlvdata(order_flag, ispay,payway);
			} else {
				getActivity().startActivity(
						new Intent(getActivity(), CenterLoginActivity.class));
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
			break;
		case R.id.tv2:
			if (Myapplication.login) {
				initTextColor("1");
				clear();
				PersonCenter.this.tab_flag = "1";
				order_flag="101";
				ispay = "0";
				payway="0";
				intercept.setVisibility(View.VISIBLE);
				footView.setVisibility(View.VISIBLE);
				initlvdata(order_flag, ispay,payway);
			} else {
				getActivity().startActivity(
						new Intent(getActivity(), CenterLoginActivity.class));
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
			break;
		case R.id.tv3:
			if (Myapplication.login) {
				initTextColor("2");
				clear();
				PersonCenter.this.tab_flag = "2";
				order_flag="3";
				ispay = "-1";
				payway="0";
				intercept.setVisibility(View.VISIBLE);
				footView.setVisibility(View.VISIBLE);
				initlvdata(order_flag, ispay,payway);
			} else {
				getActivity().startActivity(
						new Intent(getActivity(), CenterLoginActivity.class));
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
			break;
		case R.id.tv4:
			if (Myapplication.login) {
				initTextColor("3");
				clear();
				PersonCenter.this.tab_flag = "3";
				order_flag="5";
				ispay = "-1";
				payway="0";
				intercept.setVisibility(View.VISIBLE);
				footView.setVisibility(View.VISIBLE);
				initlvdata(order_flag, ispay,payway);
			} else {
				getActivity().startActivity(
						new Intent(getActivity(), CenterLoginActivity.class));
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
			break;
		case R.id.tishi:
			getActivity().startActivity(
					new Intent(getActivity(), CenterLoginActivity.class));
			getActivity().overridePendingTransition(R.anim.view_in_from_right,
					R.anim.view_out_to_left);
			break;
		case R.id.left:
			Intent in = new Intent(getActivity(), OrderDetails.class);
			OrderBean ob = (OrderBean) v.getTag();
			in.putExtra("ob", ob);
			in.putExtra("statu", statu);
			startActivity(in);
			getActivity().overridePendingTransition(R.anim.view_in_from_right,
					R.anim.view_out_to_left);
			break;
		case R.id.right:
			View cc = (View) v.getParent().getParent();
			TextView cv = (TextView) cc.findViewById(R.id.bt);
			TextView flag = (TextView) cc.findViewById(R.id.flag);
			TextView tv = (TextView) v;
			OrderBean om = (OrderBean) tv.getTag();
			if (tv.getText().toString().equals("取消订单")) {
				cancel(om.getOrder_id(), tv, om.getPoistion(), cv, flag);
			} else if (tv.getText().toString().equals("删除订单")) {
				dele(om.getOrder_id(), om.getPoistion());
			}

			break;
		case R.id.bt: // 支付宝支付和订单追踪的按钮 need
			TextView b = (TextView) v;
			OrderBean oo = (OrderBean) b.getTag();
			if (b.getText().equals("支付")) {
				Intent intent=new Intent();
				intent.setClass(getActivity(),PayMethod.class);
				intent.putExtra("subject", oo.getOrder_subject());
				intent.putExtra("price", oo.getAllprice());
				intent.putExtra("hc", oo.getOrderHc());
				intent.putExtra("oid", oo.getOrder_id());
				startActivity(intent);
			

			} else if (b.getText().equals("订单追踪")) {
				b.setVisibility(View.GONE);
//				Intent inn = new Intent(getActivity(), OrderTrack.class);
//				inn.putExtra("type", oo.getExpress_enname());
//				inn.putExtra("postid", oo.getExpress_code());
//				startActivity(inn);
//				getActivity().overridePendingTransition(
//						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}

			break;
		// case R.id.loginOut:
		// //need
		// loginOut.setVisibility(View.VISIBLE);
		// getActivity().startActivity(
		// new Intent(getActivity(), ExitLoginActivity.class));
		// break;
		default:
			break;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (onece) {
			initData();
			onece = false;
			return;
		}
		if (Myapplication.orderneedflash) {
			clear();
			Myapplication.orderneedflash = false;
			initData();
		}
	}

	/**
	 * 
	 */
	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
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
			OrderBean ob = li.get(position);
			ob.setPosition(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(R.layout.order1_item,
						null);
			}
			TextView order_code = (TextView) convertView
					.findViewById(R.id.order_code);
			TextView totalprice = (TextView) convertView
					.findViewById(R.id.totalprice);
			TextView flag = (TextView) convertView.findViewById(R.id.flag);
			TextView createtime = (TextView) convertView
					.findViewById(R.id.createtime);
			TextView bt = (TextView) convertView.findViewById(R.id.bt);
			LinearLayout show1 = (LinearLayout) convertView
					.findViewById(R.id.show1);
			NetworkImageView im = (NetworkImageView) convertView
					.findViewById(R.id.im);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView num = (TextView) convertView.findViewById(R.id.num);
			TextView price = (TextView) convertView.findViewById(R.id.price);
			HorizontalScrollView showmore = (HorizontalScrollView) convertView
					.findViewById(R.id.showmore);
			LinearLayout images = (LinearLayout) convertView
					.findViewById(R.id.images);
			TextView left = (TextView) convertView.findViewById(R.id.left);
			TextView right = (TextView) convertView.findViewById(R.id.right);
			order_code.setText(ob.getOrder_code());
			totalprice.setText("￥"+ob.getAllprice());
			createtime.setText(ob.getCreatetime());
			left.setTag(ob);
			right.setTag(ob);
			bt.setTag(ob);
			ArrayList<Product> li = (ArrayList<Product>) ob.getLi();
			if (ob.getLi().size() > 1) {
				show1.setVisibility(View.GONE);
				showmore.setVisibility(View.VISIBLE);
				images.removeAllViews();
				for (int i = 0; i < li.size(); i++) {
					Product cb = li.get(i);
					LinearLayout oo = (LinearLayout) Myapplication.lf.inflate(
							R.layout.sale_li, null);
					NetworkImageView nv = (NetworkImageView) oo
							.findViewById(R.id.im);
					TextView pric = (TextView) oo.findViewById(R.id.price);
					pric.setText("" + cb.getStore_price());
					TextView nam = (TextView) oo.findViewById(R.id.name);
					nam.setText(cb.getProduct_name());
					images.addView(oo, ll);
					Myapplication.client.getImageForNetImageView(
							cb.getProduct_photo(), nv, R.drawable.ic_launcher);
				}

			} else if (ob.getLi().size() == 1) {
				show1.setVisibility(View.VISIBLE);
				showmore.setVisibility(View.GONE);
				Product cb = li.get(0);
				Myapplication.client.getImageForNetImageView(
						cb.getProduct_photo(), im, R.drawable.ic_launcher);
				name.setText(cb.getProduct_name());
				num.setText("数量  :" + cb.getNum());
				price.setText("￥" + cb.getStore_price());
			}
			Log.i("test", "ob.getPayway()"+ ob.getPayway());
			Log.i("test", "getOrder_flag-->" + ob.getOrder_flag()+"  "+ob.getOrder_code()+" "+ob.getIspay());
			Log.i("test", "tab_flag-->" + tab_flag);
			switch (Integer.parseInt(tab_flag)) {
			case 0:
				if (ob.getFlag().equals("1") && ob.getIspay().equals("0")&& ob.getPayway().equals("1")) {
					flag.setText("未发货，待付款");
					right.setText("取消订单");
					bt.setVisibility(View.VISIBLE); //显示支付宝支付
					bt.setText("支付");
				} 
				else if (ob.getFlag().equals("1") && ob.getIspay().equals("0")&& ob.getPayway().equals("2")) {
					flag.setText("未发货，货到付款");
					right.setText("取消订单");
					bt.setVisibility(View.GONE);//不显示支付宝支付
				}
				else if (ob.getFlag().equals("1") && ob.getIspay().equals("1")) {
					flag.setText("未发货，已付款");
					right.setText("取消订单");
					bt.setVisibility(View.GONE);//不显示支付宝支付
				}
				else if (ob.getFlag().equals("2")) {
					flag.setText("订单已取消");
					right.setText("删除订单");
					bt.setVisibility(View.GONE);//不显示支付宝支付
				}else if(ob.getFlag().equals("5")&& ob.getIspay().equals("0"))
				{
					flag.setText("测试版的已完成订单");
					right.setText("删除订单");
				}
				statu.put(ob.getOrder_code(), flag.getText().toString());
				// --
				break;
			case 1:
				if (ob.getFlag().equals("1") && ob.getIspay().equals("0")&& ob.getPayway().equals("1")) {
					flag.setText("未发货，待付款");
					right.setText("取消订单");
					bt.setVisibility(View.VISIBLE); //显示支付宝支付
					bt.setText("支付");
				} 
				else if (ob.getFlag().equals("1") && ob.getIspay().equals("0")&& ob.getPayway().equals("2")) {
					flag.setText("未发货，货到付款");
					right.setText("取消订单");
					bt.setVisibility(View.GONE);//不显示支付宝支付
				}
				else if (ob.getFlag().equals("1") && ob.getIspay().equals("1")) {
					flag.setText("未发货，已付款");
					right.setText("取消订单");
					bt.setVisibility(View.GONE);//不显示支付宝支付
				}
				else if (ob.getFlag().equals("2")) {
					flag.setText("订单已取消");
					right.setText("删除订单");
					bt.setVisibility(View.GONE);//不显示支付宝支付
				}
				statu.put(ob.getOrder_code(), flag.getText().toString());
				break;
			case 2:
				if (ob.getFlag().equals("3") && ob.getIspay().equals("1")) {
					flag.setText("派送中，已付款");
					right.setText("");//已发货的订单无法取消，删除和取消订单的地方为空
					bt.setVisibility(View.GONE);//如果已经付款而且已经派送，支付宝支付更改为订单追踪
					bt.setText("订单追踪");
				}
				else if (ob.getFlag().equals("3") && ob.getIspay().equals("0")&& ob.getPayway().equals( "1")) {
					flag.setText("派送中，未付款");
					right.setText("");
					bt.setVisibility(View.VISIBLE); //显示支付宝支付
					bt.setText("支付");
				
				}
				else if (ob.getFlag().equals("3") && ob.getIspay().equals("0")&& ob.getPayway().equals( "2")) {
					flag.setText("派送中，货到付款");
					right.setText("");
					bt.setVisibility(View.GONE); //不显示支付宝支付
				}

				statu.put(ob.getOrder_code(), flag.getText().toString());
				// flag.setText("已发货");
				// right.setText("");
				// bt.setVisibility(View.VISIBLE);
				// bt.setText("订单追踪");
				break;
			case 3:
//				Log.i("test", "getFlag-->"+ob.getFlag()+" " +ob.getIspay());
				if (ob.getFlag().equals("4")) {
					flag.setText("货物拒收");
					right.setText("删除订单");
					bt.setVisibility(View.GONE);

				} else if (ob.getFlag().equals("5")&& ob.getIspay().equals("1")) {
					flag.setText("交易完成");
					right.setText("删除订单");
				}
				else if(ob.getFlag().equals("5")&& ob.getIspay().equals("0"))
				{
					flag.setText("测试版的已完成订单");
					right.setText("删除订单");
				}
				statu.put(ob.getOrder_code(), flag.getText().toString());
				break;
			default:
				break;
			}
			bt.setOnClickListener(PersonCenter.this);
			left.setOnClickListener(PersonCenter.this);
			right.setOnClickListener(PersonCenter.this);
			return convertView;
		}
	}

	/**
	 * 
	 */
	private void clear() {
		page = 1;
		totalpage = 1;
		li.clear();
	}

	/**
	 * 
	 */
	public void dele(final String id, final int pos) {
		new AlertDialog.Builder(getActivity())
				.setMessage("确定清除该订单?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						final HashMap<String, String> ha = new HashMap<String, String>();
						ha.put("act", UrlPath.ACT_ORDER);
						ha.put("acttag", "delete"); // 删除订单
						ha.put("uid", Myapplication.sp.getString("uid", ""));
						ha.put("username",
								Myapplication.sp.getString("username", ""));
						ha.put("seskey", Myapplication.seskey);
						ha.put("order_id", id);
						ha.put("store_id", Myapplication.store_id);
						// Log.e("test",Myapplication.getUrl(ha));
						Myapplication.client.postWithURL(UrlPath.SERVER_URL,
								ha, new Listener<JSONObject>() {
									@Override
									public void onResponse(JSONObject response) {
										try {
											if (response.getInt("code") == 1) {
												li.remove(pos);
												Toast.makeText(getActivity(),
														"删除成功", 2000).show();
												ad.notifyDataSetChanged();
											}
											else
											{
												Toast.makeText(getActivity(),
														"删除失败，"+response.getString("msg"), 2000).show();
											}
										} catch (JSONException e) {
											Toast.makeText(getActivity(),
													"删除失败", 2000).show();
											e.printStackTrace();
										}

									}
								}, new ErrorListener() {

									@Override
									public void onErrorResponse(
											VolleyError error) {

									}
								});

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	/**
	 * 
	 */
	public void cancel(final String id, final TextView v, final int pos,
			final TextView cv, final TextView flag) {
		new AlertDialog.Builder(getActivity())
				.setMessage("确定取消订单?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						HashMap<String, String> ha = new HashMap<String, String>();
						ha.put("act", UrlPath.ACT_ORDER);
						ha.put("acttag", "cancle");
						ha.put("store_id", Myapplication.store_id);
						ha.put("uid", Myapplication.sp.getString("uid", ""));
						ha.put("username",
								Myapplication.sp.getString("username", ""));
						ha.put("seskey", Myapplication.seskey);
						ha.put("order_id", id);
						// ha.put("uid", Myapplication.sp.getString("uid", ""));
						// ha.put("username",
						// Myapplication.sp.getString("username", ""));
						// ha.put("seskey", Myapplication.seskey);
						// ha.put("order_id", id);
						Log.i("test", "cacel-->"+Myapplication.getUrl(ha));
						Myapplication.client.postWithURL(UrlPath.SERVER_URL,
								ha, new Listener<JSONObject>() {
									@Override
									public void onResponse(JSONObject response) {
										try {
											Log.i("test", "cacel code-->"+response.getInt("code"));
											if (response.getInt("code") == 1) {
												Toast.makeText(getActivity(),
														"取消成功", 2000).show();
												li.get(pos).setFlag("1");
												v.setText("删除订单");
												cv.setVisibility(View.GONE);
												flag.setText("已取消");
												// initData();
												// ad.notifyDataSetChanged();
											}else
											{
												Toast.makeText(getActivity(),
														"取消失败，"+response.getString("msg"), 2000).show();
											}
										} catch (JSONException e) {
											Toast.makeText(getActivity(),
													"取消失败", 2000).show();
											e.printStackTrace();
										}

									}
								}, new ErrorListener() {

									@Override
									public void onErrorResponse(
											VolleyError error) {

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
