package xinyuan.cn.zhoulin.model;

import xinyuan.cn.zhoulin.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class MyDilog extends AlertDialog {

	Context context;

	public MyDilog(Context context) {
		super(context);
		this.context = context;
	}

	public MyDilog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog);
	}

}