package xinyuan.cn.zhoulin.model;

import android.widget.TextView;

public class OrderDetailsBean {
	private String order_id;
	private String product_name;
	private String product_num;
	private String product_photo;
	private double product_price;
	private double product_totalprice;
	private String product_id;

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_num() {
		return product_num;
	}

	public void setProduct_num(String product_num) {
		this.product_num = product_num;
	}

	public String getProduct_photo() {
		return product_photo;
	}

	public void setProduct_photo(String product_photo) {
		this.product_photo = product_photo;
	}

	public double getProduct_price() {
		return product_price;
	}

	public void setProduct_price(double product_price) {
		this.product_price = product_price;
	}

	public double getProduct_totalprice() {
		return product_totalprice;
	}

	public void setProduct_totalprice(double product_totalprice) {
		this.product_totalprice = product_totalprice;
	}

}
