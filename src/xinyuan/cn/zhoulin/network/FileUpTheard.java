package xinyuan.cn.zhoulin.network;

import android.os.Handler;
import android.os.Message;
/**
 * 
 */
public class FileUpTheard implements Runnable {
	private Handler ha;
	private String url;
	private byte[] bt;
	private int i;

	public FileUpTheard(Handler ha, String url, byte[] bt,int i) {
		this.ha = ha;
		this.url = url;
		this.bt = bt;
		this.i=i;
	}

	public void run() {
		String m = FileUt.uploadFile(url, bt,i);
		Message ms = new Message();
		ms.obj = m;
		ha.sendMessage(ms);
	}

}
