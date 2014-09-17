package xinyuan.cn.zhoulin;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.Toast;

public class ProductDetailWebView extends Activity{
	private ProgressDialog progressBar;// 加载商品详情时候的进度条
	private AlertDialog alertDialog;// 加载商品详情出错时候的提示框
	private WebView webView;// 显示商品详情的webView
	private String productUrl = ""; // 商品详情网址
	private Button backBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_webview);
		progressBar = new ProgressDialog(this);// 初始化进度条
		webView = (WebView) findViewById(R.id.product_webview);// 初始化商品详情网页内容
		Intent intent=getIntent();
		productUrl=intent.getStringExtra("url");
		backBtn=(Button) findViewById(R.id.back);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			finish();
			}
		});
		initWebView();
	}
	
	/*
	 * 初始化webview
	 */
	protected void initWebView() {
		// 设计进度条
		progressBar = ProgressDialog.show(ProductDetailWebView.this, null,
				"正在加载商品详情，请稍后…");
		// 获得WebView组件
		webView.getSettings().setJavaScriptEnabled(true);
		WebSettings settings = webView.getSettings(); 
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
//		webView.getSettings().setSupportZoom(true);
//		webView.getSettings().setBuiltInZoomControls(true);
//		Log.i(MyApplication.TAG, "url-->"+url);
		webView.loadUrl(productUrl);
		alertDialog = new AlertDialog.Builder(this).create();
		// 设置视图客户端
		webView.setWebViewClient(new MyWebViewClient());
	}

	/*
	 * 设置加载网页时显示进度条，无法加载时弹出错误提示
	 */
	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			progressBar.show();
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			if (progressBar.isShowing()) {
				progressBar.dismiss();
			}
		}

		
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Toast.makeText(ProductDetailWebView.this, "网页加载出错！", Toast.LENGTH_LONG);
			alertDialog.setTitle("ERROR");
			alertDialog.setMessage(description);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			});
			alertDialog.show();
		}
	}
}
