package xinyuan.cn.zhoulin;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.model.Product;
import xinyuan.cn.zhoulin.model.MyView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

public class Zxingshow extends Activity implements OnClickListener { // 公用Activity商品详细信息
	private String url;
	private View back;
	private WebView wb;
	private TextView title;
	private ProgressBar pr;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zxingshow);
		url = (String) getIntent().getExtras().get("url");
		initView();
		initlistener();
	}

	private void initlistener() {
		back.setOnClickListener(this);
	}

	private void initView() {
		pr = (ProgressBar) findViewById(R.id.pr);
		title = (TextView) findViewById(R.id.title);
		back = findViewById(R.id.back);
		wb = (WebView) findViewById(R.id.wb);
		wb.getSettings().setJavaScriptEnabled(true);
		wb.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 超链接时会调用此方法
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				pr.setVisibility(View.GONE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				view.loadUrl(Myapplication.locality);
			}

		});
		wb.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Builder builder = new Builder(Zxingshow.this);
				builder.setTitle("");
				builder.setMessage(message);
				builder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 点击确定按钮之后，继续执行网页中的操作
								result.confirm();
							}
						});
				builder.setCancelable(false);
				builder.create();
				builder.show();
				return true;
			}
		});
		wb.loadUrl(url);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			Zxingshow.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);

			break;

		default:
			break;
		}

	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK) { // KEYCODE_BACK=4;
			Zxingshow.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			return true;
		}
		return true;
	}

}
