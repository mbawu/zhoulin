package xinyuan.cn.zhoulin.model;

import java.io.Serializable;

import com.android.volley.toolbox.NetworkImageView;

public class Product implements Serializable {
	private String orderlist_id;


	private String product_id;
	private String product_name;
	private String product_photo;
	private String product_attribute;
	private double store_price; //商品价
	private double reference_price; //参考价
	
	
	public String getProduct_attribute() {
		return product_attribute;
	}

	public void setProduct_attribute(String product_attribute) {
		this.product_attribute = product_attribute;
	}

	public double getReference_price() {
		return reference_price;
	}

	public void setReference_price(double reference_price) {
		this.reference_price = reference_price;
	}

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
	public String getOrderlist_id() {
		return orderlist_id;
	}

	public void setOrderlist_id(String orderlist_id) {
		this.orderlist_id = orderlist_id;
	}
}
