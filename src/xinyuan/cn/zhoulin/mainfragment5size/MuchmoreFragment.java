package xinyuan.cn.zhoulin.mainfragment5size;

import xinyuan.cn.zhoulin.Commodity;
import xinyuan.cn.zhoulin.LoginActivity;
import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.activitys2.muchmore.ContactActivity;
import xinyuan.cn.zhoulin.activitys2.muchmore.SuggesActivity;
import xinyuan.cn.zhoulin.tools.Banbenshengji;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 菜单栏里“更多”的菜单页面
 */
public class MuchmoreFragment extends Fragment implements OnClickListener {
	private String tag = "MuchmoreFragment";
	private View v;
	private View suggestion;
	private View update;
	private View about;
	private View phone;
	public Boolean gc = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		gc = true;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.munchmorefragment, container, false);
			initView();
			initlistener();
		}
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}
		return v;
	}
	/**
	 * 
	 */
	private void initlistener() {
		suggestion.setOnClickListener(this);
		about.setOnClickListener(this);
		update.setOnClickListener(this);
		phone.setOnClickListener(this);
	}
	/**
	 * 
	 */
	private void initView() {
		update = v.findViewById(R.id.update);
		suggestion = v.findViewById(R.id.sugeestion);
		about = v.findViewById(R.id.about);
		phone = v.findViewById(R.id.phone);
	}
	/**
	 * 
	 */
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.sugeestion:
			if (Myapplication.login) {
				getActivity().startActivity(
						new Intent(getActivity(), SuggesActivity.class));
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			} else {
				getActivity().startActivity(
						new Intent(getActivity(), LoginActivity.class));
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}

			break;
		case R.id.update:
			Toast.makeText(getActivity(), "正在检查最新版本，请稍后", 2000).show();
			new Banbenshengji(
					"http://www.xinquanxinyuan.com/apkversion/xml/zhoulin.xml",
					getActivity()).jiancha();

			break;
		case R.id.about:
			startActivity(new Intent(getActivity(), ContactActivity.class));
			getActivity().overridePendingTransition(R.anim.view_in_from_right,
					R.anim.view_out_to_left);
			break;
		case R.id.phone:
			TextView num = (TextView) v.findViewById(R.id.number);
			new AlertDialog.Builder(getActivity()).setMessage("\t\t确定拨打电话\n\t\t\t"+num.getText().toString())
		     .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
		  
		         @Override 
		         public void onClick(DialogInterface dialog, int which) { 
		         // 点击“确认”后的操作 
		        	 TextView num = (TextView) v.findViewById(R.id.number);
		        	 Intent phoneIntent = new Intent("android.intent.action.CALL",
		 					Uri.parse("tel:" + "010-63703006"));
		 			getActivity().startActivity(phoneIntent);
		         } 
		     }) 
		     .setNegativeButton("返回", new DialogInterface.OnClickListener() { 
		  
		         @Override 
		         public void onClick(DialogInterface dialog, int which) { 
		         // 点击“返回”后的操作,这里不设置没有任何操作 
		         } 
		     }).show(); 
			
			break;
		default:

			break;
		}

	}
}