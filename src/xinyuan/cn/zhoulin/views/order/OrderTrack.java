package xinyuan.cn.zhoulin.views.order;

import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
/**
 * 
 */
public class OrderTrack extends Activity {
	private Button back;
	private WebView wb;
	private String type;
	private String postid;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ordertrack);
		Intent in = getIntent();
		type = in.getStringExtra("type");
		postid = in.getStringExtra("postid");
		initview();
		initData();
		initListener();

	}
	/**
	 * 
	 */
	private void initListener() {
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				OrderTrack.this.finish();
				OrderTrack.this.overridePendingTransition(
						R.anim.view_in_from_left, R.anim.view_out_to_right);

			}
		});

	}
	/**
	 * 
	 */
	private void initview() {
		back = (Button) findViewById(R.id.back);
		wb = (WebView) findViewById(R.id.wb);
		wb.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		wb.getSettings().setJavaScriptEnabled(true);
	}
	/**
	 * 
	 */
	private void initData() {
		String s = UrlPath.ordertrack.replace("[aa]", type);
		String ss = s.replace("[bb]", postid);
		wb.loadUrl(ss);
	}
}
