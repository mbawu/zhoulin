package xinyuan.cn.zhoulin.mainfragment5size;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import xinyuan.cn.zhoulin.Commodity;
import xinyuan.cn.zhoulin.LoginActivity;
import xinyuan.cn.zhoulin.MachineCommitOrderActivity;
import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.model.Product;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
/**
 * 
 */
public class MachineFragment extends Fragment implements OnClickListener {
	private String tag = "MachineFragment";
	private View v;
	private TextView go;
	private TextView total_price;
	private CheckBox checkBox;
	private ListView lv;
	private Myad ad;
	private Boolean chex = true;
	public Boolean gc = false;
	private double totalprice = 0;
	private LinearLayout gwckong;
	private Button shop;
	private Machineshoplistener ms;
	private LinearLayout vv;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (v == null) {
			v = inflater.inflate(R.layout.machinefragment, container, false);
			inittotalprice();
			initView();
			initListener();
		}
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}
		return v;
	}

	public interface Machineshoplistener {
		public void onshop();
	}
	/**
	 * 
	 */
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			ms = (Machineshoplistener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString());
		}

	}
	/**
	 * 
	 */
	private void inittotalprice() {
		if (Myapplication.machineCachelist == null) {
			try {
				Myapplication.machineCachelist = Myapplication.machineCachetool
						.readCache();
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		totalprice = 0;
		for (int i = 0; i < Myapplication.machineCachelist.size(); i++) {
			if (!Myapplication.machineCachelist.get(i).getChecked()) {
				chex = false;
			} else {
				totalprice = totalprice
						+ Myapplication.machineCachelist.get(i)
								.getStore_price()
						* Myapplication.machineCachelist.get(i).getNum();
			}
		}
	}
	/**
	 * 
	 */
	private void initListener() {
		go.setOnClickListener(this);
		checkBox.setOnClickListener(this);
		shop.setOnClickListener(this);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.id);
				ImageView add = (ImageView) arg1.findViewById(R.id.add);
				Intent in = new Intent(getActivity(), Commodity.class);
				in.putExtra("id", id.getText().toString());
				getActivity().startActivity(in);
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
		});
	}
	/**
	 * 
	 */
	private void initView() {
		go = (TextView) v.findViewById(R.id.go);
		checkBox = (CheckBox) v.findViewById(R.id.checkbox);
		if (chex) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}
		vv = (LinearLayout) v.findViewById(R.id.vv);
		gwckong = (LinearLayout) v.findViewById(R.id.gwckong);
		shop = (Button) v.findViewById(R.id.shop);
		total_price = (TextView) v.findViewById(R.id.total_price);
		total_price.setText("￥ " + totalprice);
		lv = (ListView) v.findViewById(R.id.lv);
		ad = new Myad();
		lv.setAdapter(ad);
	}

	public void onPause() {
		Log.e(tag, tag + "onPause");
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Myapplication.machine_refresh) {
			Myapplication.machine_refresh = false;
			inittotalprice();
			total_price.setText("￥ " + totalprice);
			chex = false;
			checkBox.setChecked(false);
			ad.notifyDataSetChanged();
		} else {

		}

	}
	/**
	 * 
	 */
	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			if (Myapplication.machineCachelist == null) {
				gwckong.setVisibility(View.VISIBLE);
				go.setVisibility(View.GONE);
				vv.setVisibility(View.INVISIBLE);
				return 0;
			} else if (Myapplication.machineCachelist.size() == 0) {
				gwckong.setVisibility(View.VISIBLE);
				go.setVisibility(View.GONE);
				vv.setVisibility(View.INVISIBLE);
				return 0;
			} else {
				gwckong.setVisibility(View.GONE);
				vv.setVisibility(View.VISIBLE);
				go.setVisibility(View.VISIBLE);
				return Myapplication.machineCachelist.size();
			}
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Product cb = Myapplication.machineCachelist.get(position);
			Viewholder vh;
			if (convertView == null) {
				vh = new Viewholder();
				convertView = Myapplication.lf.inflate(R.layout.machinelv_item,
						null);
				vh.add = (ImageView) convertView.findViewById(R.id.add);
				vh.im = ((CheckBox) convertView.findViewById(R.id.im));
				vh.name = ((TextView) convertView.findViewById(R.id.name));
				vh.num = ((EditText) convertView.findViewById(R.id.num));
				vh.photo = ((NetworkImageView) convertView
						.findViewById(R.id.photo));
				vh.price = ((TextView) convertView.findViewById(R.id.price));
				vh.remove = ((ImageView) convertView.findViewById(R.id.remove));
				vh.id = (TextView) convertView.findViewById(R.id.id);
				convertView.setTag(vh);

			} else {
				vh = (Viewholder) convertView.getTag();

			}
			if (cb.getChecked()) {
				vh.im.setChecked(true);
			} else {
				vh.im.setChecked(false);
			}
			vh.im.setTag(cb);
			vh.im.setOnClickListener(MachineFragment.this);
			vh.name.setText(cb.getProduct_name());
			vh.num.setText("" + cb.getNum());
			vh.price.setText("￥ " + cb.getStore_price());
			vh.id.setText(cb.getProduct_id());
			vh.add.setOnClickListener(MachineFragment.this);
			vh.add.setTag(cb);
			vh.remove.setOnClickListener(MachineFragment.this);
			vh.remove.setTag(cb);
			Myapplication.client.getImageForNetImageView(cb.getProduct_photo(),
					vh.photo, R.drawable.ic_launcher);
			Log.e("zcsfdfd", cb.getProduct_photo());
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			View pare = (View) v.getParent();
			EditText ed = (EditText) pare.findViewById(R.id.num);
			int s = Integer.parseInt(ed.getText().toString());
			ed.setText("" + (s + 1));
			Product tt = (Product) v.getTag();
			tt.add();
			if (tt.getChecked()) {
				totalprice = totalprice + tt.getStore_price();
				total_price.setText("￥ " + totalprice);
			}

			break;
		case R.id.shop:
			ms.onshop();
			break;
		case R.id.remove:
			View paren = (View) v.getParent();
			EditText edd = (EditText) paren.findViewById(R.id.num);
			int ss = Integer.parseInt(edd.getText().toString());
			if (ss == 1) {
			} else {
				edd.setText("" + (ss - 1));
				Product t = (Product) v.getTag();
				t.subtract();
				if (t.getChecked()) {
					totalprice = totalprice - t.getStore_price();
					total_price.setText("￥ " + totalprice);
				}
			}

			break;

		case R.id.go:
			if (Myapplication.login) {
				if (totalprice > 0) {
					ArrayList<Product> al = new ArrayList<Product>();
					for (int i = 0; i < Myapplication.machineCachelist.size(); i++) {
						if (Myapplication.machineCachelist.get(i).getChecked()) {
							al.add(Myapplication.machineCachelist.get(i));
						}
					}
					Intent i = new Intent(getActivity(),
							MachineCommitOrderActivity.class);
					i.putExtra("list", al);
					i.putExtra("xiaoji", totalprice);
					getActivity().startActivity(i);
					getActivity().overridePendingTransition(
							R.anim.view_in_from_right, R.anim.view_out_to_left);
				} else {
					Toast.makeText(getActivity(), "请选择您要购买的商品", 2000).show();
				}
			} else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}

			break;
		case R.id.checkbox:
			CheckBox cbb = (CheckBox) v;
			if (cbb.isChecked()) {
				chex = true;
				for (int i = 0; i < Myapplication.machineCachelist.size(); i++) {
					Myapplication.machineCachelist.get(i).setChecked(true);
				}
				ad.notifyDataSetChanged();
				inittotalprice();
				total_price.setText("￥ " + totalprice);
			} else {
				chex = false;
				for (int i = 0; i < Myapplication.machineCachelist.size(); i++) {
					Myapplication.machineCachelist.get(i).setChecked(false);
				}
				ad.notifyDataSetChanged();
				totalprice = 0;
				total_price.setText("￥ " + "0");
			}
			break;

		default:
			CheckBox vv = (CheckBox) v;
			if (vv.isChecked()) {
				Product cb = (Product) v.getTag();
				cb.setChecked(true);
				totalprice = totalprice + (cb.getNum() * cb.getStore_price());
				total_price.setText("￥ " + totalprice);
			} else {
				Product cb = (Product) v.getTag();
				cb.setChecked(false);
				chex = false;
				checkBox.setChecked(false);
				totalprice = totalprice - (cb.getNum() * cb.getStore_price());
				total_price.setText("￥ " + totalprice);
			}
			break;
		}
	}
	/**
	 * 
	 */
	public void delet() {
		if (chex) {
			Myapplication.machineCachelist.clear();
			try {
				Myapplication.machineCachetool
						.saveCache(Myapplication.machineCachelist);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ad.notifyDataSetChanged();
			totalprice = 0;
			total_price.setText("￥ " + "0");
		} else {
			for (int i = 0, len = Myapplication.machineCachelist.size(); i < len; i++) {
				if (Myapplication.machineCachelist.get(i).getChecked()) {
					Myapplication.machineCachelist
							.remove(Myapplication.machineCachelist.get(i));
					i--;
					len--;
				}
			}
			ad.notifyDataSetChanged();
			totalprice = 0;
			total_price.setText("￥ " + "0");

		}
	}

	/**
	 * 
	 */
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		gc = true;
	}
	/**
	 * 
	 */
	class Viewholder {
		public CheckBox im;
		public NetworkImageView photo;
		public TextView name;
		public ImageView remove;
		public ImageView add;
		public EditText num;
		public TextView price;
		public TextView id;
	}
}
