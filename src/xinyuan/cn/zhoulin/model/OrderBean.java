package xinyuan.cn.zhoulin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderBean implements Serializable {
	private String allprice;
	private String price;
	private String createtime;
	private String flag;
	private String order_flag;
	private int position;
	private String mobile;
	private String order_code;
	private String order_id;
	private String realname;
	private String express_code;
	private String express_enname;
	private String address;
	private List<Product> li;
	private String releasetime;
	private String payway;
	private String ispay;
	private String orderHc;
	private String order_subject;
	private String freight;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public String getOrder_subject() {
		return order_subject;
	}

	public void setOrder_subject(String order_subject) {
		this.order_subject = order_subject;
	}

	public String getOrderHc() {
		return orderHc;
	}

	public void setOrderHc(String orderHc) {
		this.orderHc = orderHc;
	}

	public String getIspay() {
		return ispay;
	}

	public void setIspay(String ispay) {
		this.ispay = ispay;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	public String getReleasetime() {
		return releasetime;
	}

	public void setReleasetime(String releasetime) {
		this.releasetime = releasetime;
	}

	public int getPosition() {
		return position;
	}

	public int getPoistion() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getOrder_flag() {
		return order_flag;
	}

	public void setOrder_flag(String order_flag) {
		this.order_flag = order_flag;
	}

	public String getAllprice() {
		return allprice;
	}

	public void setAllprice(String allprice) {
		this.allprice = allprice;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getExpress_code() {
		return express_code;
	}

	public void setExpress_code(String express_code) {
		this.express_code = express_code;
	}

	public String getExpress_enname() {
		return express_enname;
	}

	public void setExpress_enname(String express_enname) {
		this.express_enname = express_enname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Product> getLi() {
		return li;
	}

	public void setLi(List<Product> li) {
		this.li = li;
	}
}
