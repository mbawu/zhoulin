package xinyuan.cn.zhoulin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
/**
 * 
 */
public class ContentActivity extends Activity implements OnClickListener {
	private Button back;
	private WebView wb;
	private String url;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contentactivity);
		url = getIntent().getExtras().getString("url");
		initview();
		initlistener();
		initdata();
	}

	private void initdata() {
		wb.loadUrl(url);
	}

	private void initlistener() {
		back.setOnClickListener(this);
	}

	private void initview() {
		back = (Button) findViewById(R.id.back);
		wb = (WebView) findViewById(R.id.web);
		wb.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		wb.getSettings().setJavaScriptEnabled(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			ContentActivity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;

		default:
			break;
		}

	}

}
