package xinyuan.cn.zhoulin.views.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.NetworkImageView;

import xinyuan.cn.zhoulin.MainActivity;
import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;

import xinyuan.cn.zhoulin.model.OrderBean;
import xinyuan.cn.zhoulin.model.OrderDetailsBean;
import xinyuan.cn.zhoulin.model.Product;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class OrderProductEvaluate extends Activity implements OnClickListener {

	private Button backBtn;
	private ListView productLv;
	private Product product;
	private ArrayList<Product> productList;
	private ProductAdatpter adapter;
	private String comment_content;
	private boolean sendSuccess = false;
	private String sendMsg = "";
	private String orderlist_id="0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_product_evaluate);
		initView();
		initData();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		      if (keyCode == KeyEvent.KEYCODE_BACK) {  
		    	  Intent intent = new Intent();
					intent.setClass(OrderProductEvaluate.this,
							OrderDetails.class);
					intent.putExtra("comment", "fail");
					intent.putExtra("orderlist_id", orderlist_id);
					setResult(Activity.RESULT_OK, intent);
					OrderProductEvaluate.this.finish();
		      } 
		      return true;
	}
	private void initData() {
		Intent intent = getIntent();
		// 从上个页面直接取到已经序列化的产品的信息
		productList = (ArrayList<Product>) intent
				.getSerializableExtra("products");
	}

	private void initView() {
		backBtn = (Button) findViewById(R.id.backComment);
		productLv = (ListView) findViewById(R.id.lv);
		adapter = new ProductAdatpter();
		productLv.setAdapter(adapter);
		product = new Product();
		productList = new ArrayList<Product>();
		backBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.backComment:
			Intent intent = new Intent();
			intent.setClass(OrderProductEvaluate.this,
					OrderDetails.class);
			intent.putExtra("comment", "fail");
			intent.putExtra("orderlist_id", orderlist_id);
			setResult(Activity.RESULT_OK, intent);
			OrderProductEvaluate.this.finish();
			break;

		default:
			break;
		}
		// if (v.getId() == R.id.backComment)
		// OrderProductEvaluate.this.finish();
	}

	/**
	 * 根据adapter的数量来刷新listView的高度
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

	private class ProductAdatpter extends BaseAdapter {

		@Override
		public int getCount() {
			if (productList == null) {
				return 0;
			} else {
				return productList.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			product = productList.get(position);
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(
						R.layout.orderevaluate_item, null);
			}
			NetworkImageView nw = (NetworkImageView) convertView
					.findViewById(R.id.im);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			// TextView num = (TextView) convertView.findViewById(R.id.num);
			TextView price = (TextView) convertView.findViewById(R.id.price);
			final EditText getText = (EditText) convertView
					.findViewById(R.id.addContent);
			final RatingBar stars = (RatingBar) convertView
					.findViewById(R.id.starBtn);
			Button addComment = (Button) convertView
					.findViewById(R.id.addComment);
			addComment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (getText.getText().toString().equals("")) {
						Toast.makeText(OrderProductEvaluate.this,
								"请填写评论！", 2000);
						return;
					}
					sendMsg(getText.getText().toString(),
							Float.valueOf(stars.getRating()));

					// Toast.makeText(OrderProductEvaluate.this, result,
					// 2000).show();

					productList.remove(position);
					adapter.notifyDataSetChanged();
					if (productList.size() == 0)
					{
					Intent intent = new Intent();
					intent.setClass(OrderProductEvaluate.this,
							OrderDetails.class);
					intent.putExtra("comment", "suc");
					intent.putExtra("orderlist_id", orderlist_id);
					setResult(Activity.RESULT_OK, intent);
					OrderProductEvaluate.this.finish();
				}
				}
			});
			name.setText(product.getProduct_name());
			price.setText("￥：" + product.getStore_price());
			Myapplication.client.getImageForNetImageView(
					product.getProduct_photo(), nw, R.drawable.ic_launcher);
			return convertView;
		}

		private void sendMsg(String content, float stars) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("act", UrlPath.ACT_COMMENT);
			ha.put("acttag", "add");
			ha.put("store_id", Myapplication.store_id);
			ha.put("uid", Myapplication.sp.getString("uid", ""));
			ha.put("username", Myapplication.sp.getString("username", ""));
//			ha.put("realname", "");
			ha.put("seskey", Myapplication.seskey);
			ha.put("orderlist_id", product.getOrderlist_id());
			orderlist_id=product.getOrderlist_id();
			ha.put("product_id", product.getProduct_id());
			ha.put("comment_subject", product.getProduct_name());
			ha.put("comment_content", content);
			ha.put("evaluation_star", String.valueOf((int) stars));
			Log.i("test", "发表评论：" + Myapplication.getUrl(ha));

			Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {

							try {
								int code = response.getInt("code");
								Log.i("test", "code-->" + +code+response.getString("msg"));
								switch (code) {
								case 1:

									Toast.makeText(OrderProductEvaluate.this,
											"评论成功！", 2000);
									break;

								default:
									Toast.makeText(OrderProductEvaluate.this,
											 response.getString("msg"), 2000);
									break;
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							error.printStackTrace();
							Log.i("test", "发表评论error："+error.getMessage());
						}
					});
		}
	}
}
