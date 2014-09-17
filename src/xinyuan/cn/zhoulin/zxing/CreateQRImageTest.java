package xinyuan.cn.zhoulin.zxing;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
/**
 * @�๦��˵��: ��ɶ�ά��ͼƬʾ��
 * @����:������
 * @ʱ��:2013-4-18����11:10:42
 * @�汾:V1.0
 */
public class CreateQRImageTest
{
	private ImageView sweepIV;
	private int QR_WIDTH = 200, QR_HEIGHT = 200;
	/**
	 * @��������˵��: ��ɶ�ά��ͼƬ,ʵ��ʹ��ʱҪ��ʼ��sweepIV,��Ȼ�ᱨ��ָ�����
	 * @����:������
	 * @ʱ��:2013-4-18����11:14:16
	 * @����: @param url Ҫת���ĵ�ַ���ַ�,����������
	 * @return void
	 * @throws
	 */
	
	//Ҫת���ĵ�ַ���ַ�,����������
	public void createQRImage(String url)
	{
		try
		{
			//�ж�URL�Ϸ���
			if (url == null || "".equals(url) || url.length() < 1)
			{
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			//ͼ�����ת����ʹ���˾���ת��
			BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			//�������ﰴ�ն�ά����㷨�������ɶ�ά���ͼƬ��
			//����forѭ����ͼƬ����ɨ��Ľ��
			for (int y = 0; y < QR_HEIGHT; y++)
			{
				for (int x = 0; x < QR_WIDTH; x++)
				{
					if (bitMatrix.get(x, y))
					{
						pixels[y * QR_WIDTH + x] = 0xff000000;
					}
					else
					{
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			//��ɶ�ά��ͼƬ�ĸ�ʽ��ʹ��ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			//��ʾ��һ��ImageView����
			sweepIV.setImageBitmap(bitmap);
		}
		catch (WriterException e)
		{
			e.printStackTrace();
		}
	}
}
