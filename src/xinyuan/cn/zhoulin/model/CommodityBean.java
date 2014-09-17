package xinyuan.cn.zhoulin.model;

import java.io.Serializable;

import com.android.volley.toolbox.NetworkImageView;

public class CommodityBean implements Serializable {
	private String product_id;
	private String product_name;
	private String product_photo;
	private double store_price;
	private int num = 1;
	private Boolean Checked = true;
	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getNum() {
		return num;
	}

	public void add() {
		num = num + 1;
	}

	public void subtract() {
		num = num - 1;
	}

	public Boolean getChecked() {
		return Checked;
	}

	public void setChecked(Boolean checked) {
		Checked = checked;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_photo() {
		return product_photo;
	}

	public void setProduct_photo(String product_photo) {
		this.product_photo = product_photo;
	}

	public double getStore_price() {
		return store_price;
	}

	public void setStore_price(double store_price) {
		this.store_price = store_price;
	}

}
