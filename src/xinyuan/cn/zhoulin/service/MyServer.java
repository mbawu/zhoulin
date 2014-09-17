//package xinyuan.cn.zhoulin.service;
//
//import java.util.HashMap;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import xinyuan.cn.zhoulin.Myapplication;
//import xinyuan.cn.zhoulin.R;
//import xinyuan.cn.zhoulin.SongcuomenActivity;
//import xinyuan.cn.zhoulin.UrlPath;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.util.Log;
//
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.Response.Listener;
//import com.android.volley.VolleyError;
//
//public class MyServer extends Service {
//	// 获取消息线程
//	// 点击查看
//	private Intent messageIntent = null;
//	private PendingIntent messagePendingIntent = null;
//	// 通知栏消息
//	private int messageNotificationID = 1000;
//	private Notification messageNotification = null;
//	private NotificationManager messageNotificationManager = null;
//	private Boolean isrun = true;
//	private MyThread mt;
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		return null;
//	}
//
//	@Override
//	public void onCreate() {
//		super.onCreate();
//		messageNotification = new Notification();
//		messageNotification.tickerText = "新消息"; // 通知标题
//		messageNotification.defaults = Notification.DEFAULT_SOUND;
//		messageNotification.icon = R.drawable.logomini;
//		messageNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
//		// 点击查看
//		messageIntent = new Intent(this, SongcuomenActivity.class);
//		messagePendingIntent = PendingIntent.getActivity(this, 0,
//				messageIntent, 0);
//		mt = new MyThread();
//	}
//
//	/**
//	 * 
//	 */
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		if (!mt.isAlive()) {
//			mt.start();
//		}
//		return super.onStartCommand(intent, flags, startId);
//	}
//	/**
//	 * 
//	 */
//	public class MyThread extends Thread {
//		public void run() {
//			while (isrun) {
//				try {
//					Thread.sleep(120000);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//				HashMap<String, String> ha = new HashMap<String, String>();
//				ha.put("push_id", Myapplication.sp.getString("push_id", "0"));
//				ha.put("push_type", "2");
//				Myapplication.client.postWithURL(UrlPath.push, ha,
//						new Listener<JSONObject>() {
//
//							@Override
//							public void onResponse(JSONObject response) {
//								try {
//									if (response.getInt("code") == 1) {
//										JSONArray ja = response
//												.getJSONArray("list");
//										for (int i = 0; i < ja.length(); i++) {
//											JSONObject jb = ja.getJSONObject(i);
//											messageNotification.setLatestEventInfo(
//													MyServer.this, "无线盒子",
//													jb.getString("push_title"),
//													messagePendingIntent);
//											// 发布消息
//											messageNotificationManager.notify(
//													messageNotificationID,
//													messageNotification);
//											// 避免覆盖消息，采取ID自增
//											messageNotificationID++;
//											Myapplication.ed.putString(
//													"push_id",
//													jb.getString("push_id"));
//											Myapplication.ed.commit();
//										}
//									}
//
//								} catch (JSONException e) {
//									e.printStackTrace();
//								}
//							}
//
//						}, new ErrorListener() {
//							@Override
//							public void onErrorResponse(VolleyError error) {
//
//							}
//
//						});
//
//			}
//
//		}
//	}
//
//	/*
//	 * public void onDestroy() { super.onDestroy(); isrun = false;
//	 * MyServer.this.startService(new Intent(MyServer.this, MyServer.class));
//	 * 
//	 * }
//	 */
//}
