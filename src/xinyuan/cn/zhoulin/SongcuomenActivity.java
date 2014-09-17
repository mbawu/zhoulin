package xinyuan.cn.zhoulin;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
/**
 * 
 */ 
//public class SongcuomenActivity extends Activity { // LOGO界面，公司标示
 public class SongcuomenActivity extends InstrumentedActivity { // LOGO界面，公司标示
	/** Called when the activity is first created. */
	private Timer ti;
	private Handler handler;

	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		//获取屏幕高和宽的长度
		Myapplication.width = metric.widthPixels;
		Myapplication.height = metric.heightPixels;
		Myapplication.getInstance().addActivity(this);
		Thread thread=new Thread(new MyRunnable());
		thread.start();
//		handler = new Handler() {
//			public void handleMessage(Message msg) {
//				start();
//			}
//		};
//		handler.sendEmptyMessageDelayed(1, 2000);
//		SongcuomenActivity.this.startService(new Intent(
//				SongcuomenActivity.this, MyServer.class));
		//初始化极光推送服务
		
//		Intent intent=new Intent();
//		intent.setClass(SongcuomenActivity.this, MainActivity.class);
//		startActivity(intent);
//		this.finish();
	}
	

	/**
	 * 
	 */
	private void start() {
		ti = new Timer();
		TimerTask tast = new TimerTask() {
			public void run() {
				startActivity(new Intent(SongcuomenActivity.this,
						MainActivity.class));
			}
		};
		ti.schedule(tast, 100);
		SongcuomenActivity.this.finish();
	}

	@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			JPushInterface.setDebugMode(true);
			JPushInterface.init(this);
		}
	class MyRunnable implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				JPushInterface.setDebugMode(true);
				JPushInterface.init(SongcuomenActivity.this);
				Thread.sleep(2000);
				startActivity(new Intent(SongcuomenActivity.this,
						MainActivity.class));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}