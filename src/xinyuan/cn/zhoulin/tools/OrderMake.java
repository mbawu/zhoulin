package xinyuan.cn.zhoulin.tools;

import java.util.ArrayList;

import android.util.Log;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.model.Product;
/**
 * 
 */
public class OrderMake {
	/**
	 * 
	 */
	public String makeOrder(ArrayList<Product> al) {
		StringBuffer json = new StringBuffer();
		json.append("[");
		for (int i = 0; i < al.size(); i++) {
			if (al.get(i).getChecked()) {
				json.append(toJson(al.get(i)) + ",");
			}

		}
		String str = json.toString().substring(0, json.toString().length() - 1)
				+ "]";
		return str;

	}
	/**
	 * 
	 */
	public String toJson(Product cm) {
		StringBuffer json = new StringBuffer();
		json.append("{\"store_id\":\"").append(Myapplication.store_id)
		.append("\"");
		json.append(",\"product_photo\":\"").append(cm.getProduct_photo())
		.append("\"");
		json.append(",\"product_name\":\"").append(cm.getProduct_name())
		.append("\"");
		if(cm.getProduct_attribute()==null)
			json.append(",\"product_attribute\":\"").append("").append("\"");
		else
			json.append(",\"product_attribute\":\"").append(cm.getProduct_attribute()).append("\"");
		json.append(",\"reference_price\":\"").append("1.00")
		.append("\"");
		json.append(",\"product_id\":\"").append(cm.getProduct_id())
				.append("\"");
		json.append(",\"product_num\":\"").append("" + cm.getNum())
				.append("\"");
		json.append(",\"store_price\":\"").append("" + cm.getStore_price())
				.append("\"");
		json.append("}");
		return json.toString();
	}
}
