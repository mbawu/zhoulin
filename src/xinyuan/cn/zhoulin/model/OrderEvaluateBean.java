package xinyuan.cn.zhoulin.model;

public class OrderEvaluateBean {
	private String product_id;
	private String product_name;
	private String product_photo;
	private String evaluation_flag;
	private String product_num;
	private String product_price;

	public String getProduct_num() {
		return product_num;
	}

	public void setProduct_num(String product_num) {
		this.product_num = product_num;
	}

	public String getProduct_price() {
		return product_price;
	}

	public void setProduct_price(String product_price) {
		this.product_price = product_price;
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

	public String getEvaluation_flag() {
		return evaluation_flag;
	}

	public void setEvaluation_flag(String evaluation_flag) {
		this.evaluation_flag = evaluation_flag;
	}

}
