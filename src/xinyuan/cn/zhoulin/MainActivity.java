package xinyuan.cn.zhoulin;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import xinyuan.cn.zhoulin.loginchanger.ExitLoginActivity;
import xinyuan.cn.zhoulin.mainfragment5size.MachineFragment;
import xinyuan.cn.zhoulin.mainfragment5size.MuchmoreFragment;
import xinyuan.cn.zhoulin.mainfragment5size.MainFragment;
import xinyuan.cn.zhoulin.mainfragment5size.SearChFragment;
import xinyuan.cn.zhoulin.mainfragment5size.ShopListFragment;
import xinyuan.cn.zhoulin.mainfragment5size.MachineFragment.Machineshoplistener;
import xinyuan.cn.zhoulin.mainfragment5size.SearChFragment.SearChListener;
import xinyuan.cn.zhoulin.views.order.PersonCenter;
import xinyuan.cn.zhoulin.zxing.CaptureActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.slidingmenu.lib.SlidingMenu;

public class MainActivity extends FragmentActivity implements OnClickListener,
		SearChListener, Machineshoplistener { // 主Activity包含5个主Fragment
	private SlidingMenu menu;
	private View men;
	private LinearLayout ly;
	private LinearLayout lu;
	private LinearLayout ly1; //首页 
	private LinearLayout ly2; //搜索
	private LinearLayout ly3; //列表
	private LinearLayout ly4; //购物车
	private LinearLayout ly5; //个人中心
	private LinearLayout ly6; //扫一扫
	private LinearLayout ly7; //更多
	private TextView tit;
	private MainFragment sl; //首页商品展示页面
	private SearChFragment sf; //查询页面
	private ShopListFragment sp; //商品列表页面
	private MuchmoreFragment mm;  //更多页面
	private MachineFragment mf; //扫一扫页面
	private PersonCenter of;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private View delet;
	private View loginOut;

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE); //去掉标题
		setContentView(R.layout.mainactivity);
		Myapplication.getInstance().addActivity(this);
		fragmentManager = getSupportFragmentManager();
		initmenu();
		initView();
		initListener();
		if (sl == null) {
			sl = new MainFragment();
		}
		changeFragment(1, ly1);
		if (!Myapplication.sp.getString("username", "").equals("")) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("store_id", Myapplication.store_id);
			ha.put("act", UrlPath.ACT_LOGIN);
			ha.put("username", Myapplication.sp.getString("username", ""));
			ha.put("password", Myapplication.sp.getString("password", ""));
			//进入APP首先检查是否有SharedPreferences文件，如果有的话就自动读取并登录。
			Myapplication.client.postWithURL(UrlPath.SERVER_URL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject arg0) {
							try {
								switch (arg0.getInt("code")) {
								case 1:
									Myapplication.seskey = (String) arg0
											.get("seskey");
									Myapplication.login = true;
									Toast.makeText(MainActivity.this, "登录成功",
											2000).show();
									break;
								default:
									Toast.makeText(MainActivity.this,
											arg0.getString("msg"), 2000).show();
									break;
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							Toast.makeText(MainActivity.this, "您的网络异常", 2000)
									.show();
						}
					});

		}
	}

	private void initmenu() {
		menu = new SlidingMenu(this);// 初始化组件
		men = findViewById(R.id.menu);
		menu.setMode(SlidingMenu.LEFT);// 设置菜单的位置
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置菜单滑动的样式
		menu.setBehindWidth(Myapplication.width / 2);// 菜单宽度
		menu.setFadeDegree(0.35f);  //SlidingMenu滑动时的渐变程度
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);//使SlidingMenu附加在Activity上
		menu.setMenu(R.layout.menu);// 添加菜单
		
	}

	private void initView() {
		lu = (LinearLayout) findViewById(R.id.lu);
		ly = (LinearLayout) findViewById(R.id.ly);
		ly1 = (LinearLayout) findViewById(R.id.ly1);
		ly2 = (LinearLayout) findViewById(R.id.ly2);
		ly3 = (LinearLayout) findViewById(R.id.ly3);
		ly4 = (LinearLayout) findViewById(R.id.ly4);
		ly5 = (LinearLayout) findViewById(R.id.ly5);
		ly6 = (LinearLayout) findViewById(R.id.ly6);
		ly7 = (LinearLayout) findViewById(R.id.ly7);
		delet = findViewById(R.id.delet);
		tit = (TextView) findViewById(R.id.title);
		loginOut=findViewById(R.id.loginOut);
	}

	private void initListener() {
		men.setOnClickListener(this);
		delet.setOnClickListener(this);
		ly1.setOnClickListener(this);
		ly2.setOnClickListener(this);
		ly3.setOnClickListener(this);
		ly4.setOnClickListener(this);
		ly5.setOnClickListener(this);
		ly6.setOnClickListener(this);
		ly7.setOnClickListener(this);
		loginOut.setOnClickListener(this);
		ly6.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					v.setBackgroundResource(R.drawable.menu_backan);
					break;
				case MotionEvent.ACTION_UP:
					v.setBackgroundResource(R.drawable.menu_back);
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ly1:
			if (sl == null) {
				sl = new MainFragment();
			} else if (sl.gc) {
				sl = new MainFragment();
			}
			changeFragment(1, v);
			menu.toggle();// 滑动的方法
			break;
		case R.id.ly2:
			if (sf == null) {
				sf = new SearChFragment();
			} else if (sf.gc) {
				sf = new SearChFragment();
			}
			changeFragment(2, v);
			menu.toggle();// 滑动的方法
			break;
		case R.id.ly3:
			if (sp == null) {
				sp = new ShopListFragment();
			} else if (sp.gc) {
				sp = new ShopListFragment();
			}
			changeFragment(3, v);
			menu.toggle();// 滑动的方法
			break;
		case R.id.ly4:
			if (mf == null) {
				mf = new MachineFragment();
			} else if (mf.gc) {
				mf = new MachineFragment();
			}
			changeFragment(4, v);
			menu.toggle();// 滑动的方法
			break;
		case R.id.ly5:
			if (of == null) {
				of = new PersonCenter();
			} else if (of.gc) {
				of = new PersonCenter();
			}
			changeFragment(5, v);
			menu.toggle();// 滑动的方法
			break;
		case R.id.ly6:
			startActivity(new Intent(MainActivity.this, CaptureActivity.class));
			this.overridePendingTransition(R.anim.view_in_from_right,
					R.anim.view_out_to_left);
			break;
		case R.id.ly7:
			if (mm == null) {
				mm = new MuchmoreFragment();
			} else if (mm.gc) {
				mm = new MuchmoreFragment();
			}
			changeFragment(7, v);
			menu.toggle();// 滑动的方法
			break;
		case R.id.menu:
			menu.toggle();// 滑动的方法
			break;
		case R.id.delet:
			dele();
			break;
		case R.id.loginOut: //点击该按钮退出登录
//			loginOut.setVisibility(View.GONE);
			startActivity(
					new Intent(this, ExitLoginActivity.class));
			break;
		default:
			break;
		}
	}

	
	/**
	 * 点击菜单判断点击哪一个菜单并切换到相应的fragment
	 * @param i 第几个菜单
	 * @param v  view控件
	 */
	private void changeFragment(int i, View v) {
		switch (i) {
		case 1:
			fragmentTransaction = fragmentManager.beginTransaction();
			//将主页面中的LinearLayout控件替换成对应的fragment
			fragmentTransaction.replace(R.id.ly, sl, "salefragment");
			changer(1, v);
			//隐藏删除按钮
			delet.setVisibility(View.GONE);
			loginOut.setVisibility(View.GONE);
			//设置标题文字
			tit.setText("首页");
			break;
		case 2:
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.ly, sf, "searchfragment");
			changer(2, v);
			tit.setText("搜索");
			delet.setVisibility(View.GONE);
			loginOut.setVisibility(View.GONE);
			break;
		case 3:
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.ly, sp, "shopfragment");
			changer(3, v);
			tit.setText("商品列表");
			delet.setVisibility(View.GONE);
			loginOut.setVisibility(View.GONE);
			break;
		case 4:
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.ly, mf, "machinefragment");
			changer(4, v);
			tit.setText("购物车");
			delet.setVisibility(View.VISIBLE);
			loginOut.setVisibility(View.GONE);
			break;
		case 5:
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.ly, of, "orderfragment");
			changer(5, v);
			tit.setText("个人中心");
			delet.setVisibility(View.GONE);
			if(Myapplication.login)
			loginOut.setVisibility(View.VISIBLE);
			else
				loginOut.setVisibility(View.GONE);
			break;
		case 6:

			break;
		case 7:
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.ly, mm, "munchmorefragment");
			changer(7, v);
			tit.setText("更多");
			delet.setVisibility(View.GONE);
			loginOut.setVisibility(View.GONE);
			break;

		default:
			break;
		}
		fragmentTransaction.commit();
	}

	/**
	 * 点击了某菜单以后重新绘制菜单选项并让菜单背景色变暗
	 * @param i
	 * @param v
	 */
	private void changer(int i, View v) {
		for (int o = 0; o < lu.getChildCount(); o++) {
			switch (o) {
			case 0:
				lu.getChildAt(o).setBackgroundResource(R.drawable.menu_back);
//				lu.getChildAt(o).findViewById(R.id.im)
//						.setBackgroundResource(R.drawable.shou_yean);
//				TextView tv1 = (TextView) lu.getChildAt(o)
//						.findViewById(R.id.tv);
//				tv1.setTextColor(Color.parseColor("#333333"));
				break;
			case 1:
				lu.getChildAt(o).setBackgroundResource(R.drawable.menu_back);
//				lu.getChildAt(o).findViewById(R.id.im)
//						.setBackgroundResource(R.drawable.sou_suoan);
//				TextView tv2 = (TextView) lu.getChildAt(o)
//						.findViewById(R.id.tv);
//				tv2.setTextColor(Color.parseColor("#333333"));
				break;
			case 2:
				lu.getChildAt(o).setBackgroundResource(R.drawable.menu_back);
//				lu.getChildAt(o).findViewById(R.id.im)
//						.setBackgroundResource(R.drawable.lie_biaoan);
//				TextView tv3 = (TextView) lu.getChildAt(o)
//						.findViewById(R.id.tv);
//				tv3.setTextColor(Color.parseColor("#333333"));

				break;
			case 3:
				lu.getChildAt(o).setBackgroundResource(R.drawable.menu_back);
//				lu.getChildAt(o).findViewById(R.id.im)
//						.setBackgroundResource(R.drawable.gou_wuchean);
//				TextView tv4 = (TextView) lu.getChildAt(o)
//						.findViewById(R.id.tv);
//				tv4.setTextColor(Color.parseColor("#333333"));

				break;
			case 4:
				lu.getChildAt(o).setBackgroundResource(R.drawable.menu_back);
//				lu.getChildAt(o).findViewById(R.id.im)
//						.setBackgroundResource(R.drawable.geren_an);
//				TextView tv5 = (TextView) lu.getChildAt(o)
//						.findViewById(R.id.tv);
//				tv5.setTextColor(Color.parseColor("#333333"));
				break;
			case 5:
				lu.getChildAt(o).setBackgroundResource(R.drawable.menu_back);
//				lu.getChildAt(o).findViewById(R.id.im)
//						.setBackgroundResource(R.drawable.saoyisao_an);
//				TextView tv6 = (TextView) lu.getChildAt(o)
//						.findViewById(R.id.tv);
//				tv6.setTextColor(Color.parseColor("#333333"));
				break;
			case 6:
				lu.getChildAt(o).setBackgroundResource(R.drawable.menu_back);
//				lu.getChildAt(o).findViewById(R.id.im)
//						.setBackgroundResource(R.drawable.geng_duoan);
//				TextView tv7 = (TextView) lu.getChildAt(o)
//						.findViewById(R.id.tv);
//				tv7.setTextColor(Color.parseColor("#333333"));
				break;
			default:
				break;
			}

		}
		lu.getChildAt(i - 1).setBackgroundResource(R.drawable.menu_backan);
		switch (i) {
		case 1:
			lu.getChildAt(i - 1).findViewById(R.id.im)
					.setBackgroundResource(R.drawable.shou_ye);
			TextView tv1 = (TextView) lu.getChildAt(i - 1)
					.findViewById(R.id.tv);
			tv1.setTextColor(Color.parseColor("#ffffff"));

			break;
		case 2:
			lu.getChildAt(i - 1).findViewById(R.id.im)
					.setBackgroundResource(R.drawable.sou_suo);
			TextView tv2 = (TextView) lu.getChildAt(i - 1)
					.findViewById(R.id.tv);
			tv2.setTextColor(Color.parseColor("#ffffff"));

			break;
		case 3:
			lu.getChildAt(i - 1).findViewById(R.id.im)
					.setBackgroundResource(R.drawable.lie_biao);
			TextView tv3 = (TextView) lu.getChildAt(i - 1)
					.findViewById(R.id.tv);
			tv3.setTextColor(Color.parseColor("#ffffff"));

			break;
		case 4:
			lu.getChildAt(i - 1).findViewById(R.id.im)
					.setBackgroundResource(R.drawable.gou_wuche);
			TextView tv4 = (TextView) lu.getChildAt(i - 1)
					.findViewById(R.id.tv);
			tv4.setTextColor(Color.parseColor("#ffffff"));

			break;
		case 5:
			lu.getChildAt(i - 1).findViewById(R.id.im)
					.setBackgroundResource(R.drawable.geren);
			TextView tv5 = (TextView) lu.getChildAt(i - 1)
					.findViewById(R.id.tv);
			tv5.setTextColor(Color.parseColor("#ffffff"));

			break;
		case 6:
			lu.getChildAt(i - 1).findViewById(R.id.im)
					.setBackgroundResource(R.drawable.saoyisao);
			TextView tv6 = (TextView) lu.getChildAt(i - 1)
					.findViewById(R.id.tv);
			tv6.setTextColor(Color.parseColor("#ffffff"));

			break;
		case 7:
			lu.getChildAt(i - 1).findViewById(R.id.im)
					.setBackgroundResource(R.drawable.geng_duo);
			TextView tv7 = (TextView) lu.getChildAt(i - 1)
					.findViewById(R.id.tv);
			tv7.setTextColor(Color.parseColor("#ffffff"));
			break;

		default:
			break;
		}

	}

	//点击返回按钮提示退出对话框
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK) { // KEYCODE_BACK=4;
			tuichu();
			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

	//退出对话框
	public void tuichu() {
		new AlertDialog.Builder(MainActivity.this)
				.setMessage("确定退出?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						setResult(RESULT_OK);//
						try {
							Myapplication.machineCachetool
									.saveCache(Myapplication.machineCachelist);
						} catch (IOException e) {

							e.printStackTrace();
						}
						Myapplication.getInstance().exit();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						setResult(RESULT_CANCELED);//
					}
				}).show();
	}

	//删除商品的消息提示框
	public void dele() {
		new AlertDialog.Builder(MainActivity.this)
				.setMessage("确定清除勾选商品?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if (mf == null) {
							mf = new MachineFragment();
							Toast.makeText(MainActivity.this, "请稍等界面正在初始化",
									2000).show();
							changeFragment(4, ly4);
							menu.toggle();// 滑动的方法
						} else if (mf.gc) {
							mf = new MachineFragment();
							Toast.makeText(MainActivity.this, "请稍等界面正在初始化",
									2000).show();
							changeFragment(4, ly4);
							menu.toggle();// 滑动的方法
						} else {
							mf.delet();
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	
	/**
	 * 使用全局变量tiao记录下来切出程序时候显示的哪个fragment，当回到程序的时候调用该方法并恢复到切出时候的状态
	 */
	protected void onResume() {
		super.onResume();
		if (Myapplication.tiao) {
			switch (Myapplication.tiaosize) {
			case 1:
				if (sl == null) {
					sl = new MainFragment();
				} else if (sl.gc) {
					sl = new MainFragment();
				}
				changeFragment(1, ly1);
				Myapplication.tiao = false;
				break;
			case 2:
				if (sf == null) {
					sf = new SearChFragment();
				} else if (sf.gc) {
					sf = new SearChFragment();
				}
				changeFragment(2, ly2);
				Myapplication.tiao = false;
				break;
			case 3:
				if (sp == null) {
					sp = new ShopListFragment();
				} else if (sp.gc) {
					sp = new ShopListFragment();
				}
				changeFragment(3, ly3);
				Myapplication.tiao = false;
				break;
			case 4:
				if (mf == null) {
					mf = new MachineFragment();
				} else if (mf.gc) {
					mf = new MachineFragment();
				}
				changeFragment(4, ly4);
				Myapplication.tiao = false;
				break;
			case 5:
				if (Myapplication.login) {
					if (of == null) {
						of = new PersonCenter();
					} else if (of.gc) {
						of = new PersonCenter();
					}
					changeFragment(5, ly5);
				} else {
					startActivity(new Intent(MainActivity.this,
							LoginActivity.class));
					this.overridePendingTransition(R.anim.view_in_from_right,
							R.anim.view_out_to_left);
				}
				Myapplication.tiao = false;
				break;

			default:
				break;
			}

		}
	}

	@Override
	public void onshop() {
		changeFragment(1, ly1);

	}

}
