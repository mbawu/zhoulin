package xinyuan.cn.zhoulin.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.model.Product;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
/**
 * 
 */
public class MachineCache {
	private File fi = new File(getPath() + File.separator + "demoo.txt");
	private Context cn;

	public MachineCache(Context cn) {
		this.cn = cn;
	}
	/**
	 * 
	 */
	public ArrayList<Product> readCache()
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		ArrayList<Product> ha = null;
		if (!fi.exists()) {
			fi.createNewFile();
			FileOutputStream fo = new FileOutputStream(fi);
			ObjectOutputStream oos = new ObjectOutputStream(fo);
			oos.writeObject(new ArrayList<Product>());
			oos.close();
		}
		FileInputStream fis = new FileInputStream(fi);
		ObjectInputStream ois = new ObjectInputStream(fis);
		ha = (ArrayList<Product>) ois.readObject();
		ois.close();
		return ha;
	}
	/**
	 * 
	 */
	public void saveCache(ArrayList<Product> ha) throws IOException {
		FileOutputStream fo = new FileOutputStream(fi);
		ObjectOutputStream oos = new ObjectOutputStream(fo);
		oos.writeObject(ha);
		oos.close();
	}
	/**
	 * 
	 */
	public String getPath() {
		File fi = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			fi = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "demoo");
			if (!fi.exists()) {
				fi.mkdir();
			}
			return fi.toString();
		} else {
			return null;
		}

	}
	/**
	 * 
	 */
	public void dele() {
		fi.delete();
	}
	/**
	 * 
	 */
	public void refresh() {
		for (int i = 0, len = Myapplication.machineCachelist.size(); i < len; i++) {
			if (Myapplication.machineCachelist.get(i).getChecked()) {
				Myapplication.machineCachelist
						.remove(Myapplication.machineCachelist.get(i));
				i--;
				len--;
			}
		}

	}
	/**
	 * 
	 */
	public void add(Product cb) {
		if (Myapplication.machineCachelist.size() > 19) {
			Toast.makeText(cn, "购物车数量不能超过20条哦!", 2000).show();
		} else {
			if (Myapplication.machineCachelist.size() == 0) {
				Myapplication.machineCachelist.add(cb);
			} else {
				for (int i = 0; i < Myapplication.machineCachelist.size(); i++) {
					Product ba = Myapplication.machineCachelist.get(i);
					if (cb.getProduct_id().equals(ba.getProduct_id())) {
						ba.setNum(cb.getNum() + ba.getNum());
						return;
					}
				}
				Myapplication.machineCachelist.add(cb);
			}
		}

	}
	/**
	 * 
	 */
	public void remove(int position) {
		Myapplication.machineCachelist.remove(position);
	}
}
