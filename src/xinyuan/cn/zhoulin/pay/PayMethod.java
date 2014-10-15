package xinyuan.cn.zhoulin.pay;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.android.app.sdk.AliPay;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PayMethod extends Activity implements OnClickListener {

	private Button backBtn;
	private LinearLayout alipay;
	private LinearLayout bankpay;

	private String subject;
	private String price;
	private String oid;
	private String hc;

	private static String mMode = "00";// 设置测试模式:01为测试 00为正式环境
	private static final String TN_URL_01 = "http://www.zhoulinjk.com/unionpay_app/instance/tn.php";// 银联获取TN接口
	private static final int BANK_PAY = 3;// 银联卡支付
	private static final int RQF_PAY = 1;// 支付宝支付
	private static final int RQF_LOGIN = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.paymethod);
		initView();
	}

	private void initView() {
		backBtn = (Button) findViewById(R.id.back);
		alipay = (LinearLayout) findViewById(R.id.alipay);
		bankpay = (LinearLayout) findViewById(R.id.bankpay);
		backBtn.setOnClickListener(this);
		alipay.setOnClickListener(this);
		bankpay.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.alipay:// 支付宝支付接口
			alipay();
			break;
		case R.id.bankpay:// 银联卡支付接口
			bankpay();
			break;
		}

	}

	private void bankpay() {
		new Thread() {
			public void run() {
				Intent intent = getIntent();
				subject = intent.getStringExtra("subject");
				price = intent.getStringExtra("price");
				double tempPrice = Double.valueOf(price) * 100;// 转换价格
				price=String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(tempPrice)));
				oid = intent.getStringExtra("oid");
				hc = intent.getStringExtra("hc");
				HashMap<String, String> paramter = new HashMap<String, String>();
				paramter.put("oid", oid);
				paramter.put("hc", hc);
				paramter.put("subject", subject);
				paramter.put("price", price);
				Log.i("test", "price-->"+price);
				Log.i("test", Myapplication.getUrl(paramter));
				Myapplication.client.postWithURL(TN_URL_01, paramter,
						new Listener<JSONObject>() {
							public void onResponse(JSONObject response) {
								String tn;
								try {
									tn = response.getString("code");
									Log.i("test", "tn-->"+tn);
									if (!tn.equals("-1")) {
										Message msg = mHandler.obtainMessage();
										msg.what = BANK_PAY;
										msg.obj = tn;
										mHandler.sendMessage(msg);
									} else {
										Toast.makeText(PayMethod.this, "连接服务器失败",
												2000);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.i("test", "VolleyError-->"+error.getMessage());
							}
						});
			}
		}.start();

	}

	private void alipay() {
		Intent intent = getIntent();
		subject = intent.getStringExtra("subject");
		price = intent.getStringExtra("price");
		oid = intent.getStringExtra("oid");
		hc = intent.getStringExtra("hc");
		Log.i("test", "subject:"+subject+",price:"+price+",oid-->"+oid+",hc:"+hc);
		// 支付宝支付接口
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(getOutTradeNo());
		sb.append("\"&subject=\"");
		sb.append(subject);
//		sb.append("\"&hc=\"");
//		sb.append(hc);
		sb.append("\"&body=\"");
		sb.append("ID:"+oid);
		sb.append("\"&total_fee=\"");
		sb.append(price);
		sb.append("\"&notify_url=\"");
		// 网址需要做URL编码
		sb.append(URLEncoder
				.encode("http://www.zhoulinjk.com/alipay3/notify_url.php"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder
				.encode("http://www.zhoulinjk.com/alipay3/call_back_url.php"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);
		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");
		String info = new String(sb);
		String sign = Rsa.sign(info, Keys.PRIVATE);
		Log.i("test", "sign-->" + sign);
		sign = URLEncoder.encode(sign);
		info += "&sign=\"" + sign + "\"&" + getSignType();
		// 获取订单组装字符串
		final String orderInfo = info;
		Log.i("test", "info-->" + orderInfo);
		new Thread() {
			public void run() {
				// 获取Alipay对象，构造参数为当前Activity和Handler实例对象
				AliPay alipay = new AliPay(PayMethod.this, mHandler);
				// 调用pay方法，将订单信息传入
				String result = alipay.pay(orderInfo);
				Log.i("test", "result-->" + result);
				// 处理返回结果
				Message msg = new Message();
				msg.what = RQF_PAY;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		}.start();

	}

	/**
	 * 启动支付界面
	 */
	public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
		UPPayAssistEx.startPayByJAR(activity, PayActivity.class, null, null,
				tn, mode);
	}

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

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);

			switch (msg.what) {
			case RQF_PAY:
			case RQF_LOGIN: {
				Toast.makeText(PayMethod.this, result.getErrorResult(),
						Toast.LENGTH_SHORT).show();

			}
				break;
			case BANK_PAY:
				String tn = "";
				if (msg.obj == null || ((String) msg.obj).length() == 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							PayMethod.this);
					builder.setTitle("错误提示");
					builder.setMessage("网络连接失败,请重试!");
					builder.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();
				} else {
					tn = (String) msg.obj;
					doStartUnionPayPlugin(PayMethod.this, tn, mMode);
				}

				break;
			default:
				break;
			}
		};
	};
}
