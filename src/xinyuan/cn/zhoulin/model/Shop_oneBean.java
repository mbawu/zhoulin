package xinyuan.cn.zhoulin.model;

import java.io.Serializable;
import java.util.RandomAccess;

public class Shop_oneBean implements Serializable {
	private String category1_id;
	private String category1_name;
	private String category1_intro;
	private String category1_images;
	private Boolean show = false;

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

	public String getCategory1_id() {
		return category1_id;
	}

	public void setCategory1_id(String category1_id) {
		this.category1_id = category1_id;
	}

	public String getCategory1_name() {
		return category1_name;
	}

	public void setCategory1_name(String category1_name) {
		this.category1_name = category1_name;
	}

	public String getCategory1_intro() {
		return category1_intro;
	}

	public void setCategory1_intro(String category1_intro) {
		this.category1_intro = category1_intro;
	}

	public String getCategory1_images() {
		return category1_images;
	}

	public void setCategory1_images(String category1_images) {
		this.category1_images = category1_images;
	}

}
