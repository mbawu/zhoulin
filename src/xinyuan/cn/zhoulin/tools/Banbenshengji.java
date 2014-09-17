﻿package xinyuan.cn.zhoulin.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.widget.Toast;
/**
 * 
 */
public class Banbenshengji {
	private String xmlurl = "";
	private UpdateInfo info = null;
	private Context cn;
	private Handler ha;
	private ProgressDialog pd;

	public Banbenshengji(String xmlurl, Context cn) {
		this.xmlurl = xmlurl;
		this.cn = cn;
		ha = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					showUpdateDialog(); // 如果需要更新走这个
					break;
				case 2:
					showErrorDialog(); // 如果检查版本的时候网络出错走这个
					break;
				case 4:
					Toast.makeText(Banbenshengji.this.cn, "抱歉网络异常更新失败", 2000)
							.show();
					break;
				case 5:
					Toast.makeText(Banbenshengji.this.cn, "请检查您的SD卡是否安装", 2000)
							.show();

					break;

				default:
					Toast.makeText(Banbenshengji.this.cn, "无需升级，已经是最新版本哦", 2000)
							.show();
					break;
				}
			}
		};
	}

	public void jiancha() { // 主要要用到的方法检查版本更新
		new Thread(new CheckVersionTask()).start();
	}

	private class CheckVersionTask implements Runnable { // 检查更新线程通过检查服务器XML文件里的版本信息和本地的对比来判断是否更新
		public void run() {

			try {
				URL url = new URL(xmlurl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				Thread.sleep(4000);
				InputStream ins = conn.getInputStream();
				info = getUpdateInfo(ins);// 用解析器解析
				String vs = cn.getApplicationContext().getResources()
						.getString(R.string.version);
				if (vs.equals(info.getVersion())) {
					ha.sendEmptyMessage(6);
				} else {
					sendUpdateMessage();
				}
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = 2;
				ha.sendMessage(msg);
				e.printStackTrace();
			}
		}

		private void sendUpdateMessage() {
			Message msg = new Message();
			msg.what = 1;
			ha.sendMessage(msg);
		}
	}

	public UpdateInfo getUpdateInfo(InputStream ins) throws Exception { // 通过解析服务器XML文件流获得到里面的版本信息等内容封装JAVAbean
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(ins, "UTF-8");
		int type = -1;
		UpdateInfo info = new UpdateInfo();
		type = parser.getEventType();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName())) {
					info.setVersion(parser.nextText());
				}
				if ("url".equals(parser.getName())) {
					info.setUrl(parser.nextText());
				}
				if ("note".equals(parser.getName())) {
					info.setNote(parser.nextText());
				}
				break;
			}

			type = parser.next();
		}

		return info;
	}

	public class UpdateInfo { // JAVABEAN用来封装从服务器获得到的XML里面解析出来的信息
		private String version;
		private String url;
		private String note;

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}

	}

	private void showErrorDialog() { // 网络出现错误展示给用户看的对话框
		AlertDialog.Builder builder = new Builder(cn);
		builder.setTitle("错误");
		builder.setMessage("连接服务器失败");
		builder.setPositiveButton("确定", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void showUpdateDialog() { // 需要更新的情况下展示给用户看的对话框
		AlertDialog.Builder builder = new Builder(cn);
		builder.setTitle("请更新版本");
		builder.setMessage(info.getNote());
		builder.setNegativeButton("取消", new OnClickListener() {
			// 设置取消按钮
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("确定", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				pd = new ProgressDialog(cn);
				pd.setMessage("正在下载。。");
				pd.setCanceledOnTouchOutside(false);
				pd.setCancelable(false);
				pd.show();
				Myapplication.sp.edit().putBoolean("one", true);
				new Thread() {
					public void run() {
						File file;
						try {
							file = getFile(info.getUrl());
							install(file);
							pd.dismiss();
						} catch (Exception e) {
							Message ms = new Message();
							ms.what = 4;
							ha.sendMessage(ms);
							e.printStackTrace();
						}
					}
				}.start();

			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();

	}

	private void install(File file) { // 安装文件的方法
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		cn.startActivity(intent);
	}

	public File getFile(String urlpath) throws Exception { // 通过文件APK路径下载文件并返回的方法
		Thread.sleep(5000);
		URL url = new URL(urlpath);
		File file = null;
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		InputStream ins = conn.getInputStream();
		// 把他写道文件里面 //判断SD卡存在不
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			file = new File(Environment.getExternalStorageDirectory(),
					"update.apk");
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = ins.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			ins.close();
			fos.close();
		} else {
			Message ms = new Message();
			ms.what = 5;
			ha.sendMessage(ms);
		}
		return file;
	}
}
