package xinyuan.cn.zhoulin.network;

import xinyuan.cn.zhoulin.Myapplication;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 
 */
public class ImageViewAdapTools {

	public static ImageView getImageView(Context cn, int kuansize, int gaosize,
			int kuanjianju,int gaojianju) { // ��Ҫ�õ���Ļ��ȣ�����ImageView
		ImageView im = new ImageView(cn);
		int s=Myapplication.width-(kuanjianju*(kuansize-1));
		int b=Myapplication.height-(gaojianju*(gaosize-1));
		im.setLayoutParams(new LinearLayout.LayoutParams(s/kuansize, b/gaosize));
		return im;
	}

	public static ImageView getImageView(Context cn,int kuan,int jianju) { // ��Ҫ�õ���Ļ��ȵļ���֮��������ImageView
        
		ImageView im = new ImageView(cn);
		int s=Myapplication.width-(jianju*(kuan-1));
		im.setLayoutParams(new LinearLayout.LayoutParams(s/kuan,s/kuan));
		return im;

	}

}
