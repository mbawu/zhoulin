package xinyuan.cn.zhoulin;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import xinyuan.cn.zhoulin.model.Product;
import xinyuan.cn.zhoulin.network.MyHttpClient;
import xinyuan.cn.zhoulin.tools.MachineCache;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
/**
 * 
 */
public class Myapplication extends Application {
	public static SharedPreferences sp;
	public static Editor ed;
	private List<Activity> mList = new LinkedList<Activity>();
	private static Myapplication instance; //全局唯一的实例
	public static LayoutInflater lf;
	public static int width; //屏幕的宽度
	public static int height; //屏幕的高度
	public static MyHttpClient client;
	public static String seskey; //用户密钥
	public static Boolean tiao = false;  //判断是否跳出程序
	public static Boolean login = false; //用户登录状态
	public static int tiaosize = 1;  //记录跳出程序时候的fragment的状态
	public static Boolean machine_refresh = false;
	public static String store_id = "10";  //App标识号
	public static String productItemNum="10"; //首页商品显示多少个产品
	public static String adItemNum="10"; //首页广告显示多少条
	public static String adPage="1"; //首页广告显示第几页的数据
	public static String locality = "file:///android_asset/load.html";
	public static MachineCache machineCachetool;
	public static ArrayList<Product> machineCachelist;
	public static Boolean addressneedflash = false;
	public static Boolean orderneedflash = false;
	public static Boolean addresscreade = false;

	public void onCreate() {
		super.onCreate();
		machineCachetool = new MachineCache(getApplicationContext());
		Log.i("test", "onCreate-->"+(Myapplication.machineCachelist==null));
		sp = getSharedPreferences("zhoulin", MODE_PRIVATE);
		ed = sp.edit();
		lf = LayoutInflater.from(getApplicationContext());
		client = MyHttpClient.getInstance(Myapplication.this
				.getApplicationContext());
		machineCachelist=new ArrayList<Product>();
		try {
			machineCachelist = machineCachetool.readCache();
		} catch (StreamCorruptedException e) {
			machineCachetool.dele();
			e.printStackTrace();
		} catch (IOException e) {
			machineCachetool.dele();
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			machineCachetool.dele();
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	public synchronized static Myapplication getInstance() {
		if (null == instance) {
			instance = new Myapplication();
		}
		return instance;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}
	/**
	 * 
	 */
	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
	/**
	 * 
	 */
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
	
	//获取拼接出来的请求字符串
	public static String getUrl(HashMap<String, String> ha)
	{
		Iterator iter=ha.entrySet().iterator();
		String url=UrlPath.SERVER_URL;
		int count=0;
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			if(count==0)
				url=url+"?"+key+"="+val;
			else
				url=url+"&"+key+"="+val;
			count++;
			}
		return url;
	}

}
