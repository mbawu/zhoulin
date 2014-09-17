package xinyuan.cn.zhoulin.mainfragment5size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.Myapplication;
import xinyuan.cn.zhoulin.R;
import xinyuan.cn.zhoulin.UrlPath;
import xinyuan.cn.zhoulin.activitys2.search.SearChResoutListActivity;
import xinyuan.cn.zhoulin.model.SearchShopBean;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class SearChFragment extends Fragment implements OnClickListener {
	private String tag = "SearchFragment";
	private MyHotad hotad;
	private MyHistoryad historyad;
	private List<SearchShopBean> hotli;
	private List<SearchShopBean> historyli;
	private EditText content;
	private LinearLayout search;
	private ListView historylv;
	private ListView hotlv;
	private View v;
	public Boolean gc = false;
	private ProgressBar pr;
	private PopupWindow pw;
	private View popuView;
	private View pop;
	private View footView;

	private void initpop() {
		pw = new PopupWindow(popuView, ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pw.setBackgroundDrawable(new BitmapDrawable());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		gc = true;
	}

	public void onCreate(Bundle savedInstanceState) {
		Log.e(tag, tag + "onCreate");
		super.onCreate(savedInstanceState);
	}

	public interface SearChListener { //

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.searchfragment, container, false);
			initView();
			initlistener();
			initData();
		}
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}

		return v;
	}

	private void initData() {
		if (getActivity() == null) {
			return;
		}
		initHistoryliData();
		//获取热门搜索的信息
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("act", UrlPath.ACT_HOTWORD);
		ha.put("acttag", "list");
		ha.put("store_id", Myapplication.store_id);
		Log.i("test", "HotSeach-->"+Myapplication.getUrl(ha));
		Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						String content = response.toString();
						try {
							JSONObject jb = new JSONObject(content);
							Log.i("test", "HotSeach-->"+jb.getInt("code"));
							Log.i("test", "HotSeach-->"+jb.toString());
							if (jb.getInt("code") == 1) {
								hotli.clear();
								JSONArray ja = jb.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jbb = ja.getJSONObject(i);
									SearchShopBean sp = new SearchShopBean();
									sp.setName(jbb.getString("hotword"));
									hotli.add(sp);
								}
								hotad.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						pr.setVisibility(View.GONE);
					}
				}, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						pr.setVisibility(View.GONE);
						Log.i("test", "HotSeach error-->"+error.getMessage());
					}
				});
	}

	private void initHistoryliData() {
		//先查询有没有历史查询信息
		String content = Myapplication.sp.getString("history", "");
		//如果有历史记录的话
		if (!content.equals("")) {
			String[] st = content.split(",");
			//清空历史查询记录的List
			historyli.clear();
			//如果历史记录大于10条，只取前十条
			if (st.length > 10) {
				for (int i = 1; i < 10; i++) {
					SearchShopBean sp = new SearchShopBean();
					sp.setName(st[st.length - 1 - i]);
					if (sp.getName().equals("")) {
					} else {
						historyli.add(sp);//如果不是空的查询值的话就将它保存
					}

				}
			} else {
				for (int i = 0; i < st.length; i++) {
					SearchShopBean sp = new SearchShopBean();
					sp.setName(st[st.length - 1 - i]);
					if (sp.getName().equals("")) {
					} else {
						historyli.add(sp);
					}
				}
			}
			historyad.notifyDataSetChanged();

		} else {
			historyad.notifyDataSetChanged();
		}
	}

	private void initlistener() {
		//点击历史查询记录的子项，获取到文本并显示在查询文本框中
		historylv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView tv = (TextView) arg1.findViewById(R.id.tv);
				String con = tv.getText().toString();
				content.setText(con);
				pw.dismiss();

			}
		});
		hotlv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView tv = (TextView) arg1.findViewById(R.id.tv);
				String con = tv.getText().toString();
				Intent in = new Intent(getActivity(),
						SearChResoutListActivity.class);
				in.putExtra("name", con);
				getActivity().startActivity(in);
				getActivity().overridePendingTransition(
						R.anim.view_in_from_right, R.anim.view_out_to_left);
			}
		});
		/*
		 * content.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { pw.showAsDropDown(v);
		 * pw.setFocusable(true); pw.setOutsideTouchable(true); pw.update(); }
		 * });
		 */
		pop.setOnClickListener(this);
		footView.setOnClickListener(this);
		search.setOnTouchListener(new OnTouchListener() {
			//点击查询按钮
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String conten = content.getText().toString();
				if (conten.equals("")) {
					Toast.makeText(getActivity(), "请输入您要查找的商品", 2000).show();
				} else {
					//取出历史查询记录
					String cc = Myapplication.sp.getString("history", "");
					if (cc.equals("")) { //如果记录为空，就保存本次的搜索记录
						cc = "," + conten;
					} else { //如果不为空就在之前的内容后面加上本次搜索记录
						if (conten.equals(cc.substring(cc.lastIndexOf(",") + 1,
								cc.length()))) {
						} else {
							cc = cc + "," + conten;
						}

					}
					//写入editor历史记录
					Myapplication.ed.putString("history", cc);
					Myapplication.ed.commit();
					//跳转到搜索结果界面
					Intent in = new Intent(getActivity(),
							SearChResoutListActivity.class);
					in.putExtra("name", conten);
					getActivity().startActivity(in);
					getActivity().overridePendingTransition(
							R.anim.view_in_from_right, R.anim.view_out_to_left);
				}
				return false;
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		initHistoryliData();
	}

	private void initView() {
		footView = Myapplication.lf.inflate(R.layout.popfootview, null); //清除历史记录
		popuView = Myapplication.lf.inflate(R.layout.po, null); //查看历史查询按钮的界面
		pop = v.findViewById(R.id.pop);  //查看历史界面的三角按钮
		initpop(); //初始化历史查询记录窗口
		pr = (ProgressBar) v.findViewById(R.id.pr);
		historyli = new ArrayList<SearchShopBean>();
		hotli = new ArrayList<SearchShopBean>();
		content = (EditText) v.findViewById(R.id.content); //查询的内容
		content.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT); 
		search = (LinearLayout) v.findViewById(R.id.search); //包含查询图片的Layout
		hotlv = (ListView) v.findViewById(R.id.hotlv); //热门搜索的listView
		historylv = (ListView) popuView.findViewById(R.id.historylv); //历史查询信息的listView
		historyad = new MyHistoryad();
		hotad = new MyHotad();
		hotlv.setAdapter(hotad);
		historylv.addFooterView(footView);
		historylv.setAdapter(historyad);
	}

	@Override
	public void onPause() {
		super.onPause();
		Myapplication.client.getmRequestQueue().cancelAll(UrlPath.SERVER_URL);
	}

	/**
	 * 显示热门搜索信息的adapter
	 * @author Administrator
	 *
	 */
	private class MyHotad extends BaseAdapter {

		public int getCount() {
			if (hotli == null) {
				return 0;
			} else {
				return hotli.size();
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
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(R.layout.search_item,
						null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.tv);
			SearchShopBean sp = hotli.get(position);
			tv.setText(sp.getName());
			switch (position) {
			case 0:
				convertView.findViewById(R.id.im).setBackgroundResource(
						R.drawable.i1);
				break;
			case 1:
				convertView.findViewById(R.id.im).setBackgroundResource(
						R.drawable.i2);
				break;
			case 2:
				convertView.findViewById(R.id.im).setBackgroundResource(
						R.drawable.i3);
				break;
			case 3:
				convertView.findViewById(R.id.im).setBackgroundResource(
						R.drawable.i4);
				break;
			case 4:
				convertView.findViewById(R.id.im).setBackgroundResource(
						R.drawable.i5);
				break;
			case 5:
				convertView.findViewById(R.id.im).setBackgroundResource(
						R.drawable.i6);
				break;
			case 6:
				convertView.findViewById(R.id.im).setBackgroundResource(
						R.drawable.i7);
				break;
			case 7:
				convertView.findViewById(R.id.im).setBackgroundResource(
						R.drawable.i8);
				break;
			case 8:
				convertView.findViewById(R.id.im).setBackgroundResource(
						R.drawable.i9);
				break;
			case 9:
				convertView.findViewById(R.id.im).setBackgroundResource(
						R.drawable.i10);
				break;

			default:

				break;
			}
			return convertView;
		}
	}

	private class MyHistoryad extends BaseAdapter {

		public int getCount() {
			if (historyli == null) {
				return 0;
			} else {
				return historyli.size();
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
			if (convertView == null) {
				convertView = Myapplication.lf.inflate(R.layout.search_item2,
						null);
			}
			SearchShopBean sp = historyli.get(position);
			TextView tv = (TextView) convertView.findViewById(R.id.tv);
			tv.setText(sp.getName());
			tv.setHeight(60);
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop:
			pw.showAsDropDown(v); //点击历史记录的小三角以下拉单的形式弹出
			pw.setFocusable(true); //获取焦点
			pw.setOutsideTouchable(true);  
			pw.update();
			break;

		default:
			new AlertDialog.Builder(getActivity())
					.setMessage("确定清除历史记录?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Myapplication.ed.remove("history"); //清除history文件
									Myapplication.ed.commit();  //提交
									historyli.clear(); //清除保存记录的list
									initHistoryliData(); //重新刷新数据
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			break;
		}

	}
}
